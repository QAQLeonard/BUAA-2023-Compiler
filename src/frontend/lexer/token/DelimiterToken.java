package frontend.lexer.token;

public class DelimiterToken extends Token
{
    public DelimiterToken(TokenType type, String value, int lineNumber)
    {
        super(type, value, lineNumber);
    }
}
