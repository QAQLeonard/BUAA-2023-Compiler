import backend.errorhandler.ErrorHandler;
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
            ErrorHandler errorHandler = new ErrorHandler();
            Parser parser = new Parser();
            parser.run();
            parser.output();

        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}