package frontend.parser.node;

import frontend.lexer.token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * 声明 Decl → ConstDecl | VarDecl
 */
public class DeclNode extends Node
{

    public ConstDeclNode constDeclNode;
    public VarDeclNode varDeclNode;

    public DeclNode()
    {
        super(NodeType.Decl);
        constDeclNode = null;
        varDeclNode = null;
    }

    @Override
    public void parseNode()
    {
        if(Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.CONSTTK)
        {
            this.constDeclNode = new ConstDeclNode();
            this.constDeclNode.parseNode();
        }
        else
        {
            this.varDeclNode = new VarDeclNode();
            this.varDeclNode.parseNode();
        }
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        if(this.constDeclNode != null)
        {
            this.constDeclNode.outputNode(destFile);
        }
        else
        {
            this.varDeclNode.outputNode(destFile);
        }
    }

}
