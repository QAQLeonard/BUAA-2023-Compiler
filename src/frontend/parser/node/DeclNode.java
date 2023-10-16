package frontend.parser.node;

import backend.errorhandler.CompilerError;
import frontend.parser.symbol.SymbolTable;
import frontend.lexer.TokenType;
import frontend.parser.Parser;

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

    @Override
    public void parseSymbol(SymbolTable st)
    {
        if(this.constDeclNode != null)
        {
            this.constDeclNode.parseSymbol(st);
        }
        else
        {
            this.varDeclNode.parseSymbol(st);
        }
    }

}
