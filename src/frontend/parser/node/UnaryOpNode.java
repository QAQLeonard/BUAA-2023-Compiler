package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * 单目运算符 UnaryOp → '+' | '−' | '!'
 */
public class UnaryOpNode extends Node implements Expression
{
    Token PLUSToken;
    Token MINUSToken;
    Token NOTToken;

    public UnaryOpNode()
    {
        super(NodeType.UnaryOp);
    }

    @Override
    public void parseNode()
    {
        Token token = Parser.peekToken(0);
        if (Objects.requireNonNull(token).getType() == TokenType.PLUS)
        {
            this.PLUSToken = Parser.getToken();
        }
        else if (Objects.requireNonNull(token).getType() == TokenType.MINU)
        {
            this.MINUSToken = Parser.getToken();
        }
        else
        {
            this.NOTToken = Parser.getToken();
        }
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
            return this.NOTToken;
        }
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        Token OPToken = this.getOPToken();
        FileOperate.outputFileUsingUsingBuffer(destFile, OPToken.toString() + "\n", true);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public String toString()
    {
        Token OPToken = this.getOPToken();
        return OPToken.getValue();
    }
}
