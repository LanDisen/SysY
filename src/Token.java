import java.util.HashMap;

public class Token {
    public TokenType type;
    public String word;
    final int line;
    final int column;

    public static HashMap<String, TokenType> keywords = new HashMap<>();

    public Token(TokenType type, String word, int line, int column) {
        this.type = type;
        this.word = word;
        this.line = line;
        this.column = column;
    }

    static void initKeywords() {
        //keywords
        keywords.put("and", TokenType.AND);
        keywords.put("or", TokenType.OR);
        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("return", TokenType.RETURN);
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("for", TokenType.FOR);
        keywords.put("while", TokenType.WHILE);
        keywords.put("do", TokenType.DO);
        keywords.put("break", TokenType.BREAK);
        keywords.put("continue", TokenType.CONTINUE);
        keywords.put("const", TokenType.CONST);
        keywords.put("case", TokenType.CASE);
        keywords.put("switch", TokenType.SWITCH);
        //data type
        keywords.put("int", TokenType.INT);
        keywords.put("float", TokenType.FLOAT);
        keywords.put("void", TokenType.VOID);
        keywords.put("bool", TokenType.BOOL);
        keywords.put("char", TokenType.CHAR);
        keywords.put("string", TokenType.STRING);
        keywords.put("print", TokenType.PRINT);
        keywords.put("println", TokenType.PRINTLN);
    }

    @Override
    public String toString() {
        return word;
    }
}

enum TokenType {
    PLUS, MINUS, STAR,
    SLASH, BACKSLASH, //斜杠，反斜杠
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,
    //逗号，句点，冒号，问号，感叹号，分号
    COMMA, DOT, COLON, QUESTION, EXCLAMATION, SEMICOLON,
    //括号
    LEFT_BRACKET, RIGHT_BRACKET,
    LEFT_MID_BRACKET, RIGHT_MID_BRACKET,
    LEFT_FLOWER_BRACKET, RIGHT_FLOWER_BRACKET,
    //keyword
    AND, OR, IF, ELSE, RETURN,
    TRUE, FALSE, FOR, WHILE, DO,
    BREAK, CONTINUE, CONST, CASE, SWITCH,
    //数据类型
    INT, FLOAT, VOID, BOOL, CHAR, STRING,
    ID, NUM, ERROR,  //identifier
    PRINT, PRINTLN,
    END
}
