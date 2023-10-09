package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
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
