import connection.BootstrapConnection;
import sys.Listener;
import sys.Parser;

public class Client2 {
    public static void main(String[] args) {
        String name = "Madhushanka";
        int port = 55557;

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
