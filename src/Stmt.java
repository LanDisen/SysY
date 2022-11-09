import java.util.Vector;

public abstract class Stmt {
    abstract Object accept(StmtVisitor visitor);
}

class ExpressionStmt extends Stmt {
    ExpressionStmt(Expr expression) {
        this.expression = expression;
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

    @Override
    public String toString() {
        String str = hasLn ? "print(" : "println(";
        str += expr.toString() + ")";
        return str;
    }

    final Expr expr;
    final boolean hasLn;
}

class BlockStmt extends Stmt {
    BlockStmt(Vector<Stmt> statements) {
        this.statements = statements;
    }

    @Override
    Object accept(StmtVisitor visitor) {
        return visitor.visitBlockStmt(this);
    }

    Vector<Stmt> statements;
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

    @Override
    public String toString() {
        return name.word;
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

    @Override
    public String toString() {
        String str = "if(" + condition + ")";
        return str;
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

    @Override
    public String toString() {
        String str = "while(" + condition + ")";
        return str;
    }

    Expr condition;
    Stmt body;
}