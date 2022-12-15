import java.util.HashMap;
import java.util.Vector;

@SuppressWarnings({"all"})
public class Interpreter implements ExprVisitor, StmtVisitor{
    public Scope global = new Scope();
    Scope thisScope = global;
    HashMap<String, Object> localSymbolTable = new HashMap<>();
    Vector<Stmt> statements;
    static int temp = 1; //临时变量
    int line = 0;
    Vector<Quadruple> codes = new Vector<>();

    Interpreter(Vector<Stmt> statements) {
        this.statements = statements;
        interpret(statements);
        printCodes(); //打印中间代码
    }

    void interpret(final Vector<Stmt> statements) {
        for (Stmt statement: statements) {
            execute(statement);
        }
    }

    Object execute(final Stmt stmt) {
        if (stmt == null) return "None";
        return stmt.accept(this);
    }

    Object evaluate(final Expr expr) {
        if (expr == null) return "None";
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
            if ((Double) obj - ((Double) obj).intValue() < 1e-8) {
                return String.valueOf(((Double) obj).intValue());
            }
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
        if (left instanceof String) {
            try {
                left = Double.parseDouble((String) left);
            } catch (NumberFormatException e) {
                new Error(expr.op, "'" + expr.op.word +"can not operate on string and number");
            }
        }
        if (right instanceof String) {
            try {
                right = Double.parseDouble((String) right);
            } catch (NumberFormatException e) {
                new Error(expr.op, "'" + expr.op.word + "' can not operate on string and number");
            }
        }
        Token op = expr.op;
        if (temp == 1) {
            gen(++line, op.word, stringify(left), stringify(right), "t" + String.valueOf(temp++)); //中间代码
        }
        else {
            gen(++line, op.word, "t" + String.valueOf(temp-1), stringify(right), "t" + String.valueOf(temp++)); //中间代码
        }
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
                double divideNum = (double) right;
                if (divideNum == 0) {
                    new Error(expr.op, "can't divided by zero");
                }
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
        //System.out.println(inCode(++line, ":=", "-", ));
        //local scope没有该变量
        if (id == null) {
            //localSymbolTable.put(expr.name.word, value);
            thisScope.define(expr.name.word, value);
        } else {
            thisScope.assign(expr.name, value);
        }
        gen(++line, ":=", stringify(value), "_", expr.name.word);
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
                gen(++line, "-", "_", "_", stringify(right));
                return -(Double) right;
            }
            case EXCLAMATION -> {
                gen(++line, "not", "_", "_", stringify(right));
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
        executeBlock(stmt.statements, new Scope(thisScope));
        return null;
    }

    @Override
    public Object visitVarDeclStmt(VarDeclStmt stmt) {
        Object value = null;
        if (stmt.expr != null) {
            value = evaluate(stmt.expr);
            //localSymbolTable.put(stmt.name.word, stmt.expr);
        }
        thisScope.define(stmt.name.word, value);
        gen(++line, ":=", "-", stringify(value), stmt.name.word);
        return value;
    }

    @Override
    public Object visitIfStmt(IfStmt stmt) {
        int flag = 1;
        if (isTrue(evaluate(stmt.condition))) flag = 0;
        if (flag == 1) {
            execute(stmt.thenStmt);
        } else {
            execute(stmt.elseStmt);
        }
        return null;
    }

    @Override
    public Object visitWhileStmt(WhileStmt stmt) {
        while (isTrue(evaluate(stmt.condition))) {
            execute(stmt.body);
        }
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

    /**
     * 中间代码，四元式形式
     */
    void emit(Quadruple code) {
        String str = "[";
        str += String.format("%4d", code.line);
        str += "] (";
        str += String.format("%4s", code.op) + ",";
        str += String.format("%4s", code.arg1) + ",";
        str += String.format("%4s", code.arg2) + ",";
        str += String.format("%4s", code.result);
        str += ")";
        System.out.println(str);
    }

    void gen(int line, String op, String arg1, String arg2, String result) {
        codes.add(new Quadruple(line, op, arg1, arg2, result));
    }

    void printCodes() {
        for (Quadruple code: codes)
            emit(code);
    }
}

//四元式
class Quadruple {
    public Quadruple(int line, String op, String arg1, String arg2, String result) {
        this.line = line;
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public int line;
    public String op;
    public String arg1;
    public String arg2;
    public String result;
}
