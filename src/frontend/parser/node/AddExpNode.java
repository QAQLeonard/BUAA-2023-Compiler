package frontend.parser.node;

import backend.errorhandler.CompilerException;
import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

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
    public void parseNode() throws CompilerException
    {
        mulExpNode = new MulExpNode();
        mulExpNode.parseNode();
        Token token = Parser.peekToken(0);
        assert token != null;
        if (token.getType() == TokenType.PLUS)
        {
            PLUSToken = Parser.getToken();
        }
        else if (token.getType() == TokenType.MINU)
        {
            MINUSToken = Parser.getToken();
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
            FileOperate.outputFileUsingUsingBuffer(destFile, OPToken.toString() + "\n", true);
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
}
