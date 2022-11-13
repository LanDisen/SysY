import java.util.Vector;

/**
 * 语法分析器
 * 算术表达式文法：
 * expression  ->  term ( ("+"|"-") term )*
 * term        ->  factor ( ("*"|"/") factor )*
 * factor      ->  "(" expression ")" | NUM
 *
 * 表达式文法
 * expression  ->  assignment
 * assignment  ->  ID "=" assignment | condition
 * condition   ->  logicOr
 * logicOr     ->  logicAnd ("||" logicAnd)*
 * logicAnd    ->  equality ("&&" equality)*
 * equality    ->  comparison ( ("!="|"==") comparison)*
 * comparison  ->  term ( (">"|">="|"<"|"<=") term)*
 * term        ->  factor( ("+"|"-") factor)*
 * factor      ->  unary ( ("*"|"/") unary )*
 * unary       ->  ("!"|"-") unary | primary
 * primary     ->  NUM | "true" | "false"
 *              | "(" expression ")"
 *              | ID
 *
 * 语句文法
 * block           -> "{" declaration "}"
 * declaration     ->  varDecl
 *                  |  statement
 * varDecl         ->  ("int"|"float") ID ("=" expression)? ";"
 * statement       ->  expressionStmt
 *                  |  printStatement
 *                  |  ifStatement
 * printStmt       ->  ("print"|“println“) "("expression")"
 * expressionStmt  ->  expression ";"
 */


public class Parser {
    Parser(final Vector<Token> tokens) {
        this.tokens = tokens;
        parse();
    }

    void parse() {
        while (!isParseOver()) {
            //statements.add(statement());
            statements.add(declaration());
        }
    }

    Stmt declaration() {
        if (match(TokenType.INT) || match(TokenType.FLOAT)) {
            return varDeclaration(); //temp
        }
        return statement();
    }

    //TODO 变量声明定义待实现
    Stmt varDeclaration() {
        if (match(TokenType.ID)) {
            Token name = getPrev();
            if (match(TokenType.EQUAL)) {
                Expr expr = expression();
                if (!match(TokenType.SEMICOLON))
                    new Error(peek(), "expect ';' here");
                return new VarDeclStmt(name, expr);
            }
            if (!match(TokenType.SEMICOLON))
                new Error(peek(), "expect ';' here");
        }
        new Error(peek(), "expect an identifier when declaration");
        return null;
    }

    Stmt statement() {
        if (match(TokenType.PRINT)) return printStatement();
        if (match(TokenType.PRINTLN)) return printStatement();
        if (match(TokenType.IF)) return ifStatement();
        if (match(TokenType.WHILE)) return whileStmt();
        if (match(TokenType.FOR)) return forStmt();
        if (match(TokenType.LEFT_BIG_BRACKET)) return new BlockStmt(block());
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

    Stmt ifStatement() {
        if (!match(TokenType.LEFT_BRACKET))
            new Error(peek(), "expect '(' after 'if' statement");
        Expr condition = condition();
        if (!match(TokenType.RIGHT_BRACKET))
            new Error(peek(), "expect ')' after 'if' here");
        Stmt thenStmt = statement();
        Stmt elseStmt = null;
        if (match(TokenType.ELSE)) {
            elseStmt = statement();
        }
        return new IfStmt(condition, thenStmt, elseStmt);
    }

    Stmt whileStmt() {
        if (!match(TokenType.LEFT_BRACKET))
            new Error(peek(), "expect '(' after 'while' statement");
        Expr condition = condition();
        if (!match(TokenType.RIGHT_BRACKET))
            new Error(peek(), "expect ')' after 'while' here");
        Stmt body = statement();
        return new WhileStmt(condition, body);
    }

    Stmt forStmt() {
        return null;
    }

    Vector<Stmt> block() {
        Vector<Stmt> statements = new Vector<>();
        while (!match(TokenType.RIGHT_BIG_BRACKET) && !isParseOver()) {
            statements.add((declaration()));
        }
        if (isParseOver() && getPrev().type!=TokenType.RIGHT_BIG_BRACKET)
            new Error(getPrev(), "expect '}' after a block");
        return statements;
    }

    Stmt expressionStatement() {
        ExpressionStmt expressionStmt = new ExpressionStmt(expression());
        if (!match(TokenType.SEMICOLON)) {
            new Error(peek(), "expect ';' here");
        }
        return expressionStmt;
    }

    Expr expression() {
        return assignment();
    }

    Expr assignment() {
        Expr expr = condition();
        //TODO 未完成
        if (match(TokenType.EQUAL)) {
            Token equals = getPrev();
            Expr value = assignment();
            VarExpr var = (VarExpr) expr;
            if (var != null) {
                Token name = var.name;
                return new AssignExpr(name, value);
            }
            new Error(equals, "illegal assignment");
        }
        return expr;
    }

    Expr condition() {
        Expr expr = logicOr();
        //TODO 三目运算符待实现
        return expr;
    }

    Expr logicOr() {
        Expr expr = logicAnd();
        while (match(TokenType.OR)) {
            Token op = getPrev();
            Expr right = logicAnd();
            expr = new LogicalExpr(expr, op, right);
        }
        return expr;
    }

    Expr logicAnd() {
        Expr expr = equality();
        while (match(TokenType.AND)) {
            Token op = getPrev();
            Expr right = equality();
            expr = new LogicalExpr(expr, op, right);
        }
        return expr;
    }

    Expr equality() {
        Expr expr = comparison();
        while (match(TokenType.EQUAL_EQUAL) || match(TokenType.NOT_EQUAL)) {
            Token op = getPrev();
            Expr right = comparison();
            expr = new BinaryExpr(expr, op, right);
        }
        return expr;
    }

    Expr comparison() {
        Expr expr = term();
        while (match(TokenType.GREATER) || match(TokenType.GREATER_EQUAL)
               || match(TokenType.LESS) || match(TokenType.LESS_EQUAL)) {
            Token op = getPrev();
            Expr right = term();
            expr = new BinaryExpr(expr, op, right);
        }
        return expr;
    }

    Expr term() {
        Expr expr = factor();
        while (match(TokenType.PLUS) || match(TokenType.MINUS)) {
            Token op = getPrev();
            Expr right = factor();
            expr = new BinaryExpr(expr, op, right);
        }
        return expr;
    }

    Expr factor() {
        Expr expr = unary();
        while (match(TokenType.STAR) || match(TokenType.SLASH)) {
            Token op = getPrev();
            Expr right = unary();
            expr = new BinaryExpr(expr, op, right);
        }
        return expr;
    }

    Expr unary() {
        if (match(TokenType.EXCLAMATION) || match(TokenType.MINUS)) {
            Token op = getPrev();
            Expr right = unary();
            return new UnaryExpr(op, right);
        }
        Expr expr = primary();
        return expr;
    }

    Expr primary() {
        if (match(TokenType.NUM) || match(TokenType.NONE)
        || match(TokenType.TRUE) || match(TokenType.FALSE)) {
            return new PrimaryExpr(getPrev().word);
        }
        if (match(TokenType.ID)) {
            return new VarExpr(getPrev());
        }
        if (match(TokenType.LEFT_BRACKET)) {
            Expr expr = expression();
            if (match(TokenType.RIGHT_BRACKET)) {
                //pass
            } else {
                new Error(peek(), "expect ')' here");
            }
            return expr;
        }
        new Error(peek(), "this is not a legal expression");
        return null;
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
