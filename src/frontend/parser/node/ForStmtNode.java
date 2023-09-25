package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.parser.Parser;

/**
 * 语句 ForStmt → LVal '=' Exp
 */
public class ForStmtNode extends Node
{

    LValNode lValNode;
    Token ASSIGNToken;
    ExpNode expNode;
    public ForStmtNode()
    {
        super(NodeType.ForStmt);
        lValNode = null;
        ASSIGNToken = null;
        expNode = null;
    }

    @Override
    public void parseNode()
    {
        this.lValNode = new LValNode();
        this.lValNode.parseNode();
        this.ASSIGNToken = Parser.getToken();
        this.expNode = new ExpNode();
        this.expNode.parseNode();
    }
}
