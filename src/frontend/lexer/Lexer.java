package frontend.lexer;

import frontend.lexer.token.Token;

import java.io.*;
import java.util.ArrayList;

import static frontend.lexer.LexerUtils.*;
import static utils.FileOperate.*;

public class Lexer
{
    private int buffer = -1;
    public static ArrayList<Token> tokenList = new ArrayList<>();

    public static int lineNum = 1;

    public void run() throws IOException
    {
        File srcFile = new File("testfile.txt");
        if (!srcFile.exists())
        {
            throw new FileNotFoundException("File not found!");
        }
        try (FileReader fr = new FileReader(srcFile))
        {
            int c;
            while ((c = getNextChar(fr)) != -1)
            {
                if (Character.isLetter(c) || c == '_') processWord(fr, c);
                else if (Character.isDigit(c)) processNumber(fr, c);
                else if (isSymbol((char) c)) processSymbol(fr, c);
                else if (c == '"') processStr(fr, c);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void output() throws IOException
    {
        File destFile = new File("output.txt");
        CreateFileUsingJava7Files(destFile);
        try
        {
            for (Token t : tokenList)
            {
                outputFileUsingUsingBuffer(destFile, t.toString() + "\n", true);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private int getNextChar(FileReader fr) throws IOException
    {
        if (buffer != -1)
        {
            int c = buffer;
            buffer = -1;
            if (c == '\n') lineNum++;
            return c;
        }
        int temp = fr.read();
        if (temp == '\n')
        {
            lineNum++;
        }
        return temp;
    }

    private void UnGetCH(int c)
    {
        buffer = c;
        if (c == '\n') lineNum--;
    }

    private void processWord(FileReader fr, int firstChar) throws IOException
    {
        StringBuilder str = new StringBuilder();
        int c = firstChar;
        while (Character.isLetterOrDigit(c) || c == '_')
        {
            str.append((char) c);
            c = getNextChar(fr);
        }
        UnGetCH(c);
        tokenList.add(generateWordToken(str.toString(), lineNum));
    }

    private void processNumber(FileReader fr, int firstChar) throws IOException
    {
        StringBuilder str = new StringBuilder();
        int c = firstChar;
        while (Character.isDigit(c))
        {
            str.append((char) c);
            c = getNextChar(fr);
        }
        UnGetCH(c);
        tokenList.add(generateNumToken(str.toString(), lineNum));
    }

    private void processSymbol(FileReader fr, int firstChar) throws IOException
    {
        /* if a comment */
        if (firstChar == '/')
        {
            int nextChar = getNextChar(fr);
            if (nextChar == '/')
            {
                while (nextChar != '\n')
                {
                    nextChar = getNextChar(fr);
                }
                return;
            }
            else if (nextChar == '*')
            {
                int c1 = getNextChar(fr);
                int c2 = getNextChar(fr);
                while (c1 != '*' || c2 != '/')
                {
                    c1 = c2;
                    c2 = getNextChar(fr);
                }
                return;
            }
            else
            {
                UnGetCH(nextChar);
            }
        }

        StringBuilder str = new StringBuilder();

        if (twoCharSymbols.containsKey((char) firstChar))
        {
            str.append((char) firstChar);
            int nextChar = getNextChar(fr);
            if (twoCharSymbols.get((char) firstChar) == (char) nextChar)
            {
                str.append((char) nextChar);
            }
            else
            {
                UnGetCH(nextChar);
            }
        }
        else
        {
            str.append((char) firstChar);
        }

        tokenList.add(generateSymbolToken(str.toString(), lineNum));
    }

    private void processStr(FileReader fr, int firstChar) throws IOException
    {
        StringBuilder str = new StringBuilder();
        str.append((char) firstChar);
        int c = getNextChar(fr);
        while (c != '"')
        {
            str.append((char) c);
            c = getNextChar(fr);
        }
        str.append('"');
        tokenList.add(generateStrToken(str.toString(), lineNum));
    }

}
