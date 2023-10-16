package backend.errorhandler;

public class CompilerError implements Comparable<CompilerError>
{
    private ErrorType type;
    private String message;
    private int line;

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

//    @Override
//    public void printStackTrace()
//    {
//        System.out.println("Error: " + this.type + " " + this.message + " at line " + this.line);
//    }

    @Override
    public int compareTo(CompilerError other)
    {
        return this.line - other.line;
    }
    
}
