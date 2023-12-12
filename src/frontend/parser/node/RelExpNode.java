package frontend.parser.node;

import frontend.ir.value.BuildFactory;
import frontend.ir.value.Value;
import frontend.ir.value.instructions.Operator;
import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import frontend.error.symbol.SymbolTable;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

import static frontend.ir.LLVMGenerator.*;

/**
 * 关系表达式 RelExp → AddExp | AddExp ('<' | '>' | '<=' | '>=') RelExp
 */
public class RelExpNode extends Node implements Expression
{

    AddExpNode addExpNode;
    RelExpNode relExpNode;
    Token LSSToken;
    Token GREToken;
    Token LEQToken;
    Token GEQToken;
    boolean isInt;

    public RelExpNode()
    {
        super(NodeType.RelExp);
    }

    @Override
    public void parseNode()
    {
        addExpNode = new AddExpNode();
        addExpNode.parseNode();
        Token token = Parser.peekToken(0);
        assert token != null;
        switch (token.getType())
        {
            case LSS -> this.LSSToken = Parser.getToken(TokenType.LSS);
            case GRE -> this.GREToken = Parser.getToken(TokenType.GRE);
            case LEQ -> this.LEQToken = Parser.getToken(TokenType.LEQ);
            case GEQ -> this.GEQToken = Parser.getToken(TokenType.GEQ);
            default ->
            {
                return;
            }
        }
        relExpNode = new RelExpNode();
        relExpNode.isInt = this.isInt;
        relExpNode.parseNode();
    }

    @Override
    public Token getOPToken()
    {
        if (this.LSSToken != null)
        {
            return this.LSSToken;
        }
        else if (this.GREToken != null)
        {
            return this.GREToken;
        }
        else if (this.LEQToken != null)
        {
            return this.LEQToken;
        }
        else if (this.GEQToken != null)
        {
            return this.GEQToken;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        this.addExpNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
        Token OPToken = this.getOPToken();
        if (OPToken != null)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, OPToken + "\n", true);
            this.relExpNode.outputNode(destFile);
        }
    }

    @Override
    public ExpType getExpType()
    {
        if(this.relExpNode == null)
            return this.addExpNode.getExpType();
        else
        {
            if(this.addExpNode.getExpType() == ExpType.INT && this.relExpNode.getExpType() == ExpType.INT)
                return ExpType.INT;
            else
                return ExpType.ERROR;
        }
    }
    @Override
    public void parseSymbol(SymbolTable st)
    {
        this.addExpNode.parseSymbol(st);
        if (this.relExpNode != null)
        {
            this.relExpNode.parseSymbol(st);
        }
    }

    @Override
    public void parseIR()
    {
        // RelExp -> AddExp | AddExp ('<' | '>' | '<=' | '>=') RelExp
        Value value = tmpValue;
        Operator op = tmpOp;
        tmpValue = null;
        addExpNode.parseIR();
        if (value != null)
        {
            tmpValue = BuildFactory.getBinaryInst(blockStack.peek(), op, value, tmpValue);
        }
        if (relExpNode != null)
        {
            switch (getOPToken().getType())
            {
                case LSS:
                    tmpOp = Operator.Lt;
                    break;
                case LEQ:
                    tmpOp = Operator.Le;
                    break;
                case GRE:
                    tmpOp = Operator.Gt;
                    break;
                case GEQ:
                    tmpOp = Operator.Ge;
                    break;
                default:
                    throw new RuntimeException("Unknown operator");
            }
            relExpNode.parseIR();
        }
    }
}
