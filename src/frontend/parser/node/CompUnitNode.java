package frontend.parser.node;

import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 编译单元CompUnit → {Decl} {FuncDef} MainFuncDef
 */
public class CompUnitNode extends Node
{
    public ArrayList<DeclNode> declNodes;
    public ArrayList<FuncDefNode> funcDefNodes;
    public MainFuncDefNode mainFuncDefNode;
    public CompUnitNode()
    {
        super(NodeType.CompUnit);
        this.declNodes = new ArrayList<>();
        this.funcDefNodes = new ArrayList<>();
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
            this.declNodes.add(declNode);
        }

        // Parser.tokens.get(Parser.tokenIndex+1).getType()!=TokenType.MAINTK: 保证不是main函数
        while(Objects.requireNonNull(Parser.peekToken(1)).getType()!=TokenType.MAINTK)
        {
            FuncDefNode funcDefNode = new FuncDefNode();
            funcDefNode.parseNode();
            this.funcDefNodes.add(funcDefNode);
        }

        this.mainFuncDefNode = new MainFuncDefNode();
        this.mainFuncDefNode.parseNode();
    }

}
