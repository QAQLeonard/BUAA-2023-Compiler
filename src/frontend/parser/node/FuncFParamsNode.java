package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 函数形参表 FuncFParams → FuncFParam { ',' FuncFParam }
 */
public class FuncFParamsNode extends Node {

    ArrayList<FuncFParamNode> funcFParamNodeList;
    ArrayList<Token> COMMATokenList;
    public FuncFParamsNode()
    {
        super(NodeType.FuncFParams);
        this.funcFParamNodeList = new ArrayList<>();
        this.COMMATokenList = new ArrayList<>();
    }

    @Override
    public void parseNode()
    {
        FuncFParamNode funcFParamNode = new FuncFParamNode();
        funcFParamNode.parseNode();
        this.funcFParamNodeList.add(funcFParamNode);
        while (Objects.requireNonNull(Parser.peekToken(0)).getType().equals(TokenType.COMMA))
        {
            this.COMMATokenList.add(Parser.getToken());
            funcFParamNode = new FuncFParamNode();
            funcFParamNode.parseNode();
            this.funcFParamNodeList.add(funcFParamNode);
        }
    }
}
