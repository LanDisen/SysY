public class Token {
    public TokenType type;
    public String word;

    public Token(TokenType type, String word) {
        this.type = type;
        this.word = word;
    }

    @Override
    public String toString() {
        return "(" + type + " " + word + ")";
    }
}

enum TokenType {
    PLUS, MINUS, STAR,
    SLASH, BACKSLASH, //斜杠，反斜杠
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,
    //逗号，句点，冒号，问号，感叹号
    COMMA, DOT, COLON, QUESTION, EXCLAMATION,
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
    ID, NUM, ERROR //identifier
}
