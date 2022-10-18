import org.junit.jupiter.api.Test;

import java.io.File;

public class SysY {
    public static void main(String[] args) {
        run(new File("res\\expression.txt"));
    }

    static void run(File file) {
        Lexer lexer = new Lexer(file);
        Parser parser = new Parser(lexer.tokens);
        Interpreter interpreter = new Interpreter(parser.statements);
        //System.out.println("SysY运行结束");
    }
}
