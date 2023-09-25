package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 函数形参 FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
 */
public class FuncFParamNode extends Node
{
    BTypeNode bTypeNode;
    Token IDENFRToken;
    ArrayList<Token> LBRACKTokenList;
    ArrayList<Token> RBRACKTokenList;
    ArrayList<ConstExpNode> constExpNodeList;

    public FuncFParamNode()
    {
        super(NodeType.FuncFParam);
        this.bTypeNode = null;
        this.IDENFRToken = null;
        this.LBRACKTokenList = new ArrayList<>();
        this.RBRACKTokenList = new ArrayList<>();
        this.constExpNodeList = new ArrayList<>();
    }

    @Override
    public void parseNode()
    {
        this.bTypeNode = new BTypeNode();
        this.bTypeNode.parseNode();
        this.IDENFRToken = Parser.getToken();
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.LBRACK)
        {
            this.LBRACKTokenList.add(Parser.getToken());
            this.RBRACKTokenList.add(Parser.getToken());

            while (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.LBRACK)
            {
                this.LBRACKTokenList.add(Parser.getToken());
                ConstExpNode constExpNode = new ConstExpNode();
                constExpNode.parseNode();
                this.constExpNodeList.add(constExpNode);
                this.RBRACKTokenList.add(Parser.getToken());
            }
        }
    }
}
