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
        if (obj == null) {
            return "None";
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj ? "true": "false";
        }
        if (obj instanceof Double) {
            return String.valueOf((double) obj);
        }
        return null;
    }

    @Override
    public Object visitBinaryExpr(BinaryExpr expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);
//        left = Double.parseDouble((String) left);
//        right = Double.parseDouble((String) right);
        Token op = expr.op;
        switch (op.type) {
            case EQUAL_EQUAL -> {
                return isEqual(left, right);
            }
            case NOT_EQUAL -> {
                return !isEqual(left, right);
            }
            case GREATER -> {
                return (Double) left > (Double) right;
            }
            case GREATER_EQUAL -> {
                return (Double) left >= (Double) right;
            }
            case LESS -> {
                return (Double) left < (Double) right;
            }
            case LESS_EQUAL -> {
                return (Double) left <= (Double) right;
            }
            case PLUS -> {
                return (Double)left + (Double)right;
            }
            case MINUS -> {
                return (Double)left - (Double)right;
            }
            case STAR -> {
                return (Double)left * (Double)right;
            }
            case SLASH -> {
                /*TODO 实现除数为0异常判断
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
    public Object visitUnaryExpr(UnaryExpr expr) {
        Object right = evaluate(expr.value);
        switch (expr.symbol.type) {
            case MINUS -> {
                return -(Double) right;
            }
            case EXCLAMATION -> {
                return !isTrue(right);
            }
        }
        return null;
    }

    @Override
    public Object visitVarExpr(VarExpr expr) {
        Object value = null;

        return null;
    }

    @Override
    public Object visitAssignExpr(AssignExpr expr) {
        return null;
    }

    @Override
    public Object visitLogicalExpr(LogicalExpr expr) {
        Object left = evaluate(expr.left);
        if (expr.op.type == TokenType.OR) {
            if (isTrue(left)) return left;
        } else {
            if (!isTrue(left)) return left;
        }
        return evaluate(expr.right);
    }

    @Override
    public Object visitPrimaryExpr(PrimaryExpr expr) {
        return expr.value;
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

    @Override
    public Object visitVarDeclStmt(VarDeclStmt stmt) {
        Object value = null;
        if (stmt.expr != null) {
            value = evaluate(stmt.expr);
        }
        return value;
    }

    @Override
    public Object visitIfStmt(IfStmt stmt) {
        return null;
    }

    @Override
    public Object visitWhileStmt(WhileStmt stmt) {
        return null;
    }

    public static boolean isEqual(Object a, Object b) {
        if (a==null && b==null) return true;
        if (a instanceof Boolean && b instanceof Boolean) {
            return (Boolean) a == (Boolean) b;
        }
        if (a instanceof Double && b instanceof Double) {
            return (Boolean) a == (Boolean) b;
        }
        return false;
    }

    public static boolean isTrue(Object obj) {
        if (obj == null) return false;
        if (obj instanceof Boolean) return (Boolean) obj;
        return true;
    }

    Vector<Stmt> statements;
}
