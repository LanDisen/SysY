import java.io.File;

public class SysY {
    public static void main(String[] args) {
        run(new File("res\\expression.txt"));
    }

    static void run(File file) {
        Lexer lexer = new Lexer(file);
        Parser parser = new Parser(lexer.tokens);
        //System.out.println("SysY运行结束");
    }
}
