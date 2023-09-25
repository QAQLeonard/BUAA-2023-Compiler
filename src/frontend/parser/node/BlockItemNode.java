package frontend.parser.node;

import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

import java.util.Objects;

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
        if(Objects.requireNonNull(Parser.peekToken(0)).getType()== TokenType.CONSTTK || Objects.requireNonNull(Parser.peekToken(0)).getType()== TokenType.INTTK)
        {
            this.declNode = new DeclNode();
            this.declNode.parseNode();
        }
        else
        {
            this.stmtNode = new StmtNode();
            this.stmtNode.parseNode();
        }
    }
}
