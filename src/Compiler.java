import lexer.Lexer;

import java.io.IOException;

public class Compiler
{
    public static void main(String[] args)
    {
        try
        {
            Lexer lexer = new Lexer();
            lexer.run();
            lexer.output();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}