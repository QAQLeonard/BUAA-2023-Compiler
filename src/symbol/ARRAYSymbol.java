package symbol;

import java.util.ArrayList;

public class ARRAYSymbol extends Symbol
{
    private final int dimension;
    private final boolean isConstant;
    private final boolean isInitialized;
    //// 每一维数组长度
    private final ArrayList<Integer> length;

    public ARRAYSymbol(String name, int dimension, boolean isConstant, boolean isInitialized)
    {
        super(name, SymbolType.ARRAY);
        this.dimension = dimension;
        this.isConstant = isConstant;
        this.isInitialized = isInitialized;
        this.length = new ArrayList<>();
    }

    public boolean isConstant()
    {
        return this.isConstant;
    }

    @Override
    public String toString()
    {
        return "ARRAYSymbol{" +
                "name='" + this.getName() + '\'' +
                ", dimension=" + dimension +
                ", isConstant=" + isConstant +
                ", isInitialized=" + isInitialized +
                ", length=" + length +
                '}';
    }

    public int getDim()
    {
        return dimension;
    }

}
