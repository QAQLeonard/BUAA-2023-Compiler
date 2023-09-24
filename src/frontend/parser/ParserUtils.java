package frontend.parser;

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
}
