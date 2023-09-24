package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.parser.Parser;

/**
 * ConstDecl → 'const' 'int' ConstDef ';'
 */
public class ConstDeclNode extends Node {

    Token CONSTTKToken;
    Token INTTKToken;
    ConstDefNode constDefNode;
    Token SEMICNToken;

    public ConstDeclNode()
    {
        super(NodeType.ConstDecl);
    }

    @Override
    public void parseNode()
    {
        this.CONSTTKToken = Parser.getToken();
        this.INTTKToken = Parser.getToken();
        this.constDefNode = new ConstDefNode();
        this.constDefNode.parseNode();
        this.SEMICNToken = Parser.getToken();
    }
}
