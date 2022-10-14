import java.util.Vector;

/**
 * 语法分析器
 * 算术表达式文法：
 * expression  ->  term expression'
 * expression' ->  ("+"|"-") term expression' | ε
 * term        ->  factor term'
 * term'       ->  ("*"|"/") factor term' | ε
 * factor      ->  "(" expression ")" | NUMBER
 */
public class Parser {
    Parser(final Vector<Token> tokens) {
        this.tokens = tokens;
    }

    Vector<Stmt> parse() {
        Vector<Stmt> statements = new Vector<>();
        while (!isParseOver()) {
            //statements.add();
        }
        return statements;
    }

    Expr expression() {
        Expr expr = null;
        Expr termExpr = term();
        Expr expression1Expr = expression1();
        expr = new BinaryExpr(termExpr, expression1Expr);
        return expr;
    }

    Expr expression1() {
        Expr expr = null;
        while (match(TokenType.PLUS) || match(TokenType.MINUS)) {
            Token opt = getPrev();
            Expr termExpr = term();
            Expr expression1Expr = expression1();
            expr = new OptBinaryExpr(opt, termExpr, expression1Expr);
        }
        return expr;
    }

    Expr term() {
        Expr expr = null;
        Expr factorExpr = factor();
        Expr term1Expr = term1();
        expr = new BinaryExpr(factorExpr, term1Expr);
        return expr;
    }

    Expr term1() {
        Expr expr = null;
        //匹配"*"|"/"
        while (match(TokenType.STAR) || match(TokenType.SLASH)) {
            Token opt = getPrev();
            Expr termExpr = term();
            Expr expression1Expr = expression1();
            expr = new OptBinaryExpr(opt, termExpr, expression1Expr);
        }
        return expr;
    }

    //factor  ->  "(" expression ")" | NUMBER
    Expr factor() {
        Expr expr = null;
        while (match(TokenType.LEFT_BRACKET)) {
            Expr expressionExpr = expression();
            while (match(TokenType.RIGHT_BRACKET)) {
                expr = new ExpressionExpr(expressionExpr);
            }
        }
        return expr;
    }

    Token peek() {
        return tokens.elementAt(curr);
    }

    Token getPrev() {
        return tokens.elementAt(curr - 1);
    }

    Token moveForward() {
        if (!isParseOver()) curr++;
        return getPrev();
    }

    boolean isParseOver() {
        return peek().type == TokenType.END;
    }

    boolean match(TokenType type) {
        if (isParseOver()) return false;
        //moveForward();
        return peek().type == type;
    }

    Vector<Token> tokens;
    int curr = 0;
}
