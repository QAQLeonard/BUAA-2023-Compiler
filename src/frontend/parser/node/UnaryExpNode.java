package frontend.parser.node;

import backend.errorhandler.CompilerException;
import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * 一元表达式 UnaryExp → PrimaryExp <br>
 * | Ident '(' [FuncRParams] ')'<br>
 * | UnaryOp UnaryExp<br>
 */
public class UnaryExpNode extends Node implements Expression
{

    PrimaryExpNode primaryExpNode;
    Token IDENFRToken;
    Token LPARENTToken;
    FuncRParamsNode funcRParamsNode;
    Token RPARENTToken;
    UnaryOpNode unaryOpNode;
    UnaryExpNode unaryExpNode;
    public UnaryExpNode()
    {
        super(NodeType.UnaryExp);
    }

    @Override
    public void parseNode() throws CompilerException
    {
        // UnaryExp → UnaryOp UnaryExp
        if (ParserUtils.UnaryOpTokenTypes.contains(Objects.requireNonNull(Parser.peekToken(0)).getType()))
        {
            this.unaryOpNode = new UnaryOpNode();
            this.unaryOpNode.parseNode();
            this.unaryExpNode = new UnaryExpNode();
            this.unaryExpNode.parseNode();
        }
        // UnaryExp → Ident '(' [FuncRParams] ')'
        else if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.IDENFR && Objects.requireNonNull(Parser.peekToken(1)).getType() == TokenType.LPARENT)
        {
            this.IDENFRToken = Parser.getToken(TokenType.IDENFR);
            this.LPARENTToken = Parser.getToken(TokenType.LPARENT);
            if (Objects.requireNonNull(Parser.peekToken(0)).getType() != TokenType.RPARENT)
            {
                this.funcRParamsNode = new FuncRParamsNode();
                this.funcRParamsNode.parseNode();
            }
            this.RPARENTToken = Parser.getToken(TokenType.RPARENT);
        }
        // UnaryExp → PrimaryExp
        else
        {
            this.primaryExpNode = new PrimaryExpNode(NodeType.PrimaryExp);
            this.primaryExpNode.parseNode();
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
        if (this.unaryOpNode != null)
        {
            this.unaryOpNode.outputNode(destFile);
            this.unaryExpNode.outputNode(destFile);
        }
        else if (this.IDENFRToken != null)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.IDENFRToken.toString() + "\n", true);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.LPARENTToken.toString() + "\n", true);
            if (this.funcRParamsNode != null)
            {
                this.funcRParamsNode.outputNode(destFile);
            }
            FileOperate.outputFileUsingUsingBuffer(destFile, this.RPARENTToken.toString() + "\n", true);
        }
        else
        {
            this.primaryExpNode.outputNode(destFile);
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public String toString()
    {
        String temp = "";
        if (this.unaryOpNode != null)
        {
            temp += this.unaryOpNode.toString();
            temp += this.unaryExpNode.toString();
        }
        else if (this.IDENFRToken != null)
        {
            temp += this.IDENFRToken.getValue() + "\n";
            temp += this.LPARENTToken.getValue() + "\n";
            if (this.funcRParamsNode != null)
            {
                temp += this.funcRParamsNode.toString();
            }
            temp += this.RPARENTToken.getValue() + "\n";
        }
        else
        {
            temp += this.primaryExpNode.toString();
        }
        return temp;
    }
}
