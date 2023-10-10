package frontend.parser.node;

import backend.errorhandler.CompilerException;
import backend.errorhandler.symbol.SymbolTable;

import java.io.File;
import java.io.IOException;

public abstract class Node
{
    private String value;
    private NodeType type;

    public Node(NodeType type)
    {
        this.type = type;
    }

    public void outputNode(File destFile) throws IOException
    {

    }

    public void parseNode()  throws CompilerException
    {

    }

    public NodeType getType()
    {
        return type;
    }

    public void parseSymbol(SymbolTable parent)
    {

    }

}

