package frontend.parser.node;

import frontend.errorhandler.symbol.SymbolTable;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 编译单元 CompUnit → {Decl} {FuncDef} MainFuncDef
 */
public class CompUnitNode extends Node
{
    public ArrayList<DeclNode> declNodeList;
    public ArrayList<FuncDefNode> funcDefNodeList;
    public MainFuncDefNode mainFuncDefNode;
    private static CompUnitNode instance;

    private CompUnitNode()
    {
        super(NodeType.CompUnit);
        this.declNodeList = new ArrayList<>();
        this.funcDefNodeList = new ArrayList<>();
        this.mainFuncDefNode = null;
    }

    public static CompUnitNode getInstance()
    {
        if (instance == null)
        {
            instance = new CompUnitNode();
        }
        return instance;
    }

    @Override
    public void parseNode()
    {
        // Parser.tokens.get(Parser.tokenIndex+2).getType()!=TokenType.LPARENT: 保证不是函数
        while (Objects.requireNonNull(Parser.peekToken(2)).getType() != TokenType.LPARENT)
        {
            DeclNode declNode = new DeclNode();
            declNode.parseNode();
            this.declNodeList.add(declNode);
        }
        // System.out.println(this.declNodeList.size());

        // Parser.tokens.get(Parser.tokenIndex+1).getType()!=TokenType.MAINTK: 保证不是main函数
        while (Objects.requireNonNull(Parser.peekToken(1)).getType() != TokenType.MAINTK)
        {
            FuncDefNode funcDefNode = new FuncDefNode();
            funcDefNode.parseNode();
            this.funcDefNodeList.add(funcDefNode);

        }

        this.mainFuncDefNode = new MainFuncDefNode();
        this.mainFuncDefNode.parseNode();
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        for (DeclNode declNode : this.declNodeList)
        {
            declNode.outputNode(destFile);
        }
        for (FuncDefNode funcDefNode : this.funcDefNodeList)
        {
            funcDefNode.outputNode(destFile);
        }
        this.mainFuncDefNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        for (DeclNode declNode : this.declNodeList)
        {
            declNode.parseSymbol(st);
        }
        for (FuncDefNode funcDefNode : this.funcDefNodeList)
        {
            funcDefNode.parseSymbol(st);
        }
        this.mainFuncDefNode.parseSymbol(st);

    }

    @Override
    public void parseIR()
    {
        for(DeclNode declNode : this.declNodeList)
        {
            declNode.parseIR();
        }
        for(FuncDefNode funcDefNode : this.funcDefNodeList)
        {
            funcDefNode.parseIR();
        }
        this.mainFuncDefNode.parseIR();
    }
}
