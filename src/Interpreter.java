import java.util.Vector;

public class Interpreter implements ExprVisitor, StmtVisitor{
    Interpreter(Vector<Stmt> statements) {
        this.statements = statements;
        interpret(statements);
    }

    void interpret(final Vector<Stmt> statements) {
        for (Stmt statement: statements) {
            execute(statement);
        }
    }

    Object execute(final Stmt stmt) {
        return stmt.accept(this);
    }

    Object evaluate(final Expr expr) {
        return expr.accept(this);
    }

    String stringify(final Object obj) {
        if (obj instanceof Double) {
            return String.valueOf((double) obj);
        }
        return null;
    }

    @Override
    public Object visitBinaryExpr(BinaryExpr expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);
        Token op = expr.op;
        switch (op.type) {
            case PLUS -> {
                return (double)left + (double)right;
            }
            case MINUS -> {
                return (double)left - (double)right;
            }
            case STAR -> {
                return (double)left * (double)right;
            }
            case SLASH -> {
                /* 除数为0异常，暂未实现
                double divideNum = (double) right;
                if (divideNum == 0) {
                    new Error((Token) right, "can't divided by zero");
                    return null;
                }
                 */
                return (double)left / (double)right;
            }
        }
        return null;
    }

    @Override
    public Object visitExpressionExpr(ExpressionExpr expr) {
        return evaluate(expr);
    }

    @Override
    public Object visitNumberExpr(NumberExpr expr) {
        return (double) Double.parseDouble(expr.number);
    }

    @Override
    public Object visitNullExpr(NullExpr expr) {
        return null;
    }

    @Override
    public Object visitExpressionStmt(ExpressionStmt stmt) {
        return evaluate(stmt.expression);
    }

    @Override
    public Object visitPrintStmt(PrintStmt stmt) {
        Object value = evaluate(stmt.expr);
        System.out.print(stringify(value));
        if (stmt.hasLn)
            System.out.print("\n");
        return null;
    }

    Vector<Stmt> statements;
}
