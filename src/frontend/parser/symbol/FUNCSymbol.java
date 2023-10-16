package frontend.parser.symbol;

import frontend.parser.node.ExpType;
import frontend.parser.node.StmtNode;

import java.util.ArrayList;

public class FUNCSymbol extends Symbol
{
    private String FUNCName;
    private ExpType returnType;
    private ArrayList<Symbol> parameters;

    public ArrayList<StmtNode> ReturnStmtNodeList;
    public FUNCSymbol(String name, ExpType returnType, ArrayList<Symbol> parameters)
    {
        super(name, SymbolType.FUNCTION);
        this.FUNCName = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.ReturnStmtNodeList = new ArrayList<>();
    }

    public String getFUNCName()
    {
        return this.FUNCName;
    }
    public ExpType getReturnType()
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
