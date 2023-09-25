package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

import java.util.ArrayList;
import java.util.Objects;

import static frontend.parser.node.NodeUtil.parseArrayDimension;

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
}
