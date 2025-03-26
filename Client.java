import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class Client {
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    public static void main(String[] args) {
        try {
            // Socket de communication
            Socket clientSocket = new Socket("localhost", 20000);

            // Connexion effectuee
            System.out.println("Client : Connexion effectuee");

            Scanner scanner = new Scanner(System.in);
            System.out.println("Entrer votre pseudo :");
            String pseudo = scanner.nextLine();

            OutputStream out = clientSocket.getOutputStream();
            out.write(pseudo.getBytes());

            MessageReceptor messageReceptor = new MessageReceptor(clientSocket);
            messageReceptor.start();

            MessageTransmitter messageTransmitter = new MessageTransmitter(clientSocket);
            messageTransmitter.start();

        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur", e);
        }
    }

}