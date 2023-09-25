package frontend.parser.node;

import frontend.lexer.token.IdentifierToken;
import frontend.lexer.token.Token;
import frontend.parser.Parser;

/**
 * MainFuncDef -> 'int' 'main' '(' ')' Block
 */

public class MainFuncDefNode extends Node {

    Token INTTKToken;
    Token MAINTKToken;
    Token LPARENTToken;
    Token RPARENTToken;
    BlockNode blockNode;
    public MainFuncDefNode()
    {
        super(NodeType.MainFuncDef);
        this.INTTKToken = null;
        this.MAINTKToken = null;
        this.LPARENTToken = null;
        this.RPARENTToken = null;
        this.blockNode = null;
    }

    @Override
    public void parseNode()
    {
        this.INTTKToken = Parser.getToken();
        this.MAINTKToken = Parser.getToken();
        this.LPARENTToken = Parser.getToken();
        this.RPARENTToken = Parser.getToken();
        this.blockNode = new BlockNode();
        this.blockNode.parseNode();
    }

}
