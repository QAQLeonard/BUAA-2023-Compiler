import frontend.lexer.Lexer;
import frontend.parser.Parser;

import java.io.IOException;

public class Compiler
{
    public static void main(String[] args)
    {
        try
        {
            Lexer lexer = new Lexer();
            lexer.run();
            // lexer.output();
            Parser parser = new Parser();
            parser.run();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}