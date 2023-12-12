package frontend.parser.node;

import frontend.errorhandler.symbol.SymbolTable;

import java.io.File;
import java.io.IOException;

public abstract class Node
{
    private final NodeType type;

    public Node(NodeType type)
    {
        this.type = type;
    }

    public void outputNode(File destFile) throws IOException
    {
        System.out.println("Node outputNode ERROR when parsing " + this.getType());
    }

    public void parseNode()
    {
        System.out.println("Node parseNode ERROR when parsing " + this.getType());
    }

    public NodeType getType()
    {
        return type;
    }

    public void parseSymbol(SymbolTable parent)
    {
        System.out.println("Node parseSymbol ERROR when parsing " + this.getType());
    }

    public void parseIR()
    {
        System.out.println("Node parseCode ERROR when parsing " + this.getType());
    }

}

