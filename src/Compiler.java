import backend.MipsGenerator;
import frontend.error.errorhandler.ErrorHandler;
import frontend.lexer.Lexer;
import frontend.parser.Parser;
import frontend.ir.LLVMGenerator;

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
            // errorHandler.output();
            LLVMGenerator.run();
            LLVMGenerator.outputLLVM();
            MipsGenerator.outputMips();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}