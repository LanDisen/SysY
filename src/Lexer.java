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
    public int line = 1;   //统计文件行数
    public int colomn = 0; //统计Token在当前行的位置

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
                case '+' -> tokens.add(new Token(TokenType.PLUS, "+", line, colomn++));
                case '-' -> tokens.add(new Token(TokenType.MINUS, "-", line, colomn++));
                case '*' -> tokens.add(new Token(TokenType.STAR, "*", line, colomn++));
                case '/' -> {
                    if (peek() == '/' || peek() == '*') {
                        commont(); // 注释
                        continue;
                    }
                    tokens.add(new Token(TokenType.SLASH, "/", line, colomn++));
                }
                case '\\' -> tokens.add(new Token(TokenType.BACKSLASH, "\\", line, colomn++));
                case '(' -> tokens.add(new Token(TokenType.LEFT_BRACKET, "(", line, colomn++));
                case ')' -> tokens.add(new Token(TokenType.RIGHT_BRACKET, ")", line, colomn++));
                case '[' -> tokens.add(new Token(TokenType.LEFT_MID_BRACKET, "[", line, colomn++));
                case ']' -> tokens.add(new Token(TokenType.RIGHT_MID_BRACKET, "]", line, colomn++));
                case '{' -> tokens.add(new Token(TokenType.LEFT_BIG_BRACKET, "{", line, colomn++));
                case '}' -> tokens.add(new Token(TokenType.RIGHT_BIG_BRACKET, "}", line, colomn++));
                case '|' -> {
                    Token token = null;
                    if (peek() == '|') {
                        token = new Token(TokenType.OR, "||", line, colomn++);
                        token.type = TokenType.OR;
                        token.word = "||";
                        moveForward();
                    }
                    tokens.add(token);
                }
                case '&' -> {
                    Token token = null;
                    if (peek() == '&') {
                        token = new Token(TokenType.AND, "&&", line, colomn++);
                        token.type = TokenType.AND;
                        token.word = "||";
                        moveForward();
                    }
                    tokens.add(token);
                }
                case '=' -> {
                    Token token = new Token(TokenType.EQUAL, "=", line, colomn++);
                    if (peek() == '=') {
                        token.type = TokenType.EQUAL_EQUAL;
                        token.word = "==";
                        moveForward();
                    }
                    tokens.add(token);
                }
                case '>' -> {
                    Token token = new Token(TokenType.GREATER, ">", line, colomn++);
                    if (peek() == '=') {
                        token.type = TokenType.GREATER_EQUAL;
                        token.word = ">=";
                        moveForward();
                    }
                    tokens.add(token);
                }
                case '<' -> {
                    Token token = new Token(TokenType.LESS, "<", line, colomn++);
                    if (peek() == '=') {
                        token.type = TokenType.LESS_EQUAL;
                        token.word = "<=";
                        moveForward();
                    }
                    tokens.add(token);
                }
                case ',' -> tokens.add(new Token(TokenType.COMMA, ",", line, colomn++));
                case '.' -> tokens.add(new Token(TokenType.DOT, ".", line, colomn++));
                case ':' -> tokens.add(new Token(TokenType.COLON, ":", line, colomn++));
                case '?' -> tokens.add(new Token(TokenType.QUESTION, "?", line, colomn++));
                case '!' -> {
                    Token token = new Token(TokenType.EXCLAMATION, "!", line, colomn++);
                    if (peek() == '=') {
                        token.type = TokenType.NOT_EQUAL;
                        token.word = "!=";
                        moveForward();
                    }
                    tokens.add(token);
                }
                case ';' -> tokens.add(new Token(TokenType.SEMICOLON, ";", line, colomn++));
                case '"' -> {
                    string();
                }
                default -> {
                    if (isNewLine(c)) {
                        line++; colomn = 0; break;
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

    void commont() {
        if (peek() == '/') {
            do {moveForward(); } while (peek() != '\n' && !isScanOver());
            line++;
        } else if (peek() == '*') {
            while (!isScanOver()) {
                if (peek() == '*') {
                    moveForward();
                    if (moveForward() == '/') break;
                } else if (peek() == '\n') {
                    moveForward();
                    line++;
                    continue;
                } else {
                    moveForward();
                    continue;
                }
            }
        }
    }

    void identifier() {
        while (Character.isLetterOrDigit(peek()) || peek()=='_')
            moveForward();
        String word = src.substring(begin, curr);
        TokenType type = Token.keywords.get(word);
        if (type == null) {
            type = TokenType.ID;
        }
        tokens.add(new Token(type, word, line, colomn++));
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
        tokens.add(new Token(type, word, line, colomn++));
    }

    void string() {
        moveForward();
        while (!isScanOver() && peek() != '"') {
            if (peek() == '\n') {
                new Error(line, colomn, "missing terminated '\"' character");
            }
            moveForward();
        }
        if (isScanOver()) {
            new Error(line, colomn, "missing terminated '\"' character");
        }
        moveForward();
        String word = src.substring(begin+1, curr-1);
        TokenType type = TokenType.STRING;
        tokens.add(new Token(type, word, line, colomn++));
    }

    void error() {
        TokenType type = TokenType.ERROR;
        String word = src.substring(begin, curr);
        tokens.add(new Token(type, word, line, colomn++));
    }
}
