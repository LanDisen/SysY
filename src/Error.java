public class Error {
    Error(String message) {
        report(message);
        System.exit(-1);
    }

    void report(String message) {
        System.out.println("Error: " + message);
    }
}
