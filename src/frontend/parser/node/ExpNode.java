package frontend.parser.node;

import backend.errorhandler.CompilerError;
import frontend.lexer.Token;
import frontend.parser.ParserUtils;
import frontend.parser.symbol.SymbolTable;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

/**
 * 表达式 Exp → AddExp
 */
public class ExpNode extends Node implements Expression
{

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

    @Override
    public void outputNode(File destFile) throws IOException
    {
        this.addExpNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public String toString()
    {
        return addExpNode.toString();
    }

    @Override
    public Token getOPToken()
    {
        return null;
    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        this.addExpNode.parseSymbol(st);
    }

    @Override
    public ExpType getExpType()
    {
        return this.addExpNode.getExpType();
    }
}
