package lexer.token;

public class IdentifierToken extends Token
{
    public IdentifierToken(TokenType type, String value, int lineNumber)
    {
        super(type, value, lineNumber);
    }
}
