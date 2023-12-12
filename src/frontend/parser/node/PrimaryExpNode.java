package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import frontend.errorhandler.symbol.SymbolTable;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * 基本表达式 PrimaryExp → '(' Exp ')' | LVal | Number
 */
public class PrimaryExpNode extends Node implements Expression
{

    Token LPARENTToken;
    ExpNode expNode;
    Token RPARENTToken;
    LValNode lValNode;
    NumberNode numberNode;

    public PrimaryExpNode(NodeType type)
    {
        super(type);
    }

    @Override
    public void parseNode()
    {
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.LPARENT)
        {
            this.LPARENTToken = Parser.getToken(TokenType.LPARENT);
            this.expNode = new ExpNode();
            this.expNode.parseNode();
            this.RPARENTToken = Parser.getToken(TokenType.RPARENT);
        }
        else if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.IDENFR)
        {
            this.lValNode = new LValNode();
            this.lValNode.parseNode();
        }
        else
        {
            this.numberNode = new NumberNode();
            this.numberNode.parseNode();
        }
    }

    @Override
    public Token getOPToken()
    {
        return null;
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        if (this.LPARENTToken != null)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.LPARENTToken + "\n", true);
            this.expNode.outputNode(destFile);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.RPARENTToken.toString() + "\n", true);
        }
        else if (this.lValNode != null)
        {
            this.lValNode.outputNode(destFile);
        }
        else
        {
            this.numberNode.outputNode(destFile);
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public String toString()
    {
        String temp = "";
        if (this.LPARENTToken != null)
        {
            temp += this.LPARENTToken.getValue();
            temp += this.expNode.toString();
            temp += this.RPARENTToken.getValue();
        }
        else if (this.lValNode != null)
        {
            temp += this.lValNode.toString();
        }
        else
        {
            temp += this.numberNode.toString();
        }
        return temp;
    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        if (this.expNode != null)
        {
            this.expNode.parseSymbol(st);
        }
        if (this.lValNode != null)
        {
            this.lValNode.parseSymbol(st);
        }
    }

    @Override
    public ExpType getExpType()
    {
        if (this.expNode != null)
        {
            return this.expNode.getExpType();
        }
        else if (this.lValNode != null)
        {
            return this.lValNode.getExpType();
        }
        else
        {
            return ExpType.INT;
        }
    }

    @Override
    public void parseIR()
    {
        if (this.expNode != null)
        {
            this.expNode.parseIR();
        }
        else if (this.lValNode != null)
        {
            this.lValNode.parseIR();
        }
        else
        {
            this.numberNode.parseIR();
        }
    }
}
