import java.util.HashMap;

/**
 * 作用域类
 */
public class Scope {
    HashMap<String, Object> symbolTable = new HashMap<>();; //符号表
    private Scope enclosing = null;

    Scope() {
        enclosing = null;
    }

    Scope(Scope enclosing) {
        this.enclosing = enclosing;
    }

    void define(final String name, final Object value) {
        symbolTable.put(name, value);
    }

    Scope parentScope(int distance) {
        Scope scope = this;
        while (distance > 0) {
            scope = scope.enclosing;
            distance--;
        }
        return scope;
    }

    void assign(Token name, Object value) {
        Object var = symbolTable.get(name.word);
        if (var != null) {
            symbolTable.put(name.word, value);
            return;
        }
        if (enclosing != null) {
            enclosing.assign(name, value);
        }
        new Error(name, "undefined variable " + name.word);
    }

    void assignHelp(int distance, final Token name, Object value) {
        parentScope(distance).symbolTable.replace(name.word, value);
    }

    Object getValue(final Token name) {
        Object value = symbolTable.get(name.word);
        if (value != null) {
            return value;
        }
        new Error(name, "undefined variable " + name.word);
        return null;
    }

    Object getValueHelp(int distance, final Token name) {
        return parentScope(distance).symbolTable.get(name.word);
    }
}
