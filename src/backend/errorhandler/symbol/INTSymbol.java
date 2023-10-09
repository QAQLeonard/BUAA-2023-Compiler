package backend.errorhandler.symbol;

public class INTSymbol extends Symbol
{
    private int value;
    private boolean isConstant;
    private boolean isInitialized;

    public INTSymbol(String name, int value, boolean isConstant, boolean isInitialized)
    {
        super(name, SymbolType.INT);
        this.value = value;
        this.isConstant = isConstant;
        this.isInitialized = isInitialized;
    }

    public int getValue()
    {
        return this.value;
    }
    public boolean isConstant()
    {
        return this.isConstant;
    }
}
