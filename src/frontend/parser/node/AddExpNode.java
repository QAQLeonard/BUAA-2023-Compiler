package frontend.parser.node;

import frontend.ir.LLVMGenerator;
import frontend.ir.value.Value;
import frontend.ir.value.instructions.Operator;
import frontend.ir.value.BuildFactory;
import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import frontend.error.symbol.SymbolTable;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

import static frontend.ir.utils.LLVMUtils.*;
import static frontend.ir.LLVMGenerator.*;

/**
 * AddExp → MulExp <br>
 * | MulExp ('+' | '−') AddExp
 */
public class AddExpNode extends Node implements Expression
{

    MulExpNode mulExpNode;
    AddExpNode addExpNode;
    Token PLUSToken;
    Token MINUSToken;

    public AddExpNode()
    {
        super(NodeType.AddExp);

    }

    @Override
    public void parseNode()
    {
        mulExpNode = new MulExpNode();
        mulExpNode.parseNode();
        Token token = Parser.peekToken(0);
        assert token != null;
        if (token.getType() == TokenType.PLUS)
        {
            PLUSToken = Parser.getToken(TokenType.PLUS);
        }
        else if (token.getType() == TokenType.MINU)
        {
            MINUSToken = Parser.getToken(TokenType.MINU);
        }
        else
        {
            return;
        }
        addExpNode = new AddExpNode();
        addExpNode.parseNode();
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        mulExpNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
        Token OPToken = this.getOPToken();
        if (OPToken != null)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, OPToken + "\n", true);
            addExpNode.outputNode(destFile);
        }
    }

    @Override
    public String toString()
    {
        String temp = "";
        temp += mulExpNode.toString();
        Token OPToken = this.getOPToken();
        if (OPToken != null)
        {
            temp += OPToken.getValue();
            temp += addExpNode.toString();
        }
        return temp;
    }

    @Override
    public Token getOPToken()
    {
        if (this.PLUSToken != null)
        {
            return this.PLUSToken;
        }
        else if (this.MINUSToken != null)
        {
            return this.MINUSToken;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        mulExpNode.parseSymbol(st);
        if (addExpNode != null)
        {
            addExpNode.parseSymbol(st);
        }
    }

    @Override
    public ExpType getExpType()
    {
        if (this.addExpNode == null)
        {
            return this.mulExpNode.getExpType();
        }
        else
        {
            ExpType expType1 = this.mulExpNode.getExpType();
            ExpType expType2 = this.addExpNode.getExpType();
            if (expType1 == ExpType.INT && expType2 == ExpType.INT)
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
        if (isConst)
        {
            Integer val = saveVal;
            Operator op = saveOp;
            saveVal = null;
            this.mulExpNode.parseIR();
            if (val != null)
            {
                LLVMGenerator.saveVal = calculate(op, val, LLVMGenerator.saveVal);
            }
            if (this.addExpNode != null)
            {
                LLVMGenerator.saveOp = getOPToken().getType() == TokenType.PLUS ? Operator.Add : Operator.Sub;
                addExpNode.parseIR();
            }
        }
        else
        {
            Value value = LLVMGenerator.tmpValue;
            Operator op = LLVMGenerator.tmpOp;

            LLVMGenerator.tmpValue = null;
            this.mulExpNode.parseIR();
            if (value != null)
            {
                LLVMGenerator.tmpValue = BuildFactory.getBinaryInst(LLVMGenerator.blockStack.peek(), op, value, LLVMGenerator.tmpValue);
            }
            if (addExpNode != null)
            {
                LLVMGenerator.tmpOp = getOPToken().getType() == TokenType.PLUS ? Operator.Add : Operator.Sub;
                this.addExpNode.parseIR();
            }
        }
    }
}
