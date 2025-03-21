import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Serveur {
    private static final Logger LOGGER = Logger.getLogger(Serveur.class.getName());

    public static void main(String[] args) {
        try{
            ServerSocket serverSocket = new ServerSocket(20000);
            while (true) {
                // Attente de connexion
                System.out.println("Serveur : Attente de connexion");
                Socket clientSocket = serverSocket.accept();

                // Connexion acceptée
                System.out.println("Serveur : Connexion acceptee");

                clientSocket.close();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur", e);
        }
    }
}
