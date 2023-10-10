package backend.errorhandler;

public class CompilerException extends Exception implements Comparable<CompilerException>
{
    private ExceptionType type;
    private String message;
    private int line;

    public CompilerException()
    {
        this.type = null;
        this.message = "";
        this.line = 0;
    }
    public CompilerException(ExceptionType type, String message, int line)
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
    public void printStackTrace()
    {
        System.out.println("Error: " + this.type + " " + this.message + " at line " + this.line);
    }

    @Override
    public int compareTo(CompilerException other)
    {
        return this.line - other.line;
    }

}
