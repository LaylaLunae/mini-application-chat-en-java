import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client2 {
    private static final Logger LOGGER = Logger.getLogger(Client2.class.getName());

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

            MessageReceptor message_receptor = new MessageReceptor(clientSocket);
            message_receptor.start();

            MessageTransmitter message_transmitter = new MessageTransmitter(clientSocket);
            message_transmitter.start();

        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur", e);
        }
    }

}