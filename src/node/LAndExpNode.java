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

import static ir.IRGenerator.*;

/**
 * 逻辑与表达式 LAndExp → EqExp | EqExp '&&' LAndExp
 */
public class LAndExpNode extends Node implements Expression
{

    EqExpNode eqExpNode;
    LAndExpNode lAndExpNode;
    Token ANDToken;
    public LAndExpNode()
    {
        super(NodeType.LAndExp);
    }

    @Override
    public void parseNode()
    {
        eqExpNode = new EqExpNode();
        eqExpNode.parseNode();
        Token token = Parser.peekToken(0);
        assert token != null;
        if (token.getType() == TokenType.AND)
        {
            this.ANDToken = Parser.getToken(TokenType.AND);
            lAndExpNode = new LAndExpNode();
            lAndExpNode.parseNode();
        }

    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        this.eqExpNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);

        if (this.ANDToken != null)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.ANDToken + "\n", true);
            this.lAndExpNode.outputNode(destFile);
        }

    }

    @Override
    public Token getOPToken()
    {
        if (this.ANDToken != null)
        {
            return this.ANDToken;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        this.eqExpNode.parseSymbol(st);
        if (this.lAndExpNode != null)
        {
            this.lAndExpNode.parseSymbol(st);
        }
    }

    @Override
    public ExpType getExpType()
    {
        if (this.lAndExpNode == null)
        {
            return this.eqExpNode.getExpType();
        }
        else
        {
            if (this.eqExpNode.getExpType() == ExpType.INT && this.lAndExpNode.getExpType() == ExpType.INT)
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
        // LAndExp -> EqExp | EqExp '&&' LAndExp
        BasicBlock trueBlock = curTrueBlock;
        BasicBlock falseBlock = curFalseBlock;
        BasicBlock tmpTrueBlock = curTrueBlock;
        BasicBlock thenBlock = null;
        if (lAndExpNode != null)
        {
            thenBlock = BuildFactory.buildBasicBlock(functionStack.peek());
            tmpTrueBlock = thenBlock;
        }
        curTrueBlock = tmpTrueBlock;
        tmpValue = null;
        eqExpNode.parseIR();
        BuildFactory.getBrInst(blockStack.peek(), tmpValue, curTrueBlock, curFalseBlock);
        curTrueBlock = trueBlock;
        curFalseBlock = falseBlock;
        if (lAndExpNode != null)
        {
            blockStack.push(thenBlock);
            lAndExpNode.parseIR();
        }
    }

}
