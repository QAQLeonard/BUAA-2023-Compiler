package frontend.parser.node;

import backend.errorhandler.CompilerError;
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
 * 常数声明 ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
 */
public class ConstDeclNode extends Node {

    Token CONSTTKToken;
    BTypeNode bTypeNode;
    ArrayList<Token> COMMATokenList;
    ArrayList<ConstDefNode> constDefNodeList;
    Token SEMICNToken;

    public ConstDeclNode()
    {
        super(NodeType.ConstDecl);
        CONSTTKToken = null;
        bTypeNode = null;
        COMMATokenList = new ArrayList<>();
        constDefNodeList = new ArrayList<>();
        SEMICNToken = null;
    }

    @Override
    public void parseNode()
    {
        this.CONSTTKToken = Parser.getToken(TokenType.CONSTTK);
        this.bTypeNode = new BTypeNode();
        this.bTypeNode.parseNode();
        ConstDefNode constDefNode = new ConstDefNode();
        constDefNode.parseNode();
        this.constDefNodeList.add(constDefNode);
        while(Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.COMMA)
        {
            this.COMMATokenList.add(Parser.getToken(TokenType.COMMA));
            constDefNode = new ConstDefNode();
            constDefNode.parseNode();
            this.constDefNodeList.add(constDefNode);
        }
        this.SEMICNToken = Parser.getToken(TokenType.SEMICN);
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, this.CONSTTKToken.toString() + "\n", true);
        this.bTypeNode.outputNode(destFile);
        this.constDefNodeList.get(0).outputNode(destFile);
        for(int i = 0; i < this.COMMATokenList.size(); i++)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.COMMATokenList.get(i).toString() + "\n", true);
            this.constDefNodeList.get(i+1).outputNode(destFile);
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, this.SEMICNToken.toString() + "\n", true);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public void parseSymbol(SymbolTable st)
    {
        for(ConstDefNode constDefNode : this.constDefNodeList)
        {
            constDefNode.parseSymbol(st);
        }
    }
}
