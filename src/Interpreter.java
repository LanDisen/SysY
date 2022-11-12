import java.util.HashMap;
import java.util.Vector;

public class Interpreter implements ExprVisitor, StmtVisitor{
    public Scope global = new Scope();
    Scope thisScope = global;
    HashMap<String, Object> localSymbolTable = new HashMap<>();

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

    Object executeBlock(final Vector<Stmt> statements, Scope scope) {
        Scope previousScope = thisScope;
        thisScope = scope;
        for (Stmt stmt: statements) {
            execute(stmt);
        }
        thisScope = previousScope;
        return null;
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
        if (obj instanceof String) {
            return (String) obj;
        }
        if (obj instanceof Expr) {
            return obj.toString();
        }
        return null;
    }

    @Override
    public Object visitBinaryExpr(BinaryExpr expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);
        if (left instanceof String)
            left = Double.parseDouble((String) left);
        if (right instanceof String)
            right = Double.parseDouble((String) right);
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
    public Object visitVarExpr(VarExpr expr) {
        Object value = thisScope.getValue(expr.name);
        return value;
    }

    @Override
    public Object visitAssignExpr(AssignExpr expr) {
        Object value = evaluate(expr.value);
        Object id = thisScope.getValue(expr.name);
        //local scope没有该变量
        if (id == null) {
            //localSymbolTable.put(expr.name.word, value);
            thisScope.define(expr.name.word, value);
        } else {
            thisScope.assign(expr.name, value);
        }
        return value;
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
    public Object visitPrimaryExpr(PrimaryExpr expr) {
        return expr.value;
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
    public Object visitBlockStmt(BlockStmt stmt) {
        executeBlock(stmt.statements, thisScope);
        return null;
    }

    @Override
    public Object visitVarDeclStmt(VarDeclStmt stmt) {
        Object value = null;
        if (stmt.expr != null) {
            value = evaluate(stmt.expr);
            //localSymbolTable.put(stmt.name.word, stmt.expr);
            thisScope.define(stmt.name.word, value);
        }
        return value;
    }

    @Override
    public Object visitIfStmt(IfStmt stmt) {
        if (isTrue(stmt.condition)) {
            execute(stmt.thenStmt);
        } else {
            execute(stmt.elseStmt);
        }
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
            return ((Double) a).equals((Double) b);
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
