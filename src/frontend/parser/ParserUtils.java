package frontend.parser;

import backend.errorhandler.CompilerError;
import frontend.lexer.Lexer;
import frontend.lexer.Token;
import frontend.lexer.TokenType;
import frontend.parser.node.ConstExpNode;
import frontend.parser.node.ExpType;
import frontend.parser.node.NodeType;
import frontend.parser.symbol.ARRAYSymbol;
import frontend.parser.symbol.FUNCSymbol;
import frontend.parser.symbol.Symbol;
import frontend.parser.symbol.SymbolType;
import java.util.Stack;

import java.util.*;

public class ParserUtils
{
    public static final Map<NodeType, String> nodeMap = new HashMap<>();

    static
    {
        nodeMap.put(NodeType.CompUnit, "<CompUnit>");
        nodeMap.put(NodeType.Decl, "<Decl>");
        nodeMap.put(NodeType.ConstDecl, "<ConstDecl>");
        nodeMap.put(NodeType.BType, "<BType>");
        nodeMap.put(NodeType.ConstDef, "<ConstDef>");
        nodeMap.put(NodeType.ConstInitVal, "<ConstInitVal>");
        nodeMap.put(NodeType.VarDecl, "<VarDecl>");
        nodeMap.put(NodeType.VarDef, "<VarDef>");
        nodeMap.put(NodeType.InitVal, "<InitVal>");
        nodeMap.put(NodeType.FuncDef, "<FuncDef>");
        nodeMap.put(NodeType.MainFuncDef, "<MainFuncDef>");
        nodeMap.put(NodeType.FuncType, "<FuncType>");
        nodeMap.put(NodeType.FuncFParams, "<FuncFParams>");
        nodeMap.put(NodeType.FuncFParam, "<FuncFParam>");
        nodeMap.put(NodeType.Block, "<Block>");
        nodeMap.put(NodeType.BlockItem, "<BlockItem>");
        nodeMap.put(NodeType.Stmt, "<Stmt>");
        nodeMap.put(NodeType.ForStmt, "<ForStmt>");
        nodeMap.put(NodeType.Exp, "<Exp>");
        nodeMap.put(NodeType.Cond, "<Cond>");
        nodeMap.put(NodeType.LVal, "<LVal>");
        nodeMap.put(NodeType.PrimaryExp, "<PrimaryExp>");
        nodeMap.put(NodeType.Number, "<Number>");
        nodeMap.put(NodeType.UnaryExp, "<UnaryExp>");
        nodeMap.put(NodeType.UnaryOp, "<UnaryOp>");
        nodeMap.put(NodeType.FuncRParams, "<FuncRParams>");
        nodeMap.put(NodeType.MulExp, "<MulExp>");
        nodeMap.put(NodeType.AddExp, "<AddExp>");
        nodeMap.put(NodeType.RelExp, "<RelExp>");
        nodeMap.put(NodeType.EqExp, "<EqExp>");
        nodeMap.put(NodeType.LAndExp, "<LAndExp>");
        nodeMap.put(NodeType.LOrExp, "<LOrExp>");
        nodeMap.put(NodeType.ConstExp, "<ConstExp>");
    }

    public static int ForLoopCount = 0;

    public static Stack<FUNCSymbol> funcSymbolStack = new Stack<>();

    public static Token findNearestTokenByType(TokenType targetType)
    {
        int i = Parser.tokenIndex;
        while (i < Lexer.tokenList.size())
        {
            if (Lexer.tokenList.get(i).getType() == targetType)
            {
                return Lexer.tokenList.get(i);
            }
            i++;
        }
        // 如果未找到匹配的Token，则返回-1表示未找到
        return null;
    }

    public static Set<TokenType> UnaryOpTokenTypes = EnumSet.of(TokenType.PLUS, TokenType.MINU, TokenType.NOT);

    public static void parseArrayDimension(ArrayList<Token> LBRACKTokenList, ArrayList<ConstExpNode> constExpNodeList, ArrayList<Token> RBRACKTokenList)
    {
        LBRACKTokenList.add(Parser.getToken(TokenType.LBRACK));
        ConstExpNode constExpNode = new ConstExpNode();
        constExpNode.parseNode();
        constExpNodeList.add(constExpNode);
        RBRACKTokenList.add(Parser.getToken(TokenType.RBRACK));
    }

    public static boolean TypeEqual(ExpType expType, Symbol parameter)
    {
        if (expType == ExpType.INT && parameter.getType() == SymbolType.INT)
        {
            return true;
        }
        else if (parameter.getType() == SymbolType.ARRAY)
        {
            if (expType == ExpType.ARRAY1D && ((ARRAYSymbol) parameter).getDim() == 1)
            {
                return true;
            }
            else if (expType == ExpType.ARRAY2D && ((ARRAYSymbol) parameter).getDim() == 2)
            {
                return true;
            }
            return false;
        }
        return false;
    }


}