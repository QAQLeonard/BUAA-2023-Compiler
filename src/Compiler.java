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
            ErrorHandler errorHandler = new ErrorHandler();
            Lexer lexer = new Lexer();
            lexer.run();
            lexer.output();
            Parser parser = new Parser();
            parser.run();
            // parser.output();
            errorHandler.output();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}