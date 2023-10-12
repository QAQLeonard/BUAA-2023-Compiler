package frontend.parser.symbol;

public class INTSymbol extends Symbol
{
    private boolean isConstant;
    private boolean isInitialized;

    public INTSymbol(String name, boolean isConstant, boolean isInitialized)
    {
        super(name, SymbolType.INT);
        this.isConstant = isConstant;
        this.isInitialized = isInitialized;
    }

    public boolean isConstant()
    {
        return this.isConstant;
    }

    @Override
    public String toString()
    {
        return "INTSymbol{" +
                "name='" + this.getName() + '\'' +
                ", isConstant=" + isConstant +
                ", isInitialized=" + isInitialized +
                '}';
    }
}
