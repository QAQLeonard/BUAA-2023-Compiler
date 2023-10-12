package frontend.parser.node;

import backend.errorhandler.CompilerException;
import backend.errorhandler.ExceptionType;
import frontend.parser.symbol.SymbolTable;
import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.Parser;
import frontend.parser.ParserUtils;
import utils.FileOperate;

import java.io.File;
import java.io.IOException;
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
    ArrayList<ExpNode> expNodeList;
    ArrayList<Token> SEMICNTokenList;
    BlockNode blockNode;
    Token IFTKToken;
    Token LPARENTToken;
    CondNode condNode;
    Token RPARENTToken;
    ArrayList<StmtNode> stmtNodeList;
    Token ELSETKToken;
    Token FORTKToken;
    ForStmtNode forStmtNode1, forStmtNode2;
    Token BREAKTKToken;
    Token CONTINUETKToken;
    Token RETURNTKToken;
    Token GETINTTKToken;
    Token PRINTFTKToken;
    ArrayList<Token> COMMATokenList;
    Token STRCONToken;  // FormatString
    StmtType stmtType;


    public StmtNode()
    {
        super(NodeType.Stmt);
        this.lValNode = null;
        this.ASSIGNToken = null;
        this.expNodeList = new ArrayList<>();
        this.SEMICNTokenList = new ArrayList<>();
        this.blockNode = null;
        this.IFTKToken = null;
        this.LPARENTToken = null;
        this.condNode = null;
        this.RPARENTToken = null;
        this.stmtNodeList = new ArrayList<>();
        this.ELSETKToken = null;
        this.FORTKToken = null;
        this.forStmtNode1 = null;
        this.forStmtNode2 = null;
        this.BREAKTKToken = null;
        this.CONTINUETKToken = null;
        this.RETURNTKToken = null;
        this.GETINTTKToken = null;
        this.PRINTFTKToken = null;
        this.COMMATokenList = new ArrayList<>();
        this.STRCONToken = null;
        this.stmtType = null;
    }

    @Override
    public void parseNode() throws CompilerException
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
            this.IFTKToken = Parser.getToken(TokenType.IFTK);
            this.LPARENTToken = Parser.getToken(TokenType.LPARENT);
            this.condNode = new CondNode();
            this.condNode.parseNode();
            this.RPARENTToken = Parser.getToken(TokenType.RPARENT);
            StmtNode stmtNode1 = new StmtNode();
//            stmtNode1.withinForLoop = this.withinForLoop;
            stmtNode1.parseNode();
            this.stmtNodeList.add(stmtNode1);
            if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.ELSETK)
            {
                this.ELSETKToken = Parser.getToken(TokenType.ELSETK);
                StmtNode stmtNode2 = new StmtNode();
//                stmtNode2.withinForLoop = this.withinForLoop;
                stmtNode2.parseNode();
                this.stmtNodeList.add(stmtNode2);
            }
            this.stmtType = StmtType.IF;
            return;
        }

        // Stmt → 'for' '(' [ForStmt] ';' [Cond] ';' [forStmt] ')' Stmt
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.FORTK)
        {
            this.FORTKToken = Parser.getToken(TokenType.FORTK);
            this.LPARENTToken = Parser.getToken(TokenType.LPARENT);
            if (Objects.requireNonNull(Parser.peekToken(0)).getType() != TokenType.SEMICN)
            {
                this.forStmtNode1 = new ForStmtNode();
                forStmtNode1.parseNode();

            }
            this.SEMICNTokenList.add(Parser.getToken(TokenType.SEMICN));
            if (Objects.requireNonNull(Parser.peekToken(0)).getType() != TokenType.SEMICN)
            {
                this.condNode = new CondNode();
                this.condNode.parseNode();
            }
            this.SEMICNTokenList.add(Parser.getToken(TokenType.SEMICN));
            if (Objects.requireNonNull(Parser.peekToken(0)).getType() != TokenType.RPARENT)
            {
                this.forStmtNode2 = new ForStmtNode();
                forStmtNode2.parseNode();
            }
            this.RPARENTToken = Parser.getToken(TokenType.RPARENT);
            ParserUtils.ForLoopCount++;
            StmtNode stmtNode = new StmtNode();
//            stmtNode.withinForLoop = true;
            stmtNode.parseNode();
            this.stmtNodeList.add(stmtNode);
            this.stmtType = StmtType.FOR;
            ParserUtils.ForLoopCount--;
            return;
        }

        // Stmt → 'break' ';'
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.BREAKTK)
        {
            if(ParserUtils.ForLoopCount == 0)
            {
                throw new CompilerException(ExceptionType.m, "break can only be used in for loop", Objects.requireNonNull(Parser.peekToken(0)).getLineNumber());
            }
            this.BREAKTKToken = Parser.getToken(TokenType.BREAKTK);
            this.SEMICNTokenList.add(Parser.getToken(TokenType.SEMICN));
            this.stmtType = StmtType.BREAK;
            return;
        }

        // Stmt → 'continue' ';'
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.CONTINUETK)
        {
            if(ParserUtils.ForLoopCount == 0)
            {
                throw new CompilerException(ExceptionType.m, "continue can only be used in for loop", Objects.requireNonNull(Parser.peekToken(0)).getLineNumber());
            }
            this.CONTINUETKToken = Parser.getToken(TokenType.CONTINUETK);
            this.SEMICNTokenList.add(Parser.getToken(TokenType.SEMICN));
            this.stmtType = StmtType.CONTINUE;
            return;
        }

        // Stmt → 'return' [Exp] ';'
        if (Objects.requireNonNull(Parser.peekToken(0)).getType() == TokenType.RETURNTK)
        {
            this.RETURNTKToken = Parser.getToken(TokenType.RETURNTK);
            if (Objects.requireNonNull(Parser.peekToken(0)).getType() != TokenType.SEMICN)
            {
                ExpNode expNode = new ExpNode();
                expNode.parseNode();
                this.expNodeList.add(expNode);
            }
            this.SEMICNTokenList.add(Parser.getToken(TokenType.SEMICN));
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
                this.COMMATokenList.add(Parser.getToken());
                ExpNode expNode = new ExpNode();
                expNode.parseNode();
                this.expNodeList.add(expNode);
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
            // System.out.println(temp.getLineNumber());
            // [Exp] ';'
            if (Objects.requireNonNull(Parser.peekToken(0)).getType() != TokenType.SEMICN)
            {
                ExpNode expNode = new ExpNode();
                expNode.parseNode();
                this.expNodeList.add(expNode);
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
            ExpNode expNode = new ExpNode();
            expNode.parseNode();
            this.expNodeList.add(expNode);
            this.SEMICNTokenList.add(Parser.getToken());
            this.stmtType = StmtType.ASSIGNMENT;
        }
        return;


    }

    @Override
    public void outputNode(File destFile) throws IOException
    {
        if (this.stmtType == StmtType.ASSIGNMENT)
        {
            this.lValNode.outputNode(destFile);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.ASSIGNToken.toString() + "\n", true);
            this.expNodeList.get(0).outputNode(destFile);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.SEMICNTokenList.get(0).toString() + "\n", true);
        }
        else if (this.stmtType == StmtType.EXPRESSION)
        {
            if (this.expNodeList.size() != 0)
            {
                this.expNodeList.get(0).outputNode(destFile);
            }
            FileOperate.outputFileUsingUsingBuffer(destFile, this.SEMICNTokenList.get(0).toString() + "\n", true);
        }

        else if (this.stmtType == StmtType.BLOCK)
        {
            this.blockNode.outputNode(destFile);
        }
        else if (this.stmtType == StmtType.IF)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.IFTKToken.toString() + "\n", true);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.LPARENTToken.toString() + "\n", true);
            this.condNode.outputNode(destFile);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.RPARENTToken.toString() + "\n", true);
            this.stmtNodeList.get(0).outputNode(destFile);
            if (this.ELSETKToken != null)
            {
                FileOperate.outputFileUsingUsingBuffer(destFile, this.ELSETKToken.toString() + "\n", true);
                this.stmtNodeList.get(1).outputNode(destFile);
            }
        }
        else if (this.stmtType == StmtType.FOR)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.FORTKToken.toString() + "\n", true);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.LPARENTToken.toString() + "\n", true);
            if (this.forStmtNode1 != null)
            {
                this.forStmtNode1.outputNode(destFile);
            }
            FileOperate.outputFileUsingUsingBuffer(destFile, this.SEMICNTokenList.get(0).toString() + "\n", true);
            if (this.condNode != null)
            {
                this.condNode.outputNode(destFile);
            }
            FileOperate.outputFileUsingUsingBuffer(destFile, this.SEMICNTokenList.get(1).toString() + "\n", true);
            if (this.forStmtNode2 != null)
            {
                this.forStmtNode2.outputNode(destFile);
            }
            FileOperate.outputFileUsingUsingBuffer(destFile, this.RPARENTToken.toString() + "\n", true);
            this.stmtNodeList.get(0).outputNode(destFile);
        }
        else if (this.stmtType == StmtType.BREAK)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.BREAKTKToken.toString() + "\n", true);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.SEMICNTokenList.get(0).toString() + "\n", true);
        }
        else if (this.stmtType == StmtType.CONTINUE)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.CONTINUETKToken.toString() + "\n", true);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.SEMICNTokenList.get(0).toString() + "\n", true);
        }
        else if (this.stmtType == StmtType.RETURN)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.RETURNTKToken.toString() + "\n", true);
            if (this.expNodeList.size() != 0)
            {
                this.expNodeList.get(0).outputNode(destFile);
            }
            FileOperate.outputFileUsingUsingBuffer(destFile, this.SEMICNTokenList.get(0).toString() + "\n", true);
        }
        else if (this.stmtType == StmtType.GETINT)
        {
            this.lValNode.outputNode(destFile);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.ASSIGNToken.toString() + "\n", true);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.GETINTTKToken.toString() + "\n", true);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.LPARENTToken.toString() + "\n", true);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.RPARENTToken.toString() + "\n", true);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.SEMICNTokenList.get(0).toString() + "\n", true);
        }
        else if (this.stmtType == StmtType.PRINTF)
        {
            FileOperate.outputFileUsingUsingBuffer(destFile, this.PRINTFTKToken.toString() + "\n", true);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.LPARENTToken.toString() + "\n", true);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.STRCONToken.toString() + "\n", true);
            for (int i = 0; i < this.COMMATokenList.size(); i++)
            {
                FileOperate.outputFileUsingUsingBuffer(destFile, this.COMMATokenList.get(i).toString() + "\n", true);
                this.expNodeList.get(i).outputNode(destFile);
            }
            FileOperate.outputFileUsingUsingBuffer(destFile, this.RPARENTToken.toString() + "\n", true);
            FileOperate.outputFileUsingUsingBuffer(destFile, this.SEMICNTokenList.get(0).toString() + "\n", true);
        }

        FileOperate.outputFileUsingUsingBuffer(destFile, ParserUtils.nodeMap.get(this.getType()) + "\n", true);

    }

    @Override
    public void parseSymbol(SymbolTable st) throws CompilerException
    {
        switch (this.stmtType)
        {
            case ASSIGNMENT:
                this.lValNode.parseSymbol(st);
                this.expNodeList.get(0).parseSymbol(st);
                break;
            case EXPRESSION:
                if (this.expNodeList.size() != 0)
                {
                    this.expNodeList.get(0).parseSymbol(st);
                }
                break;
            case BLOCK:
                SymbolTable newSt = new SymbolTable(st);
                this.blockNode.parseSymbol(newSt);
                break;
            case IF:
                this.condNode.parseSymbol(st);
                this.stmtNodeList.get(0).parseSymbol(st);
                if (this.ELSETKToken != null)
                {
                    this.stmtNodeList.get(1).parseSymbol(st);
                }
                break;
            case FOR:
                if (this.forStmtNode1 != null)
                {
                    this.forStmtNode1.parseSymbol(st);
                }
                if (this.condNode != null)
                {
                    this.condNode.parseSymbol(st);
                }
                if (this.forStmtNode2 != null)
                {
                    this.forStmtNode2.parseSymbol(st);
                }
                this.stmtNodeList.get(0).parseSymbol(st);
                break;
            case BREAK:
                break;
            case CONTINUE:
                break;
            case RETURN:
                if (!this.expNodeList.isEmpty())
                {
                    this.expNodeList.get(0).parseSymbol(st);
                }
                break;
            case GETINT:
                this.lValNode.parseSymbol(st);
                break;
            case PRINTF:
                for (ExpNode expNode : expNodeList)
                {
                    expNode.parseSymbol(st);
                }
                break;
        }
    }
}
