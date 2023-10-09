package frontend.parser.node;

import frontend.lexer.token.TokenType;

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

    public void parseNode()
    {

    }

    public NodeType getType()
    {
        return type;
    }

}

