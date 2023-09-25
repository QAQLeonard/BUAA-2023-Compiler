package frontend.parser.node;

/**
 * 表达式 Exp → AddExp
 */
public class ExpNode extends Node {

    AddExpNode addExpNode;
    public ExpNode()
    {
        super(NodeType.Exp);
    }

    @Override
    public void parseNode()
    {
        this.addExpNode = new AddExpNode();
        this.addExpNode.parseNode();
    }
}
