package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
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
        if (token.getType() == TokenType.MULT)
        {
            this.MULTToken = Parser.getToken();
        }
        else if (token.getType() == TokenType.DIV)
        {
            this.DIVToken = Parser.getToken();
        }
        else if (token.getType() == TokenType.MOD)
        {
            this.MODToken = Parser.getToken();
        }
        else
        {
            return ;
        }
        mulExpNode = new MulExpNode();
        mulExpNode.parseNode();
    }

    @Override
    public Token getOPToken()
    {
        if(this.MULTToken != null)
        {
            return this.MULTToken;
        }
        else if(this.DIVToken != null)
        {
            return this.DIVToken;
        }
        else if(this.MODToken != null)
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
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType())+"\n", true);
        Token OPtoken = this.getOPToken();
        if(OPtoken != null)
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
        if(OPtoken != null)
        {
            temp += OPtoken.getValue();
            temp += this.mulExpNode.toString();
        }
        return temp;
    }
}
