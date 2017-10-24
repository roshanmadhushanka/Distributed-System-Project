import connection.BootstrapConnection;
import sys.Listener;
import sys.Parser;

public class Client1 {
    public static void main(String[] args) {
        String name = "Roshan";
        int port = 55556;

        BootstrapConnection bootstrapConnection = new BootstrapConnection();
        String response = bootstrapConnection.reg("localhost", port, name);
        Parser.parseResponse(response);

        if(response.equals("Timeout")) {
            // If client fails to register bootstrap server
            return;
        }

        Listener listener = new Listener(port);
        listener.start();

        bootstrapConnection.unreg("localhost", port, name);
        listener.close();


    }
}
