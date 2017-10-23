import connection.BootstrapConnection;
import sys.Listener;
import sys.Parser;

public class Client3 {
    public static void main(String[] args) {
        String name = "Alwis";
        int port = 55558;

        BootstrapConnection bootstrapConnection = new BootstrapConnection();
        String response = bootstrapConnection.reg("localhost", port, name);
        Parser.parseResponse(response);

        if(response.equals("Timeout")) {
            // If client fails to register bootstrap server
            return;
        }

        Listener listener = new Listener(port);
        listener.start();
    }
}
