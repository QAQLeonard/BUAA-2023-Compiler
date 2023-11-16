package node;

import ir.value.BasicBlock;
import ir.value.BuildFactory;
import token.Token;
import token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import symbol.SymbolTable;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

import static ir.LLVMGenerator.*;
/**
 * 逻辑或表达式 LOrExp → LAndExp | LAndExp '||' LOrExp
 */
public class LOrExpNode extends Node implements Expression
{

    LAndExpNode lAndExpNode;
    LOrExpNode lOrExpNode;
    Token ORToken;
    public LOrExpNode()
    {
        super(NodeType.LOrExp);
    }

    @Override
    public void parseNode()
    {
        lAndExpNode = new LAndExpNode();
        lAndExpNode.parseNode();
        Token token = Parser.peekToken(0);
        assert token != null;
        if (token.getType() == TokenType.OR)
        {
            this.ORToken = Parser.getToken(TokenType.OR);
            lOrExpNode = new LOrExpNode();
            lOrExpNode.parseNode();
        }
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        this.lAndExpNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);

        if (this.ORToken != null)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.ORToken + "\n", true);
            this.lOrExpNode.outputNode(destFile);
        }
    }

    @Override
    public Token getOPToken()
    {
        if (this.ORToken != null)
        {
            return this.ORToken;
        }
        return null;
    }

    @Override
    public void parseSymbol(SymbolTable parent)
    {
        this.lAndExpNode.parseSymbol(parent);
        if(this.lOrExpNode != null)
        {
            this.lOrExpNode.parseSymbol(parent);
        }
    }

    @Override
    public ExpType getExpType()
    {
        if(this.lOrExpNode == null)
        {
            return this.lAndExpNode.getExpType();
        }
        else
        {
            if(this.lAndExpNode.getExpType() == ExpType.INT && this.lOrExpNode.getExpType() == ExpType.INT)
            {
                return ExpType.INT;
            }
            else
            {
                return ExpType.ERROR;
            }
        }
    }

    @Override
    public void parseIR()
    {
        // LOrExp -> LAndExp | LAndExp '||' LOrExp
        BasicBlock trueBlock = curTrueBlock;
        BasicBlock falseBlock = curFalseBlock;
        BasicBlock tmpFalseBlock = curFalseBlock;
        BasicBlock thenBlock = null;
        if (lOrExpNode != null)
        {
            thenBlock = BuildFactory.buildBasicBlock(functionStack.peek());
            tmpFalseBlock = thenBlock;
        }
        curFalseBlock = tmpFalseBlock;
        lAndExpNode.parseIR();
        curTrueBlock = trueBlock;
        curFalseBlock = falseBlock;
        if (lOrExpNode != null)
        {
            blockStack.push(thenBlock);
            lOrExpNode.parseIR();
        }
    }

}
