package frontend.parser.node;

import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 编译单元 CompUnit → {Decl} {FuncDef} MainFuncDef
 */
public class CompUnitNode extends Node
{
    public ArrayList<DeclNode> declNodeList;
    public ArrayList<FuncDefNode> funcDefNodeList;
    public MainFuncDefNode mainFuncDefNode;
    public CompUnitNode()
    {
        super(NodeType.CompUnit);
        this.declNodeList = new ArrayList<>();
        this.funcDefNodeList = new ArrayList<>();
        this.mainFuncDefNode = null;
    }

    @Override
    public void parseNode()
    {
        // Parser.tokens.get(Parser.tokenIndex+2).getType()!=TokenType.LPARENT: 保证不是函数
        while(Objects.requireNonNull(Parser.peekToken(2)).getType()!=TokenType.LPARENT)
        {
            DeclNode declNode = new DeclNode();
            declNode.parseNode();
            this.declNodeList.add(declNode);
        }

        // Parser.tokens.get(Parser.tokenIndex+1).getType()!=TokenType.MAINTK: 保证不是main函数
        while(Objects.requireNonNull(Parser.peekToken(1)).getType()!=TokenType.MAINTK)
        {
            FuncDefNode funcDefNode = new FuncDefNode();
            funcDefNode.parseNode();
            this.funcDefNodeList.add(funcDefNode);
        }

        this.mainFuncDefNode = new MainFuncDefNode();
        this.mainFuncDefNode.parseNode();
    }

}
