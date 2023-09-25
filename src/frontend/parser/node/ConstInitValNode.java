package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 常量初值 ConstInitVal → ConstExp<br>
 * | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
 */
public class ConstInitValNode extends Node {
    ConstExpNode constExpNode;
    Token LBRACEToken;
    ArrayList<ConstInitValNode> constInitValNodeList;
    ArrayList<Token> COMMATokenList;
    Token RBRACEToken;
    public ConstInitValNode()
    {
        super(NodeType.ConstInitVal);
    }

    @Override
    public void parseNode()
    {
        if (Objects.requireNonNull(Parser.peekToken(0)).getType()==TokenType.LBRACE)
        {
            LBRACEToken = Parser.getToken();
            if (Objects.requireNonNull(Parser.peekToken(0)).getType()==TokenType.RBRACE)
            {
                RBRACEToken = Parser.getToken();
                return;
            }
            this.constInitValNodeList = new ArrayList<>();
            ConstInitValNode constInitValNode = new ConstInitValNode();
            constInitValNode.parseNode();
            this.constInitValNodeList.add(constInitValNode);
            while (Objects.requireNonNull(Parser.peekToken(0)).getType()==TokenType.COMMA)
            {
                COMMATokenList.add(Parser.getToken());
                constInitValNode = new ConstInitValNode();
                constInitValNode.parseNode();
                this.constInitValNodeList.add(constInitValNode);
            }
            RBRACEToken = Parser.getToken();
        }
        else
        {
            this.constExpNode = new ConstExpNode();
            this.constExpNode.parseNode();
        }
    }
}
