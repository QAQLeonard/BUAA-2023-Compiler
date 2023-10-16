package backend.errorhandler;

import frontend.parser.Parser;

import java.util.ArrayList;
import java.util.Collections;

public class ErrorHandler
{
    public static ArrayList<CompilerError> exceptionList = new ArrayList<>();

    public void output()
    {
        Collections.sort(exceptionList);
        for (CompilerError e : exceptionList)
        {
            System.out.println(e.toString());
        }
    }

    public static void addError(CompilerError e)
    {
        exceptionList.add(e);
    }
}
