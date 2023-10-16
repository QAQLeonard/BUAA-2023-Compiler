package frontend.parser.node;

import backend.errorhandler.CompilerError;
import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import frontend.parser.symbol.SymbolTable;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

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
            FileOperate.outputFileUsingUsingBuffer(destFile, OPtoken.toString() + "\n", true);
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
}
