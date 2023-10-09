package frontend.parser.node;

import backend.errorhandler.CompilerException;
import frontend.lexer.TokenType;
import frontend.parser.Parser;

import java.io.File;
import java.io.IOException;
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
    public void parseNode() throws CompilerException
    {
        if(Objects.requireNonNull(Parser.peekToken(0)).getType()== TokenType.CONSTTK || Objects.requireNonNull(Parser.peekToken(0)).getType()== TokenType.INTTK)
        {
            // System.out.println("BlockItemNode Decl");
            this.declNode = new DeclNode();
            this.declNode.parseNode();
        }
        else
        {
            this.stmtNode = new StmtNode();
            this.stmtNode.parseNode();
        }
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        if(this.declNode != null)
        {
            this.declNode.outputNode(destFile);
        }
        else
        {
            this.stmtNode.outputNode(destFile);
        }
    }
}
