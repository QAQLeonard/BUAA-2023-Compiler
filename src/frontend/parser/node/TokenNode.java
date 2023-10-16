package frontend.parser.node;

import backend.errorhandler.CompilerError;
import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.Parser;

public class TokenNode extends Node
{
    Token token;

    public TokenNode()
    {
        super(NodeType.Token);
    }

    public TokenNode(Token token, TokenType type)
    {
        super(NodeType.Token);
        this.token = token;
    }

    @Override
    public void parseNode()
    {
        token = Parser.getToken();
    }
}
