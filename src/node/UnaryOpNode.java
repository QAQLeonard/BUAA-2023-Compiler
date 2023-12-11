package node;

import error.CompilerError;
import error.errorhandler.ErrorHandler;
import error.ErrorType;
import token.Token;
import token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * 单目运算符 UnaryOp → '+' | '−' | '!'
 */
public class UnaryOpNode extends Node
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
        switch (Objects.requireNonNull(token).getType())
        {
            case PLUS -> this.PLUSToken = Parser.getToken(TokenType.PLUS);
            case MINU -> this.MINUSToken = Parser.getToken(TokenType.MINU);
            case NOT -> this.NOTToken = Parser.getToken(TokenType.NOT);
            default ->
                    ErrorHandler.addError(new CompilerError(ErrorType.UNEXPECTED_TOKEN, "UnaryOpNode: parse failed", token.getLineNumber()));
        }
    }

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
