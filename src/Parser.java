import java.util.Vector;

/**
 * 语法分析器
 * 算术表达式文法：
 * expression  ->  term ( ("+"|"-") term )*
 * term        ->  factor ( ("*"|"/") factor )*
 * factor      ->  "(" expression ")" | NUMBER
 *
 * 语句文法
 * statement  ->  expressionStmt
 *             |  printStmt
 * printStmt  -> ("print"|“println“) "("expression")"
 *
 */


public class Parser {
    Parser(final Vector<Token> tokens) {
        this.tokens = tokens;
        parse();
    }

    void parse() {
        while (!isParseOver()) {
            statements.add(statement());
        }
    }

    Stmt statement() {
        if (match(TokenType.PRINT)) return printStatement();
        if (match(TokenType.PRINTLN)) return printStatement();
        return expressionStatement();
    }

    Stmt printStatement() {
        Stmt printStmt = null;
        boolean hasLn = false;
        if (getPrev().type == TokenType.PRINTLN)
            hasLn = true;
        if (match(TokenType.LEFT_BRACKET)) {
            Expr expr = expression();
            if (match(TokenType.RIGHT_BRACKET)) {
                printStmt = new PrintStmt(expr, hasLn);
            } else {
                new Error(peek(), "missing a ')' here");
            }
        } else {
            new Error(peek(), "expect '(' after print statement");
        }
        if (!match(TokenType.SEMICOLON))
            new Error(peek(), "expect ';' here");
        return printStmt;
    }

    Stmt expressionStatement() {
        ExpressionStmt expressionStmt = new ExpressionStmt(expression());
        if (!expressionStmt.hasError) {
            statements.add(expressionStmt);
        }
        if (!match(TokenType.SEMICOLON)) {
            new Error(peek(), "expect ';' here");
        }
        return expressionStmt;
    }

    Expr expression() {
        Expr term = term();
        while (match(TokenType.PLUS) || match(TokenType.MINUS)) {
            Token op = getPrev();
            Expr term2 = term();
            term = new BinaryExpr(term, op, term2);
        }
        return term;
    }

    Expr term() {
        Expr factor = factor();
        while (match(TokenType.STAR) || match(TokenType.SLASH)) {
            Token op = getPrev();
            Expr factor2 = factor();
            factor = new BinaryExpr(factor, op, factor2);
        }
        return factor;
    }

    Expr factor() {
        Expr expr = new NullExpr();
        if (match(TokenType.LEFT_BRACKET)) {
            expr = expression();
            if (match(TokenType.RIGHT_BRACKET)) {
                //expr = new ExpressionExpr(expr, true);
            } else {
                expr.hasError = true;
                new Error(peek(), "expect ')' here");
            }
        } else if (match(TokenType.NUM)) {
            expr = new NumberExpr(getPrev().word);
        } else {
            expr.hasError = true;
            new Error(peek(), "this is not a valid arithmetic expression");
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
