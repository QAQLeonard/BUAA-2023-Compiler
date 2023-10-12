package frontend.parser.symbol;
import backend.errorhandler.CompilerException;

import java.util.ArrayList;

public class SymbolTable
{
    private ArrayList<Symbol> symbolList;

    private SymbolTable parent;

    private ArrayList<SymbolTable> children;

    public SymbolTable()
    {
        this.symbolList = new ArrayList<>();
        this.children = new ArrayList<>();
        this.parent = null;
    }

    public SymbolTable(SymbolTable parent)
    {
        this.symbolList = new ArrayList<>();
        this.children = new ArrayList<>();
        this.parent = parent;
        parent.addChild(this);
    }

    public void addSymbol(Symbol symbol)
    {
        this.symbolList.add(symbol);
    }

    public Symbol getSymbol(String name)
    {
        for (Symbol symbol : this.symbolList)
        {
            if (symbol.getName().equals(name))
            {
                return symbol;
            }
        }
        return null;
    }

    public void check() throws CompilerException
    {

    }

    public ArrayList<Symbol> getSymbolList()
    {
        return this.symbolList;
    }

    public void addChild(SymbolTable child)
    {
        if (this.children == null)
        {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }

}
