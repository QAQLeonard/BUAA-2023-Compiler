package backend.errorhandler.symbol;

import java.util.ArrayList;

public class FUNCSymbol extends Symbol
{
    private String FUNCName;
    private String returnType;
    private ArrayList<Symbol> parameters;
    public FUNCSymbol(String name)
    {
        super(name, SymbolType.FUNCTION);
    }

    public String getFUNCName()
    {
        return this.FUNCName;
    }
    public String getReturnType()
    {
        return this.returnType;
    }
    public ArrayList<Symbol> getParameters()
    {
        return this.parameters;
    }
}
