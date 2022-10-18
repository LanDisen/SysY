/**
 * statement  ->  expressionStmt
 *              | printStmt
 * printStmt  -> "print" "("expression")"
 */


public abstract class Stmt {
    abstract Object accept(StmtVisitor visitor);

    boolean hasError = false;
}

class ExpressionStmt extends Stmt {
    ExpressionStmt(Expr expression) {
        this.expression = expression;
        if (expression.hasError)
            this.hasError = true;
    }
    @Override
    public String toString() {
        return expression.toString();
    }

    @Override
    Object accept(StmtVisitor visitor) {
        return visitor.visitExpressionStmt(this);
    }

    final Expr expression;
    boolean isValid = true;
}

class PrintStmt extends Stmt {
    PrintStmt(Expr expr, boolean hasLn) {
        this.expr = expr;
        this.hasLn = hasLn;
        if (expr.hasError)
            this.hasError = true;
    }

    @Override
    Object accept(StmtVisitor visitor) {
        return visitor.visitPrintStmt(this);
    }

    final Expr expr;
    final boolean hasLn;
}