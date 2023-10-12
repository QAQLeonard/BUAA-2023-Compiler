package frontend.parser.node;

import backend.errorhandler.CompilerException;
import frontend.parser.symbol.FUNCSymbol;
import frontend.parser.symbol.SymbolTable;
import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;

/**
 * MainFuncDef -> 'int' 'main' '(' ')' Block
 */

public class MainFuncDefNode extends Node
{

    Token INTTKToken;
    Token MAINTKToken;
    Token LPARENTToken;
    Token RPARENTToken;
    BlockNode blockNode;

    public MainFuncDefNode()
    {
        super(NodeType.MainFuncDef);
        this.INTTKToken = null;
        this.MAINTKToken = null;
        this.LPARENTToken = null;
        this.RPARENTToken = null;
        this.blockNode = null;
    }

    @Override
    public void parseNode() throws CompilerException
    {
        this.INTTKToken = Parser.getToken(TokenType.INTTK);
        this.MAINTKToken = Parser.getToken(TokenType.MAINTK);
        this.LPARENTToken = Parser.getToken(TokenType.LPARENT);
        this.RPARENTToken = Parser.getToken(TokenType.RPARENT);
        this.blockNode = new BlockNode();
        this.blockNode.parseNode();
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, this.INTTKToken.toString() + "\n", true);
        FileOperate.outputFileUsingUsingBuffer(destFile, this.MAINTKToken.toString() + "\n", true);
        FileOperate.outputFileUsingUsingBuffer(destFile, this.LPARENTToken.toString() + "\n", true);
        FileOperate.outputFileUsingUsingBuffer(destFile, this.RPARENTToken.toString() + "\n", true);
        this.blockNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public void parseSymbol(SymbolTable st) throws CompilerException
    {
        FUNCSymbol funcSymbol = new FUNCSymbol("main", "int", null);
        st.addSymbol(funcSymbol);
        this.blockNode.parseSymbol(st);
    }

}
