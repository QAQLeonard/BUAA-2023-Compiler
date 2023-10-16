package frontend.parser.symbol;

import java.util.ArrayList;
import java.util.Arrays;

public class ARRAYSymbol extends Symbol
{
    private int dimension;
    private boolean isConstant;
    private boolean isInitialized;
    //// 每一维数组长度
    private ArrayList<Integer> length;

    public ARRAYSymbol(String name, int dimension, boolean isConstant, boolean isInitialized, Integer... lengths)
    {
        super(name, SymbolType.ARRAY);
        this.dimension = dimension;
        this.isConstant = isConstant;
        this.isInitialized = isInitialized;
        this.length = new ArrayList<>(Arrays.asList(lengths));
    }
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
