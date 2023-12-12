package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.parser.ParserUtils;
import frontend.errorhandler.symbol.SymbolTable;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

import static ir.LLVMGenerator.*;
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
    @Override
    public void parseIR()
    {
        tmpValue = null;
        saveVal = null;
        this.addExpNode.parseIR();
    }

}
