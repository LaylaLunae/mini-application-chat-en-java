import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Thread {
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    public static void main(String[] args) {
        try {
            // Socket de communication
            Socket clientSocket = new Socket("localhost", 20000);
            // Connexion effectuee
            System.out.println("Client : Connexion effectuee");

            clientSocket.close();
        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur", e);
        }
    }

}