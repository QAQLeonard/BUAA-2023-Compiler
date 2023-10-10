package backend.errorhandler;

import backend.errorhandler.symbol.SymbolTable;
import frontend.parser.Parser;

import java.util.ArrayList;
import java.util.Collections;

public class ErrorHandler
{
    public static ArrayList<CompilerException> exceptionList;

    public ErrorHandler()
    {
        exceptionList = new ArrayList<>();
    }
    public void run()
    {
        try
        {
            Parser.parseSymbol();
            Parser.RootSymbolTable.check();
        }
        catch (CompilerException e)
        {
            exceptionList.add(e);
        }
    }

    public void output()
    {
        Collections.sort(exceptionList);
    }
}
