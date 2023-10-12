package frontend.parser.node;

import backend.errorhandler.CompilerException;
import frontend.parser.symbol.FUNCSymbol;
import frontend.parser.symbol.Symbol;
import frontend.parser.symbol.SymbolTable;
import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 函数定义 FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
 */
public class FuncDefNode extends Node {
    FuncTypeNode funcTypeNode;
    Token IDENFRToken;
    Token LPARENTToken;
    Token RPARENTToken;
    FuncFParamsNode funcFParamsNode;
    BlockNode blockNode;
    public FuncDefNode()
    {
        super(NodeType.FuncDef);
        this.funcTypeNode = null;
        this.IDENFRToken = null;
        this.LPARENTToken = null;
        this.RPARENTToken = null;
        this.funcFParamsNode = null;
        this.blockNode = null;
    }

    @Override
    public void parseNode() throws CompilerException
    {
        this.funcTypeNode = new FuncTypeNode();
        this.funcTypeNode.parseNode();
        this.IDENFRToken = Parser.getToken(TokenType.IDENFR);
        this.LPARENTToken = Parser.getToken(TokenType.LPARENT);
        // not ()
        if(Objects.requireNonNull(Parser.peekToken(0)).getType() != TokenType.RPARENT)
        {
            this.funcFParamsNode = new FuncFParamsNode();
            this.funcFParamsNode.parseNode();
        }
        this.RPARENTToken = Parser.getToken(TokenType.RPARENT);
        this.blockNode = new BlockNode();
        this.blockNode.parseNode();
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        this.funcTypeNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, this.IDENFRToken.toString() + "\n", true);
        FileOperate.outputFileUsingUsingBuffer(destFile, this.LPARENTToken.toString() + "\n", true);
        if(this.funcFParamsNode != null)
        {
            this.funcFParamsNode.outputNode(destFile);
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, this.RPARENTToken.toString() + "\n", true);
        this.blockNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType())+"\n", true);
    }

    @Override
    public void parseSymbol(SymbolTable st) throws CompilerException
    {
        SymbolTable funcTable = new SymbolTable(st);
        funcFParamsNode.parseSymbol(funcTable);
        ArrayList<Symbol> paramList = new ArrayList<>(funcTable.getSymbolList());
        Symbol funcSymbol = new FUNCSymbol(this.IDENFRToken.getValue(), this.funcTypeNode.getType().toString(), paramList);
        st.addSymbol(funcSymbol);
        for (BlockItemNode blockItemNode : blockNode.blockItemNodeList)
        {
            blockItemNode.parseSymbol(funcTable);
        }
    }


}
