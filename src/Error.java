public class Error {
    Error(final Token token, String message) {
        report(token.line, token.column, message);
        System.exit(-1);
    }

    Error(final int line, final int column, String message) {
        report(line, column, message);
        System.exit(-1);
    }

    void report(int line, int column, String message) {
        System.out.println("\033[31m" + "Error: " + message
                        + " [line:" + line + ", column:" + column + "]");
    }
}
