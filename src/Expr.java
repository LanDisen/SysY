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
    boolean hasError = false;
}

class OptBinaryExpr extends Expr {
    OptBinaryExpr(Token op, Expr left, Expr right) {
        this.op = op;
        this.left = left;
        this.right = right;
        if (left.hasError || right.hasError)
            this.hasError = true;
    }

    @Override
    Object accept(ExprVisitor visitor) {
        return visitor.visitOptBinaryExpr(this);
    }

    @Override
    public String toString() {
        return op + left.toString() + right.toString();
    }

    final Expr left;
    final Token op;
    final Expr right;
}

class BinaryExpr extends Expr {
    BinaryExpr(Expr e1, Expr e2) {
        this.e1 = e1;
        this.e2 = e2;
        if (e1.hasError || e2.hasError)
            this.hasError = true;
    }
    @Override
    Object accept(ExprVisitor visitor) {
        return visitor.visitBinaryExpr(this);
    }

    @Override
    public String toString() {
        return e1.toString() + e2.toString();
    }
    final Expr e1;
    final Expr e2;
}

class ExpressionExpr extends Expr {
    ExpressionExpr(Expr expression, boolean hasBrackets) {
        this.expression = expression;
        this.hasBracket = hasBrackets;
        if (hasBrackets) {
            lBracket= "(";
            rBracket = ")";
        } else {
            lBracket = null;
            rBracket = null;
        }
        if (expression.hasError)
            this.hasError = true;
    }

    @Override
    Object accept(ExprVisitor visitor) {
        return visitor.visitExpressionExpr(this);
    }

    @Override
    public String toString() {
        if (hasBracket) {
            return "(" + expression.toString() + ")";
        }
        return expression.toString();
    }

    final Expr expression;
    final boolean hasBracket;
    final String lBracket;
    final String rBracket;
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