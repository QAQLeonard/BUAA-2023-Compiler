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

/**
 * 变量声明 VarDecl → BType VarDef { ',' VarDef } ';'
 */
public class VarDeclNode extends Node
{

    BTypeNode bTypeNode;
    ArrayList<VarDefNode> varDefNodeList;
    ArrayList<Token> COMMATokenList;
    Token SEMICNToken;

    public VarDeclNode()
    {
        super(NodeType.VarDecl);
        bTypeNode = null;
        varDefNodeList = new ArrayList<>();
        COMMATokenList = new ArrayList<>();
        SEMICNToken = null;
    }

    @Override
    public void parseNode()
    {
        this.bTypeNode = new BTypeNode();
        this.bTypeNode.parseNode();
        VarDefNode varDefNode = new VarDefNode();
        varDefNode.parseNode();
        this.varDefNodeList.add(varDefNode);
        while (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.COMMA)
        {
            this.COMMATokenList.add(Parser.getToken());
            varDefNode = new VarDefNode();
            varDefNode.parseNode();
            this.varDefNodeList.add(varDefNode);
        }
        this.SEMICNToken = Parser.getToken();
    }
}
