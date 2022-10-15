/**
 * 基于访问者模式设计Expr类，便于添加新的表达式类型
 */
interface ExprVisitor {
    public Object visitOptBinaryExpr(OptBinaryExpr expr);
    public Object visitBinaryExpr(BinaryExpr expr);
    public Object visitExpressionExpr(ExpressionExpr expr);
    public Object visitNumberExpr(NumberExpr expr);
    public Object visitNullExpr(NullExpr expr);

}


public abstract class Expr {
    abstract Object accept(ExprVisitor visitor);
}

class OptBinaryExpr extends Expr {
    OptBinaryExpr(Token op, Expr left, Expr right) {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    Object accept(ExprVisitor visitor) {
        return visitor.visitOptBinaryExpr(this);
    }

    @Override
    public String toString() {
        return left + " " + op + " " + right;
    }

    final Expr left;
    final Token op;
    final Expr right;
}

class BinaryExpr extends Expr {
    BinaryExpr(Expr e1, Expr e2) {
        this.e1 = e1;
        this.e2 = e2;
    }
    @Override
    Object accept(ExprVisitor visitor) {
        return visitor.visitBinaryExpr(this);
    }

    @Override
    public String toString() {
        return e1 + " " + e2;
    }
    final Expr e1;
    final Expr e2;
}

class ExpressionExpr extends Expr {
    ExpressionExpr(Expr expression) {
        this.expression = expression;
    }

    @Override
    Object accept(ExprVisitor visitor) {
        return visitor.visitExpressionExpr(this);
    }

    @Override
    public String toString() {
        return expression.toString();
    }

    final Expr expression;
}

class NumberExpr extends Expr {
    NumberExpr(String number) {
        this.number = number;
    }
    @Override
    Object accept(ExprVisitor visitor) {
        return visitor.visitNumberExpr(this);
    }

    @Override
    public String toString() {
        return number;
    }

    final String number;
}

class NullExpr extends Expr {
    @Override
    Object accept(ExprVisitor visitor) {
        return visitor.visitNullExpr(this);
    }

    @Override
    public String toString() {
        return ""; //ε
    }
}