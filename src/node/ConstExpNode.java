package node;

import token.Token;
import frontend.parser.ParserUtils;
import symbol.SymbolTable;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

/**
 * 常量表达式 ConstExp → AddExp
 */
public class ConstExpNode extends Node implements Expression
{

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

    @Override
    public void outputNode(File destFile) throws IOException
    {
        addExpNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
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
