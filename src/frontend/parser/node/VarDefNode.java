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
 * 变量定义 VarDef → Ident { '[' ConstExp ']' }<br>
 * | Ident { '[' ConstExp ']' } '=' InitVal
 */
public class VarDefNode extends Node {

    Token IDENFERToken;
    ArrayList<Token> LBRACKTokenList;
    ArrayList<ConstExpNode> constExpNodeList;
    ArrayList<Token> RBRACKTokenList;
    Token ASSIGNToken;
    InitValNode initValNode;
    public VarDefNode()
    {
        super(NodeType.VarDef);
        this.IDENFERToken = null;
        this.LBRACKTokenList = new ArrayList<>();
        this.constExpNodeList = new ArrayList<>();
        this.RBRACKTokenList = new ArrayList<>();
        this.ASSIGNToken = null;
        this.initValNode = null;
    }

    @Override
    public void parseNode() throws CompilerException
    {
        this.IDENFERToken = Parser.getToken(TokenType.IDENFR);
        while(Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.LBRACK)
        {
            parseArrayDimension(this.LBRACKTokenList, this.constExpNodeList, this.RBRACKTokenList);
        }
        if(Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.ASSIGN)
        {
            this.ASSIGNToken = Parser.getToken(TokenType.ASSIGN);
            this.initValNode = new InitValNode();
            this.initValNode.parseNode();
        }
    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        FileOperate.outputFileUsingUsingBuffer(destFile, this.IDENFERToken.toString() + "\n", true);
        for(int i = 0; i < this.LBRACKTokenList.size(); i++)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.LBRACKTokenList.get(i).toString() + "\n", true);
            this.constExpNodeList.get(i).outputNode(destFile);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.RBRACKTokenList.get(i).toString() + "\n", true);
        }
        if(this.ASSIGNToken != null)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.ASSIGNToken.toString() + "\n", true);
            this.initValNode.outputNode(destFile);
        }
        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);

    }

    @Override
    public void parseSymbol(SymbolTable st) throws CompilerException
    {
        if(this.LBRACKTokenList.isEmpty())
        {
            st.addSymbol(new INTSymbol(this.IDENFERToken.getValue(), false, this.ASSIGNToken != null));
        }
        else {
            int dimension = this.LBRACKTokenList.size();
            st.addSymbol(new ARRAYSymbol(this.IDENFERToken.getValue(), dimension, false, this.ASSIGNToken != null));
        }
    }
}
