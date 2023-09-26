package frontend.parser;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.node.ConstExpNode;
import frontend.parser.node.NodeType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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

    public static void GenerateNodeClasses()
    {
        NodeType[] nodeMaps = NodeType.values();

        String outputPath = "src/frontend/parser/node/";

        for (NodeType nodeMap : nodeMaps)
        {
            String enumName = nodeMap.name();
            String className = enumName.substring(0, 1).toUpperCase() + enumName.substring(1) + "Node";

            String fileName = className + ".java";
            String fileContent = "package frontend.parser.node;\n\npublic class " + className + " extends Node {\n\n}\n";

            try
            {
                BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + fileName));
                writer.write(fileContent);
                writer.close();
                System.out.println("Generated " + fileName);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static Token findNearestTokenByType(TokenType targetType)
    {
        int i = Parser.tokenIndex;
        while (i < Parser.tokenList.size())
        {
            if (Parser.tokenList.get(i).getType() == targetType)
            {
                return Parser.tokenList.get(i);
            }
            i++;
        }
        // 如果未找到匹配的Token，则返回-1表示未找到
        return null;
    }

    public static Set<TokenType> UnaryOpTokenTypes = EnumSet.of(TokenType.PLUS, TokenType.MINU, TokenType.NOT);

    public static void parseArrayDimension(ArrayList<Token> LBRACKTokenList, ArrayList<ConstExpNode> constExpNodeList, ArrayList<Token> RBRACKTokenList)
    {
        LBRACKTokenList.add(Parser.getToken());
        ConstExpNode constExpNode = new ConstExpNode();
        constExpNode.parseNode();
        constExpNodeList.add(constExpNode);
        RBRACKTokenList.add(Parser.getToken());
    }

    private TokenType peekTokenType()
    {
        return Objects.requireNonNull(Parser.peekToken(0)).getType();
    }

}
