package frontend.parser;

import frontend.lexer.token.Token;
import frontend.lexer.token.TokenType;
import frontend.parser.node.NodeType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ParserUtils
{
    public static void GenerateNodeClasses()
    {
        NodeType[] nodeTypes = NodeType.values();

        String outputPath = "src/frontend/parser/node/";

        for (NodeType nodeType : nodeTypes)
        {
            String enumName = nodeType.name();
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


}
