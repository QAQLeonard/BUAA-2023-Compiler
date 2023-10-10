package backend.errorhandler.symbol;
import backend.errorhandler.CompilerException;
public class SymbolTable
{
    private Symbol[] symbols;
    private int size;

    private SymbolTable parent;
    public SymbolTable()
    {
        this.symbols = new Symbol[100];
        this.size = 0;
    }

    public void addSymbol(Symbol symbol)
    {
        this.symbols[this.size] = symbol;
        this.size++;
    }

    public Symbol getSymbol(String name)
    {
        for (int i = 0; i < this.size; i++)
        {
            if (this.symbols[i].getName().equals(name))
            {
                return this.symbols[i];
            }
        }
        return null;
    }

    public void check() throws CompilerException
    {

    }
}
