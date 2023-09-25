package frontend.parser.node;

/**
 * 条件表达式 Cond → LOrExp
 */
public class CondNode extends Node {

    LOrExpNode lOrExpNode;
    public CondNode()
    {
        super(NodeType.Cond);
    }

    @Override
    public void parseNode()
    {
        this.lOrExpNode = new LOrExpNode();
        this.lOrExpNode.parseNode();
    }
}
