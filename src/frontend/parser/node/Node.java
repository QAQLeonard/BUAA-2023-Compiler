package frontend.parser.node;

import java.io.File;
import java.io.IOException;

public class Node
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

