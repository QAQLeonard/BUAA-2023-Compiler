package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;

import java.util.Objects;

/**
 * 一元表达式 UnaryExp → PrimaryExp <br>
 * | Ident '(' [FuncRParams] ')'<br>
 * | UnaryOp UnaryExp<br>
 */
public class UnaryExpNode extends Node {

    PrimaryExpNode primaryExpNode;
    Token IDENFRToken;
    Token LPARENTToken;
    FuncRParamsNode funcRParamsNode;
    Token RPARENTToken;
    UnaryOpNode unaryOpNode;
    UnaryExpNode unaryExpNode;
    public UnaryExpNode()
    {
        super(NodeType.UnaryExp);
    }

    @Override
    public void parseNode()
    {
        // UnaryExp → UnaryOp UnaryExp
        if (NodeUtil.UnaryOpTokenTypes.contains(Objects.requireNonNull(Parser.peekToken(0)).getType()))
        {
            this.unaryOpNode = new UnaryOpNode();
            this.unaryOpNode.parseNode();
            this.unaryExpNode = new UnaryExpNode();
            this.unaryExpNode.parseNode();
        }
        // UnaryExp → Ident '(' [FuncRParams] ')'
        else if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.IDENFR && Objects.requireNonNull(Parser.peekToken(1)).getType() == TokenType.LPARENT)
        {
            this.IDENFRToken = Parser.getToken();
            this.LPARENTToken = Parser.getToken();
            if (Objects.requireNonNull(Parser.peekToken(0)).getType() != TokenType.RPARENT)
            {
                this.funcRParamsNode = new FuncRParamsNode();
                this.funcRParamsNode.parseNode();
            }
            this.RPARENTToken = Parser.getToken();
        }
        // UnaryExp → PrimaryExp
        else
        {
            this.primaryExpNode = new PrimaryExpNode(NodeType.PrimaryExp);
            this.primaryExpNode.parseNode();
        }
    }
}
