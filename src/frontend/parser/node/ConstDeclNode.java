package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

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
        this.CONSTTKToken = Parser.getToken();
        this.bTypeNode = new BTypeNode();
        this.bTypeNode.parseNode();
        ConstDefNode constDefNode = new ConstDefNode();
        constDefNode.parseNode();
        this.constDefNodeList.add(constDefNode);
        while(Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.COMMA)
        {
            this.COMMATokenList.add(Parser.getToken());
            constDefNode = new ConstDefNode();
            constDefNode.parseNode();
            this.constDefNodeList.add(constDefNode);
        }
        this.SEMICNToken = Parser.getToken();
    }
}
