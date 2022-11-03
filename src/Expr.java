/**
 * 基于访问者模式设计Expr类，便于添加新的表达式类型
 */

public abstract class Expr {
    abstract Object accept(ExprVisitor visitor);
}

//二叉树结构的表达式，根为操作符
class BinaryExpr extends Expr {
    BinaryExpr(Expr left, Token op, Expr right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }
    @Override
    Object accept(ExprVisitor visitor) {
        return visitor.visitBinaryExpr(this);
    }

    @Override
    public String toString() {
        return left + op.word + right;
    }

    final Expr left;
    final Token op;
    final Expr right;
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
//        if (expression.hasError)
//            this.hasError = true;
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

class UnaryExpr extends Expr {
    UnaryExpr(Token symbol, Expr value) {
        this.symbol = symbol;
        this.value = value;
    }
    @Override
    Object accept(ExprVisitor visitor) {
        return visitor.visitUnaryExpr(this);
    }

    final Token symbol; //符号
    final Expr value;
}

class VarExpr extends Expr {
    VarExpr(Token name) {
        this.name = name;
    }
    @Override
    Object accept(ExprVisitor visitor) {
        return visitor.visitVarExpr(this);
    }
    final Token name;
    @Override
    public String toString() {
        return name.word;
    }
}

class AssignExpr extends Expr {
    AssignExpr(Token name, Expr value) {
        this.name = name;
        this.value = value;
    }

    @Override
    Object accept(ExprVisitor visitor) {
        return visitor.visitAssignExpr(this);
    }

    final Token name;
    final Expr value;
}

class LogicalExpr extends Expr {
    LogicalExpr(Expr left, Token op, Expr right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    Object accept(ExprVisitor visitor) {
        return visitor.visitLogicalExpr(this);
    }

    final Expr left;
    final Token op;
    final Expr right;
}

class PrimaryExpr extends Expr {
    PrimaryExpr(String value) {
        this.value = value;
    }
    @Override
    Object accept(ExprVisitor visitor) {
        return visitor.visitPrimaryExpr(this);
    }
    final String value;
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