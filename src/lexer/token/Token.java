package lexer.token;

public class Token
{
    private final TokenType type;
    private final String value;
    private final int lineNumber;


    public Token(TokenType type, String value, int lineNumber)
    {
        this.type = type;
        this.value = value;
        this.lineNumber = lineNumber;
    }

    public String getType()
    {
        return type.toString();
    }

    public String getValue()
    {
        return value;
    }

    public int getLineNumber()
    {
        return lineNumber;
    }

    public String toString()
    {
        return type.toString() + " " + value;
    }
}

