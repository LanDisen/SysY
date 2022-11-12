import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Scanner;

public class SysY {
    public static void main(String[] args) {
        run(new File("test\\if.txt"));
        //runFile();
    }

    static void run(File file) {
        Lexer lexer = new Lexer(file);
        Parser parser = new Parser(lexer.tokens);
        Interpreter interpreter = new Interpreter(parser.statements);
    }

    static void runFile() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input the filename you need to run：");
        if (scanner.hasNext()) {
            String filepath = scanner.next();
            run(new File(filepath));
        }
    }
}
