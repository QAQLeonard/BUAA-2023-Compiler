package frontend.parser.node;

/**
 * 语句块项 BlockItem → Decl | Stmt
 */
public class BlockItemNode extends Node {

    DeclNode declNode;
    StmtNode stmtNode;
    public BlockItemNode()
    {
        super(NodeType.BlockItem);
        this.declNode = null;
        this.stmtNode = null;
    }

    @Override
    public void parseNode()
    {

    }
}
