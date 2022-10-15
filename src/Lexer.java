import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

@SuppressWarnings({"all"})
public class Lexer {

    Vector<Token> tokens = new Vector<>();
    public String src = "";
    public int begin = 0;
    public int curr = 0;
    public int line = 1; //统计文件行数

    Lexer(File file) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            int c;
            while ((c = fileReader.read()) != -1)
                src += (char) c;
            Token.initKeywords();
            scan();
        } catch (IOException e) {
            System.out.println("源文件打开失败");
        }
    }

    void scan() {
        while (!isScanOver()) {
            begin = curr;
            char c = moveForward();
            switch (c) {
                case '+' -> tokens.add(new Token(TokenType.PLUS, "+", line));
                case '-' -> tokens.add(new Token(TokenType.MINUS, "-", line));
                case '*' -> tokens.add(new Token(TokenType.STAR, "*", line));
                case '/' -> tokens.add(new Token(TokenType.SLASH, "/", line));
                case '\\' -> tokens.add(new Token(TokenType.BACKSLASH, "\\", line));
                case '(' -> tokens.add(new Token(TokenType.LEFT_BRACKET, "(", line));
                case ')' -> tokens.add(new Token(TokenType.RIGHT_BRACKET, ")", line));
                case '=' -> {
                    Token token = new Token(TokenType.EQUAL, "=", line);
                    if (peek() == '=') {
                        token.type = TokenType.EQUAL_EQUAL;
                        token.word = "==";
                        moveForward();
                    }
                    tokens.add(token);
                }
                case '>' -> {
                    Token token = new Token(TokenType.GREATER, ">", line);
                    if (peek() == '=') {
                        token.type = TokenType.GREATER_EQUAL;
                        token.word = ">=";
                        moveForward();
                    }
                    tokens.add(token);
                }
                case '<' -> {
                    Token token = new Token(TokenType.LESS, "<=>", line);
                    if (peek() == '=') {
                        token.type = TokenType.LESS_EQUAL;
                        token.word = "<=";
                        moveForward();
                    }
                    tokens.add(token);
                }
                case ',' -> tokens.add(new Token(TokenType.COMMA, ",", line));
                case '.' -> tokens.add(new Token(TokenType.DOT, ".", line));
                case ':' -> tokens.add(new Token(TokenType.COLON, ":", line));
                case '?' -> tokens.add(new Token(TokenType.QUESTION, "?", line));
                case '!' -> tokens.add(new Token(TokenType.EXCLAMATION, "!", line));
                case ';' -> tokens.add(new Token(TokenType.SEMICOLON, ";", line));
                default -> {
                    if (isNewLine(c)) {
                        line++; break;
                    }
                    if (isBlank(c))
                        break;
                    if (Character.isLetter(c) || c == '_')
                        identifier();
                    else if (Character.isDigit(c))
                        number();
                    else
                        error();
                }
            }
        }

    }

    boolean isScanOver() {
        if (curr >= src.length())
            return true;
        return false;
    }

    boolean isBlank(char c) {
        if (c==' ' || c=='\t' || c=='\r')
            return true;
        return false;
    }

    boolean isNewLine(char c) {
        return c == '\n';
    }

    char peek() {
        if (isScanOver())
            return '\0';
        return src.charAt(curr);
    }

    char moveForward() {
        return src.charAt(curr++);
    }

    void identifier() {
        while (Character.isLetterOrDigit(peek()) || peek()=='_')
            moveForward();
        String word = src.substring(begin, curr);
        TokenType type = Token.keywords.get(word);
        if (type == null) {
            type = TokenType.ID;
        }
        tokens.add(new Token(type, word, line));
    }

    void number() {
        while (Character.isDigit(peek()))
            moveForward();
        if (peek() == '.') {
            moveForward();
            while (Character.isDigit(peek()))
                moveForward();
        }
        String word = src.substring(begin, curr);
        TokenType type = TokenType.NUM;
        tokens.add(new Token(type, word, line));
    }

    void error() {
        TokenType type = TokenType.ERROR;
        String word = src.substring(begin, curr);
        tokens.add(new Token(type, word, line));
    }
}
