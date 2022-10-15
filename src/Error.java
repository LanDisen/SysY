public class Error {
    Error(final Token token, String message) {
        report(token.line, message);
        //System.exit(-1);
    }

    void report(int line, String message) {
        System.out.println("Error: " + message
                        + " [ line:" + line + " ]");
    }
}
