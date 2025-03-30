import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class Client2 {
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
    private static MessageReceptor message_receptor;
    private static MessageTransmitter message_transmitter;

    public MessageReceptor getMessageReceptor() {
        return message_receptor;
    }

    public MessageTransmitter getMessageTransmitter() {
        return message_transmitter;
    }

    public static void main(String[] args) {
        try {
            // Socket de communication
            Socket clientSocket = new Socket("localhost", 20000);

            // Connexion effectuee
            System.out.println("Client : Connexion effectuee");

            String pseudo="";
            boolean unique;

            DataInputStream ins = new DataInputStream(clientSocket.getInputStream());
            String[] pseudos= ins.readUTF().split("/");

            Scanner scanner = new Scanner(System.in);
            System.out.println("Entrez votre pseudo :");

            do {
                pseudo=scanner.nextLine();
                unique=true;
                for (String pseudo_autre : pseudos) {
                    if (pseudo.equals(pseudo_autre)) {
                        unique = false;
                    }
                    if (!unique){
                        System.out.println("Ce pseudo existe déjà, veuillez en entrer un autre :");
                    }
                }
            }while(!unique);

            OutputStream out = clientSocket.getOutputStream();
            out.write(pseudo.getBytes());

            message_receptor = new MessageReceptor(clientSocket);
            message_receptor.start();

            message_transmitter = new MessageTransmitter(clientSocket);
            message_transmitter.start();

        }
        catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur", e);
        }
    }

}