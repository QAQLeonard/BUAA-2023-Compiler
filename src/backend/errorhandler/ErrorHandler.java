package backend.errorhandler;

import error.CompilerError;
import error.ErrorType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
        Set<ErrorType> NOTOUTPUT = new HashSet<>();
        NOTOUTPUT.add(ErrorType.UNDEFINED);
        NOTOUTPUT.add(ErrorType.UNEXPECTED_TOKEN);
        int temp = 0;
        try
        {
            for (CompilerError e : exceptionList)
            {
                if(NOTOUTPUT.contains(e.getType())) continue;
                if(e.getLine()==temp) continue;
                outputFileUsingUsingBuffer(destFile, e + "\n", true);
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
