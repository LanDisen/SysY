/**
 * 基于访问者模式设计Expr类，便于添加新的表达式类型
 */
interface ExprVisitor {
    public Object visitOptBinaryExpr(OptBinaryExpr expr);
    public Object visitBinaryExpr(BinaryExpr expr);
    public Object visitExpressionExpr(ExpressionExpr expr);

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

    final Expr expression;
}
