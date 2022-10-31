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

class VarDeclStmt extends Stmt {
    VarDeclStmt(Token name, Expr expr) {
        this.name = name;
        this.expr = expr;
    }
    @Override
    Object accept(StmtVisitor visitor) {
        return visitor.visitVarDeclStmt(this);
    }

    final Token name; //变量名
    final Expr expr;
}

class IfStmt extends Stmt {
    IfStmt(Expr condition, Stmt thenStmt, Stmt elseStmt) {
        this.condition = condition;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }
    @Override
    Object accept(StmtVisitor visitor) {
        return visitor.visitIfStmt(this);
    }

    final Expr condition;
    final Stmt thenStmt;
    final Stmt elseStmt;
}

class WhileStmt extends Stmt {
    WhileStmt(Expr condition, Stmt body) {
        this.condition = condition;
        this.body = body;
    }
    @Override
    Object accept(StmtVisitor visitor) {
        return visitor.visitWhileStmt(this);
    }

    Expr condition;
    Stmt body;
}