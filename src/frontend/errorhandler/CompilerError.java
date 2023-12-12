package frontend.errorhandler;

public class CompilerError implements Comparable<CompilerError>
{
    private final ErrorType type;
    private final String message;
    private final int line;

    public CompilerError()
    {
        this.type = null;
        this.message = "";
        this.line = 0;
    }
    public CompilerError(ErrorType type, String message, int line)
    {
        this.type = type;
        this.message = message;
        this.line = line;
    }

    @Override
    public String toString()
    {
        return this.line + " " + this.type;
    }

    @Override
    public int compareTo(CompilerError other)
    {
        return this.line - other.line;
    }

    public ErrorType getType()
    {
        return type;
    }
    public int getLine()
    {
        return line;
    }
}
