import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;

//La classe Serveur est celle qui gère les connexions des clients et leurs messages
public class Serveur {
    //gestion des erreurs
    private static final Logger LOGGER = Logger.getLogger(Serveur.class.getName());
    //Liste des clients
    public static HashMap<Socket, String> liste_clients = new HashMap<>();

    //Fonction qui renvoie la liste des pseudos des clients
    public static String getListePseudos(){
        String chaine="";
        for(String pseudo : liste_clients.values()) {
            chaine += pseudo+"/";
        }
        chaine+="";
        return chaine;
    }

    //Fonction qui envoie les messages à tous les clients de la liste
    public static void diffuserMessage(String message) {
        for (Socket client : liste_clients.keySet()) {
            try {
                OutputStream out = client.getOutputStream();
                out.write(message.getBytes());
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Erreur lors de l'envoi du message à un client", e);
            }
        }
    }

    public static void main(String[] args) {
        try{
            //Connexion au port 20000
            ServerSocket serverSocket = new ServerSocket(20000);

            //Possibilité de connexion des clients infinie
            while(true) {
                //Connexion avec le client
                Socket clientSocket = serverSocket.accept();

                DataOutputStream outs = new DataOutputStream(clientSocket.getOutputStream());
                outs.writeUTF(getListePseudos());

                //Lecture du pseudo
                InputStream in = clientSocket.getInputStream();
                byte[] lecture_pseudo = new byte[1024];
                int longueur_pseudo = in.read(lecture_pseudo);
                String pseudo = new String(lecture_pseudo, 0, longueur_pseudo);
                liste_clients.put(clientSocket, pseudo);

                //Prévenir les autres clients de la venue du dernier client
                String message = "    " + pseudo + " a rejoint la conversation";
                diffuserMessage(message);

                //Création du thread
                MessageGestion message_gestion = new MessageGestion(clientSocket, pseudo);
                message_gestion.start();
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur", e);
        }
    }
}
