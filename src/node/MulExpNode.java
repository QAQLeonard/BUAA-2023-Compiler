package node;

import ir.IRGenerator;
import ir.value.BuildFactory;
import ir.value.Value;
import ir.value.instructions.Operator;
import token.Token;
import token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import symbol.SymbolTable;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

import static ir.utils.LLVMUtils.calculate;

/**
 * 乘除模表达式 MulExp → UnaryExp <br>
 * | UnaryExp ('*' | '/' | '%') MulExp
 */
public class MulExpNode extends Node implements Expression
{

    UnaryExpNode unaryExpNode;
    MulExpNode mulExpNode;

    Token MULTToken;
    Token DIVToken;
    Token MODToken;
    boolean isInt;

    public MulExpNode()
    {
        super(NodeType.MulExp);
        unaryExpNode = null;
        mulExpNode = null;
        MULTToken = null;
        DIVToken = null;
        MODToken = null;
    }

    @Override
    public void parseNode()
    {
        this.unaryExpNode = new UnaryExpNode();
        this.unaryExpNode.parseNode();
        // System.out.println(unaryExpNode);
        Token token = Parser.peekToken(0);
        assert token != null;
        switch (token.getType())
        {
            case MULT -> this.MULTToken = Parser.getToken(TokenType.MULT);
            case DIV -> this.DIVToken = Parser.getToken(TokenType.DIV);
            case MOD -> this.MODToken = Parser.getToken(TokenType.MOD);
            default ->
            {
                return;
            }
        }
        mulExpNode = new MulExpNode();
        mulExpNode.isInt = this.isInt;
        mulExpNode.parseNode();
    }

    @Override
    public Token getOPToken()
    {
        if (this.MULTToken != null)
        {
            return this.MULTToken;
        }
        else if (this.DIVToken != null)
        {
            return this.DIVToken;
        }
        else if (this.MODToken != null)
        {
            return this.MODToken;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        this.unaryExpNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
        Token OPtoken = this.getOPToken();
        if (OPtoken != null)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, OPtoken + "\n", true);
            this.mulExpNode.outputNode(destFile);
        }

    }

    @Override
    public String toString()
    {
        String temp = "";
        temp += this.unaryExpNode.toString();
        Token OPtoken = this.getOPToken();
        if (OPtoken != null)
        {
            temp += OPtoken.getValue();
            temp += this.mulExpNode.toString();
        }
        return temp;
    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        this.unaryExpNode.parseSymbol(st);
        if (this.mulExpNode != null)
        {
            this.mulExpNode.parseSymbol(st);
        }
    }

    @Override
    public ExpType getExpType()
    {
        if (this.mulExpNode == null)
        {
            return this.unaryExpNode.getExpType();
        }
        else
        {
            if (this.unaryExpNode.getExpType() == ExpType.INT && this.mulExpNode.getExpType() == ExpType.INT)
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
        // UnaryExp | UnaryExp ('*' | '/' | '%') MulExp
        if (IRGenerator.isConst)
        {
            Integer value = IRGenerator.saveValue;
            Operator op = IRGenerator.saveOp;
            IRGenerator.saveValue = null;
            unaryExpNode.parseIR();
            if (value != null)
            {
                IRGenerator.saveValue = calculate(op, value, IRGenerator.saveValue);
            }
            if (mulExpNode != null)
            {
                switch (getOPToken().getType())
                {
                    case MULT:
                        IRGenerator.saveOp = Operator.Mul;
                        break;
                    case DIV:
                        IRGenerator.saveOp = Operator.Div;
                        break;
                    case MOD:
                        IRGenerator.saveOp = Operator.Mod;
                        break;
                    default:
                        throw new RuntimeException("unknown operator");
                }
                this.mulExpNode.parseIR();
            }
        }
        else
        {
            Value value = IRGenerator.tmpValue;
            Operator op = IRGenerator.tmpOp;
            IRGenerator.tmpValue = null;
            unaryExpNode.parseIR();
            if (value != null)
            {
                IRGenerator.tmpValue = BuildFactory.getBinaryInst(IRGenerator.blockStack.peek(), op, value, IRGenerator.tmpValue);
            }
            if (mulExpNode != null)
            {
                if (getOPToken().getType() == TokenType.MULT)
                {
                    IRGenerator.tmpOp = Operator.Mul;
                }
                else if (getOPToken().getType() == TokenType.DIV)
                {
                    IRGenerator.tmpOp = Operator.Div;
                }
                else
                {
                    IRGenerator.tmpOp = Operator.Mod;
                }
                mulExpNode.parseIR();
            }
        }
    }
}
