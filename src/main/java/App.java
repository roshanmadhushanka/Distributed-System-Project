import connection.BootstrapConnection;
import sys.Listener;
import sys.Parser;

public class App {
    public static void main(String[] args) {
        int port = 12346;
        String name = "m";
        // Connect to bootstrap server
        BootstrapConnection bootstrapConnection = new BootstrapConnection();
        String response = bootstrapConnection.reg("localhost", port, name);
        Parser.parseResponse(response);

        Listener listener = new Listener(port);
        listener.start();
    }
}
