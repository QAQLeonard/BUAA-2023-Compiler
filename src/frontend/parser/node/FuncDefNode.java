package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

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
    public void parseNode()
    {
        this.funcTypeNode = new FuncTypeNode();
        this.funcTypeNode.parseNode();
        this.IDENFRToken = Parser.getToken();
        this.LPARENTToken = Parser.getToken();
        if(Objects.requireNonNull(Parser.peekToken(0)).getType() != TokenType.RPARENT)
        {
            this.funcFParamsNode = new FuncFParamsNode();
            this.funcFParamsNode.parseNode();
        }
        this.RPARENTToken = Parser.getToken();
        this.blockNode = new BlockNode();
        this.blockNode.parseNode();
    }

}
