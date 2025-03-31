import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

//La classe Client permet à l'utilisateur d'échanger avec d'autres clients
public class Client {
    private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

    public static void main(String[] args) {
        try {
            // Socket de communication
            Socket clientSocket = new Socket("localhost", 20000);

            // Connexion effectuee
            System.out.println("Client : Connexion effectuee");

            String pseudo="";
            boolean unique;

            //Recuperation de la chaine contenant les pseudos des clients connectés dans un tableau de chaines
            DataInputStream ins = new DataInputStream(clientSocket.getInputStream());
            String[] pseudos= ins.readUTF().split("/");

            //Lecture du pseudo
            Scanner scanner = new Scanner(System.in);
            System.out.println("Entrez votre pseudo :");

            do {
                pseudo=scanner.nextLine();
                unique=true;
                for (String pseudo_autre : pseudos) { //on teste si le pseudo entré est le même pour chaque pseudo du tableau 
                    if (pseudo.equals(pseudo_autre)) {
                        unique = false;
                    }
                    if (!unique){ 
                        System.out.println("Ce pseudo existe déjà, veuillez en entrer un autre :");
                    }
                }
            }while(!unique); // tant que le pseudo existe deja

            //on envoie le pseudo au serveur une fois correct
            OutputStream out = clientSocket.getOutputStream();
            out.write(pseudo.getBytes());

            //Lancement des threads de reception et de transmission des messages 
            MessageReceptor message_receptor = new MessageReceptor(clientSocket);
            message_receptor.start();

            MessageTransmitter message_transmitter = new MessageTransmitter(clientSocket);
            message_transmitter.start();

        }
        catch (IOException e) {
            Logger.getLogger(MessageGestion.class.getName()).log(Level.SEVERE, "Erreur", e);
        }
    }

}