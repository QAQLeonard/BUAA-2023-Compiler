package frontend.errorhandler.symbol;

import java.util.ArrayList;

public class SymbolTable
{
    private final ArrayList<Symbol> symbolList;

    private final SymbolTable parent;

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
        if (parent != null) return parent.getSymbol(name);
        return null;
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

    public boolean isDefinitionUnique(String symbolName)
    {
        for (Symbol s : this.symbolList)
        {
            if (s.getName().equals(symbolName))
            {
                return false;
            }
        }
        return true;
    }

    public SymbolTable getParent()
    {
        return this.parent;
    }


}
