package lexer.token;

public class OperatorToken extends Token
{
    public OperatorToken(TokenType type, String value, int lineNumber)
    {
        super(type, value, lineNumber);
    }
}
