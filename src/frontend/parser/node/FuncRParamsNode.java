package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 函数实参表 FuncRParams → Exp { ',' Exp }
 */
public class FuncRParamsNode extends Node {
    ArrayList<ExpNode> expNodeList;
    ArrayList<Token> COMMATokenList;
    public FuncRParamsNode()
    {
        super(NodeType.FuncRParams);
        this.expNodeList = new ArrayList<>();
        this.COMMATokenList = new ArrayList<>();
    }

    @Override
    public void parseNode()
    {
        ExpNode expNode = new ExpNode();
        expNode.parseNode();
        this.expNodeList.add(expNode);
        while (Objects.requireNonNull(Parser.peekToken(0)).getType()== TokenType.COMMA)
        {
            this.COMMATokenList.add(Parser.getToken());
            expNode = new ExpNode();
            expNode.parseNode();
            this.expNodeList.add(expNode);
        }
    }
}
