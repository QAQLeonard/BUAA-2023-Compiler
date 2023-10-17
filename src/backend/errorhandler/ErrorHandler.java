package backend.errorhandler;

import frontend.lexer.Token;
import frontend.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static utils.FileOperate.CreateFileUsingJava7Files;
import static utils.FileOperate.outputFileUsingUsingBuffer;

public class ErrorHandler
{
    public static ArrayList<CompilerError> exceptionList = new ArrayList<>();

    public void output() throws IOException
    {
        Collections.sort(exceptionList);
        File destFile = new File("error.txt");
        CreateFileUsingJava7Files(destFile);

        int temp = 0;
        try
        {
            for (CompilerError e : exceptionList)
            {
                if(e.getType()==ErrorType.UNDEFINED||e.getType()==ErrorType.UNEXPECTED_TOKEN) continue;
                if(e.getLine()==temp) continue;
                outputFileUsingUsingBuffer(destFile, e.toString() + "\n", true);
                temp = e.getLine();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void addError(CompilerError e)
    {
        exceptionList.add(e);
    }
}
