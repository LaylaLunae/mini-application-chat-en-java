import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;

//Thread qui gère un client et ses messages
public class MessageGestion extends Thread {
    private Socket client;
    private String pseudo;

    public MessageGestion(Socket client, String pseudo) {
        this.client = client;
        this.pseudo = pseudo;
    }

    //Fonction qui assure la bonne déconnexion du client
    public void deconnecterClient() {
        try {
            //Suppression du client de la liste
            Serveur.liste_clients.remove(client);
            String exitMessage = "    " + pseudo + " a quitté la conversation";
            Serveur.diffuserMessage(exitMessage);

            //Fermeture des entrées/sorties
            client.getOutputStream().close();
            client.getInputStream().close();
            client.close();
        }catch(IOException e){
            Logger.getLogger(MessageGestion.class.getName()).log(Level.SEVERE, "Erreur", e);
        }
    }

    //Fonction qui est lancée en boucle et qui gère les messages
    @Override
    public void run() {
        try {
            InputStream in = client.getInputStream();
            byte[] stockage_message = new byte[1024];
            String message_recu;
            String message_a_envoye;
            int longueur_message;
            Boolean client_connecte = true;

            //Tant que le client est connecté, on diffuse son message
            while (client_connecte) {
                longueur_message = in.read(stockage_message);
                if (longueur_message==-1) {
                    client_connecte = false;
                    deconnecterClient();
                }
                else{
                    message_recu = new String(stockage_message, 0, longueur_message);
                    //Si le message est "exit", on le déconnecte du serveur
                    if (message_recu.equalsIgnoreCase("exit")) {
                        client_connecte = false;
                        deconnecterClient();
                    } else {
                        message_a_envoye = "    " + pseudo + " a dit : " + message_recu;
                        Serveur.diffuserMessage(message_a_envoye);
                    }
                }
            }
        } catch (IOException e) {
            Logger.getLogger(MessageGestion.class.getName()).log(Level.SEVERE, "Erreur", e);
            deconnecterClient();
        }
        finally{
            interrupt();
        }
    }
}
