package frontend.parser.symbol;

import java.util.ArrayList;

public class FUNCSymbol extends Symbol
{
    private String FUNCName;
    private String returnType;
    private ArrayList<Symbol> parameters;
    public FUNCSymbol(String name, String returnType, ArrayList<Symbol> parameters)
    {
        super(name, SymbolType.FUNCTION);
        this.FUNCName = name;
        this.returnType = returnType;
        this.parameters = parameters;
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

    @Override
    public String toString()
    {
        return "FUNCSymbol{" +
                "FUNCName='" + FUNCName + '\'' +
                ", returnType='" + returnType +
                '}';
    }
}
