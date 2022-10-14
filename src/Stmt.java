interface StmtVisitor {
    Object visitExpressionStmt(ExpressionStmt stmt);
    Object visitPrintStmt(PrintStmt stmt);
}

public abstract class Stmt {
    abstract Object accept(StmtVisitor visitor);
}

class ExpressionStmt extends Stmt {
    ExpressionStmt(Expr expression) {
        this.expression = expression;
    }

    @Override
    Object accept(StmtVisitor visitor) {
        return visitor.visitExpressionStmt(this);
    }

    final Expr expression;
}

class PrintStmt extends Stmt {
    PrintStmt(Expr expr, boolean hasLn) {
        this.expr = expr;
        this.hasLn = hasLn;
    }

    @Override
    Object accept(StmtVisitor visitor) {
        return visitor.visitPrintStmt(this);
    }

    final Expr expr;
    final boolean hasLn;
}