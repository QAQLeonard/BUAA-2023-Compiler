package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
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
    public void parseNode()
    {
        this.IDENFERToken = Parser.getToken();
        while(Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.LBRACK)
        {
            parseArrayDimension(this.LBRACKTokenList, this.constExpNodeList, this.RBRACKTokenList);
        }
        if(Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.ASSIGN)
        {
            this.ASSIGNToken = Parser.getToken();
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
}
