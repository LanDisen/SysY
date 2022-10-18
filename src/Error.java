public class Error {
    Error(final Token token, String message) {
        report(token.line, token.column, message);
        System.exit(-1);
    }

    void report(int line, int column, String message) {
        System.out.println("Error: " + message
                        + " [line:" + line + ", column:" + column + "]");
    }
}
