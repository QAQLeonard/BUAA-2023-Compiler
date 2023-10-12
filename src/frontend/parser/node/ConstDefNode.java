package frontend.parser.node;

import backend.errorhandler.CompilerException;
import frontend.parser.symbol.ARRAYSymbol;
import frontend.parser.symbol.INTSymbol;
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

import static frontend.parser.ParserUtils.parseArrayDimension;

/**
 * 常数定义 ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
 */
public class ConstDefNode extends Node
{

    Token IDENFRToken;
    ArrayList<Token> LBRACKTokenList;
    ArrayList<ConstExpNode> constExpNodeList;
    ArrayList<Token> RBRACKTokenList;
    Token ASSIGNToken;
    ConstInitValNode constInitValNode;

    public ConstDefNode()
    {
        super(NodeType.ConstDef);
        this.IDENFRToken = null;
        this.LBRACKTokenList = new ArrayList<>();
        this.constExpNodeList = new ArrayList<>();
        this.RBRACKTokenList = new ArrayList<>();
        this.ASSIGNToken = null;
        this.constInitValNode = null;
    }

    @Override
    public void parseNode() throws CompilerException
    {
        this.IDENFRToken = Parser.getToken(TokenType.IDENFR);
        while(Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.LBRACK)
        {
            parseArrayDimension(this.LBRACKTokenList, this.constExpNodeList, this.RBRACKTokenList);
        }
        this.ASSIGNToken = Parser.getToken(TokenType.ASSIGN);
        this.constInitValNode = new ConstInitValNode();
        this.constInitValNode.parseNode();
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, this.IDENFRToken.toString() + "\n", true);
        for(int i = 0; i < this.LBRACKTokenList.size(); i++)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.LBRACKTokenList.get(i).toString() + "\n", true);
            this.constExpNodeList.get(i).outputNode(destFile);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.RBRACKTokenList.get(i).toString() + "\n", true);
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, this.ASSIGNToken.toString() + "\n", true);
        this.constInitValNode.outputNode(destFile);
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);
    }

    @Override
    public void parseSymbol(SymbolTable st) throws CompilerException
    {
        if(LBRACKTokenList.isEmpty())
        {
            INTSymbol intSymbol = new INTSymbol(IDENFRToken.getValue(),true, true);
            st.addSymbol(intSymbol);
        }
        else
        {
            ARRAYSymbol arraySymbol = new ARRAYSymbol(IDENFRToken.getValue(),LBRACKTokenList.size(),true, true);
        }
    }
}
