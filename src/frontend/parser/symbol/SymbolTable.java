package frontend.parser.symbol;

import backend.errorhandler.CompilerError;

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
        if (parent != null) return parent.getSymbol(name);
        return null;
    }

    public Symbol getSymbol(String name, SymbolType type)
    {
        for (Symbol symbol : this.symbolList)
        {
            if (symbol.getName().equals(name) && symbol.getType() == type)
            {
                return symbol;
            }
        }
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

    public Symbol getLastSymbol()
    {
        if (!this.symbolList.isEmpty())
        {
            return this.symbolList.get(this.symbolList.size() - 1);
        }
        return null;
    }

}
