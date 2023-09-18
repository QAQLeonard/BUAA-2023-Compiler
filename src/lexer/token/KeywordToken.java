package lexer.token;

public class KeywordToken extends Token
{
    public KeywordToken(TokenType type, String value, int lineNumber)
    {
        super(type, value, lineNumber);
    }
}

