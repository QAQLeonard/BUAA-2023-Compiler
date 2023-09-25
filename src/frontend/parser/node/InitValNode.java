package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

import java.util.ArrayList;
import java.util.Objects;

/**
 * InitVal → Exp <br>
 * | '{' [ InitVal { ',' InitVal } ] '}'
 */
public class InitValNode extends Node {
    ExpNode expNode;
    Token LBRACEToken;
    ArrayList<InitValNode> initValNodeList;
    ArrayList<Token> COMMATokenList;
    Token RBRACEToken;
    public InitValNode()
    {
        super(NodeType.InitVal);
        expNode = null;
        LBRACEToken = null;
        initValNodeList = new ArrayList<>();
        COMMATokenList = new ArrayList<>();
        RBRACEToken = null;
    }

    @Override
    public void parseNode()
    {
        if (Objects.requireNonNull(Parser.peekToken(0)).getType()== TokenType.LBRACE)
        {
            LBRACEToken = Parser.getToken();
            if (Objects.requireNonNull(Parser.peekToken(0)).getType()==TokenType.RBRACE)
            {
                RBRACEToken = Parser.getToken();
                return;
            }
            InitValNode initValNode = new InitValNode();
            initValNode.parseNode();
            this.initValNodeList.add(initValNode);
            while (Objects.requireNonNull(Parser.peekToken(0)).getType()==TokenType.COMMA)
            {
                COMMATokenList.add(Parser.getToken());
                initValNode = new InitValNode();
                initValNode.parseNode();
                this.initValNodeList.add(initValNode);
            }
            RBRACEToken = Parser.getToken();
        }
        else
        {
            this.expNode = new ExpNode();
            this.expNode.parseNode();
        }
    }
}
