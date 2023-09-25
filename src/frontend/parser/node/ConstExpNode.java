package frontend.parser.node;

/**
 * 常量表达式 ConstExp → AddExp
 */
public class ConstExpNode extends Node {

    AddExpNode addExpNode;
    public ConstExpNode()
    {
        super(NodeType.ConstExp);
    }

    @Override
    public void parseNode()
    {
        addExpNode = new AddExpNode();
        addExpNode.parseNode();
    }
}
