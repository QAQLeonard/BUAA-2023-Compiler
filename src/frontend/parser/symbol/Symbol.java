package frontend.parser.symbol;

public abstract class Symbol
{
    private final String name;
    private final SymbolType type;

    public Symbol(String name, SymbolType type)
    {
        this.name = name;
        this.type = type;
    }

    public String getName()
    {
        return this.name;
    }

    public SymbolType getType()
    {
        return this.type;
    }
}
