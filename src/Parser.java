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
        parse();
    }

    void parse() {
        while (!isParseOver()) {
            //System.out.println(curr);
            ExpressionStmt expressionStmt = new ExpressionStmt(expression());
            if (!expressionStmt.hasError) {
                statements.add(expressionStmt);
                System.out.println(expressionStmt);
            }
            if (!match(TokenType.SEMICOLON)) {
                new Error(peek(), "expect ';' here");
            }
        }
    }

    Expr expression() {
        Expr expr = new NullExpr();
        Expr termExpr = term();
        Expr expression1Expr = expression1();
        expr = new BinaryExpr(termExpr, expression1Expr);
        return expr;
    }

    Expr expression1() {
        Expr expr = new NullExpr();
        if (match(TokenType.PLUS) || match(TokenType.MINUS)) {
            Token opt = getPrev();
            Expr termExpr = term();
            Expr expression1Expr = expression1();
            expr = new OptBinaryExpr(opt, termExpr, expression1Expr);
        }
        return expr;
    }

    Expr term() {
        Expr expr = new NullExpr();
        Expr factorExpr = factor();
        Expr term1Expr = term1();
        expr = new BinaryExpr(factorExpr, term1Expr);
        return expr;
    }

    Expr term1() {
        Expr expr = new NullExpr();
        //匹配"*"|"/"
        if (match(TokenType.STAR) || match(TokenType.SLASH)) {
            Token opt = getPrev();
            Expr termExpr = term();
            Expr expression1Expr = expression1();
            expr = new OptBinaryExpr(opt, termExpr, expression1Expr);
        }
        return expr;
    }

    //factor  ->  "(" expression ")" | NUMBER
    //TODO 括号应被添加到expr内，后续再利用语法树进行优先级分析
    Expr factor() {
        Expr expr = new NullExpr();
        if (match(TokenType.LEFT_BRACKET)) {
            Expr expressionExpr = expression();
            if (match(TokenType.RIGHT_BRACKET)) {
                expr = new ExpressionExpr(expressionExpr, true);
            }
        } else if (match(TokenType.NUM)) {
            expr = new NumberExpr(getPrev().word);
        } else {
            expr.hasError = true;
            new Error(peek(), "this is not a valid arithmetic expression.");
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
        if (curr >= tokens.size()) return true;
        return peek().type == TokenType.END;
    }

    boolean match(TokenType type) {
        if (isParseOver()) return false;
        if (peek().type == type) {
            moveForward();
            return true;
        }
        return false;
    }

    Vector<Token> tokens;
    Vector<Stmt> statements = new Vector<>();
    int curr = 0;
}
