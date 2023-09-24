package frontend.lexer.token;

public class LiteralToken extends Token
{
    public LiteralToken(TokenType type, String value, int lineNumber)
    {
        super(type, value, lineNumber);
    }
}
