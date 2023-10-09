package backend.errorhandler.symbol;

public abstract class Symbol
{
    private String name;
    private SymbolType type;

    public Symbol(String name, SymbolType type)
    {
        this.name = name;
        this.type = type;
    }
}
