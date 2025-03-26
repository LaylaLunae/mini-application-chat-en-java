import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;

public class MessageGestion extends Thread {
    private Socket client;
    private String pseudo;

    public MessageGestion(Socket client, String pseudo) {
        this.client = client;
        this.pseudo = pseudo;
    }

    @Override
    public void run() {
        try {
            InputStream in = client.getInputStream();
            byte[] stockage_message = new byte[1024];
            String message_recu;
            String message_a_envoye;
            int longueur_message;
            Boolean client_connecte = true;

            while (client_connecte) {
                longueur_message = in.read(stockage_message);
                message_recu = new String(stockage_message, 0, longueur_message);

                if (message_recu.equalsIgnoreCase("exit")) {
                    Serveur.liste_clients.remove(client);
                    String exitMessage = "   " + pseudo + " a quitté la conversation";
                    Serveur.diffuserMessage(exitMessage);
                    client_connecte = false;
                }
                else {
                    message_a_envoye = "    " + pseudo + " a dit : " + message_recu;
                    Serveur.diffuserMessage(message_a_envoye);
                }
            }

            client.close();
        } catch (IOException e) {
            Logger.getLogger(MessageGestion.class.getName()).log(Level.SEVERE, "Erreur", e);
        }
    }
}
