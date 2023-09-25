package frontend.parser.node;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

/**
 * 语句 Stmt → LVal '=' Exp ';'
 * | [Exp] ';' //有无Exp两种情况
 * | Block
 * | 'if' '(' Cond ')' Stmt [ 'else' Stmt ] // 1.有else 2.无else
 * | 'for' '(' [ForStmt] ';' [Cond] ';' [forStmt] ')' Stmt
 * | 'break' ';' | 'continue' ';'
 * | 'return' [Exp] ';' // 1.有Exp 2.无Exp
 * | LVal '=' 'getint''('')'';'
 * | 'printf''('FormatString{','Exp}')'';' // 1.有Exp 2.无Exp
 */
public class StmtNode extends Node
{
    public enum StmtType
    {
        ASSIGNMENT, // LVal '=' Exp ';'
        EXPRESSION, // [Exp] ';'
        BLOCK,  // Block
        IF, // 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
        FOR, // 'for' '(' [ForStmt] ';' [Cond] ';' [forStmt] ')' Stmt
        BREAK,  // 'break' ';'
        CONTINUE,   // 'continue' ';'
        RETURN, // 'return' [Exp] ';'
        GETINT, // LVal '=' 'getint''('')'';'
        PRINTF, // 'printf''('FormatString{','Exp}')'';'
    }

    LValNode lValNode;
    Token ASSIGNToken;
    ExpNode expNode;
    ArrayList<Token> SEMICNTokenList;
    BlockNode blockNode;
    Token IFTKToken;
    Token LPARENTToken;
    CondNode condNode;
    Token RPARENTToken;
    ArrayList<StmtNode> stmtNodeList;
    Token ELSETKToken;
    Token FORTKToken;
    ArrayList<ForStmtNode> forStmtNodeList;
    Token BREAKTKToken;
    Token CONTINUETKToken;
    Token RETURNTKToken;
    Token GETINTTKToken;
    Token PRINTFTKToken;
    Token STRCONToken;  // FormatString
    StmtType stmtType;

    public StmtNode()
    {
        super(NodeType.Stmt);
        this.lValNode = null;
        this.ASSIGNToken = null;
        this.expNode = null;
        this.SEMICNTokenList = new ArrayList<>();
        this.blockNode = null;
        this.IFTKToken = null;
        this.LPARENTToken = null;
        this.condNode = null;
        this.RPARENTToken = null;
        this.stmtNodeList = new ArrayList<>();
        this.ELSETKToken = null;
        this.FORTKToken = null;
        this.forStmtNodeList = new ArrayList<>();
        this.BREAKTKToken = null;
        this.CONTINUETKToken = null;
        this.RETURNTKToken = null;
        this.GETINTTKToken = null;
        this.PRINTFTKToken = null;
        this.STRCONToken = null;
        this.stmtType = null;
    }

    @Override
    public void parseNode()
    {
        // Stmt →  Block → '{' { BlockItem } '}'
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.LBRACE)
        {
            this.blockNode = new BlockNode();
            this.blockNode.parseNode();
            this.stmtType = StmtType.BLOCK;
            return;
        }

        // Stmt → 'if' '(' Cond ')' Stmt [ 'else' Stmt ]
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.IFTK)
        {
            this.IFTKToken = Parser.getToken();
            this.LPARENTToken = Parser.getToken();
            this.condNode = new CondNode();
            this.condNode.parseNode();
            this.RPARENTToken = Parser.getToken();
            StmtNode stmtNode1 = new StmtNode();
            stmtNode1.parseNode();
            this.stmtNodeList.add(stmtNode1);
            if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.ELSETK)
            {
                this.ELSETKToken = Parser.getToken();
                StmtNode stmtNode2 = new StmtNode();
                stmtNode2.parseNode();
                this.stmtNodeList.add(stmtNode2);
            }
            this.stmtType = StmtType.IF;
            return;
        }

        // Stmt → 'for' '(' [ForStmt] ';' [Cond] ';' [forStmt] ')' Stmt
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.FORTK)
        {
            this.FORTKToken = Parser.getToken();
            this.LPARENTToken = Parser.getToken();
            if (Objects.requireNonNull(Parser.peekToken(0)).getType() != TokenType.SEMICN)
            {
                ForStmtNode forStmtNode1 = new ForStmtNode();
                forStmtNode1.parseNode();
                this.forStmtNodeList.add(forStmtNode1);
            }
            this.SEMICNTokenList.add(Parser.getToken());
            if (Objects.requireNonNull(Parser.peekToken(0)).getType() != TokenType.SEMICN)
            {
                this.condNode = new CondNode();
                this.condNode.parseNode();
            }
            this.SEMICNTokenList.add(Parser.getToken());
            if (Objects.requireNonNull(Parser.peekToken(0)).getType() != TokenType.RPARENT)
            {
                ForStmtNode forStmtNode2 = new ForStmtNode();
                forStmtNode2.parseNode();
                this.forStmtNodeList.add(forStmtNode2);
            }
            this.RPARENTToken = Parser.getToken();
            StmtNode stmtNode = new StmtNode();
            stmtNode.parseNode();
            this.stmtNodeList.add(stmtNode);
            this.stmtType = StmtType.FOR;
            return;
        }

        // Stmt → 'break' ';'
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.BREAKTK)
        {
            this.BREAKTKToken = Parser.getToken();
            this.SEMICNTokenList.add(Parser.getToken());
            this.stmtType = StmtType.BREAK;
            return;
        }

        // Stmt → 'continue' ';'
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.CONTINUETK)
        {
            this.CONTINUETKToken = Parser.getToken();
            this.SEMICNTokenList.add(Parser.getToken());
            this.stmtType = StmtType.CONTINUE;
            return;
        }

        // Stmt → 'return' [Exp] ';'
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.RETURNTK)
        {
            this.RETURNTKToken = Parser.getToken();
            if (Objects.requireNonNull(Parser.peekToken(0)).getType() != TokenType.SEMICN)
            {
                this.expNode = new ExpNode();
                this.expNode.parseNode();
            }
            this.SEMICNTokenList.add(Parser.getToken());
            this.stmtType = StmtType.RETURN;
            return;
        }


        // Stmt → 'printf''('FormatString{','Exp}')'';'
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.PRINTFTK)
        {
            this.PRINTFTKToken = Parser.getToken();
            this.LPARENTToken = Parser.getToken();
            this.STRCONToken = Parser.getToken();
            while (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.COMMA)
            {
                this.SEMICNTokenList.add(Parser.getToken());
                this.expNode = new ExpNode();
                this.expNode.parseNode();
            }
            this.RPARENTToken = Parser.getToken();
            this.SEMICNTokenList.add(Parser.getToken());
            this.stmtType = StmtType.PRINTF;
            return;
        }

        // Stmt → LVal '=' Exp ';'
        // Stmt → LVal '=' 'getint''('')'';'
        // Stmt → [Exp] ';'
        Token temp = ParserUtils.findNearestTokenByType(TokenType.ASSIGN);
        if (temp == null || temp.getLineNumber() != Objects.requireNonNull(Parser.peekToken(0)).getLineNumber())    //no '='
        {
            // [Exp] ';'
            if (Objects.requireNonNull(Parser.peekToken(0)).getType() != TokenType.SEMICN)
            {
                this.expNode = new ExpNode();
                this.expNode.parseNode();
            }
            this.SEMICNTokenList.add(Parser.getToken());
            this.stmtType = StmtType.EXPRESSION;
            return;
        }
        //  LVal '=' Exp ';'
        //  LVal '=' 'getint''('')'';'

        //LVal '='
        this.lValNode = new LValNode();
        this.lValNode.parseNode();
        this.ASSIGNToken = Parser.getToken();
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.GETINTTK)  // 'getint''('')'';'
        {
            this.GETINTTKToken = Parser.getToken();
            this.LPARENTToken = Parser.getToken();
            this.RPARENTToken = Parser.getToken();
            this.SEMICNTokenList.add(Parser.getToken());
            this.stmtType = StmtType.GETINT;
        }
        else    //Exp ';'
        {
            this.expNode = new ExpNode();
            this.expNode.parseNode();
            this.SEMICNTokenList.add(Parser.getToken());
            this.stmtType = StmtType.ASSIGNMENT;
        }
        return;


    }
}
