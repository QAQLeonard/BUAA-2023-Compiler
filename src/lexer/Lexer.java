package lexer;

import lexer.token.Token;

import java.io.*;
import java.util.ArrayList;

import static lexer.LexerUtils.*;
import static utils.FileOperate.*;

public class Lexer
{
    private int buffer = -1;
    private ArrayList<Token> tokens = new ArrayList<>();

    public void run() throws IOException
    {
        File srcFile = new File("testfile.txt");
        File destFile = new File("output.txt");
        if (!srcFile.exists())
        {
            throw new FileNotFoundException("File not found!");
        }
        CreateFileUsingJava7Files(destFile);
        try (FileReader fr = new FileReader(srcFile))
        {
            int c;
            int lineNum = 1;
            while ((c = getNextChar(fr)) != -1)
            {
                if (c == '\n')
                {
                    lineNum++;
                }
                else if (Character.isLetter(c))
                {
                    processWord(fr, c, lineNum);
                }
                else if (Character.isDigit(c))
                {
                    processNumber(fr, c, lineNum);
                }
                else if (isSymbol((char) c))
                {
                    processSymbol(fr, c, lineNum);
                }

                else if (c == '"')
                {
                    processStr(fr, c, lineNum);
                }


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
        try
        {
            for(Token t : tokens)
            {
                outputFileUsingUsingBuffer(destFile, t.toString()+"\n", true);
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
            return c;
        }
        return fr.read();
    }

    private void UnGetCH(int c)
    {
        buffer = c;
    }

    private void processWord(FileReader fr, int firstChar, int lineNum) throws IOException
    {
        StringBuilder str = new StringBuilder();
        int c = firstChar;
        while (Character.isLetterOrDigit(c))
        {
            str.append((char) c);
            c = getNextChar(fr);
        }
        UnGetCH(c);
        tokens.add(generateWordToken(str.toString(), lineNum));
    }

    private void processNumber(FileReader fr, int firstChar, int lineNum) throws IOException
    {
        StringBuilder str = new StringBuilder();
        int c = firstChar;
        while (Character.isDigit(c))
        {
            str.append((char) c);
            c = getNextChar(fr);
        }
        UnGetCH(c);
        tokens.add(generateNumToken(str.toString(), lineNum));
    }

    private void processSymbol(FileReader fr, int firstChar, int lineNum) throws IOException
    {
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

        tokens.add(generateSymbolToken(str.toString(), lineNum));
    }

    private void processStr(FileReader fr, int firstChar, int lineNum) throws IOException
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
        tokens.add(generateStrToken(str.toString(), lineNum));
    }

}
