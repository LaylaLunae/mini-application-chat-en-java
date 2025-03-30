import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;
import java.util.Map;


public class Serveur {
    private static final Logger LOGGER = Logger.getLogger(Serveur.class.getName());
    public static HashMap<Socket, String> liste_clients = new HashMap<>();

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
            ServerSocket serverSocket = new ServerSocket(20000);

            while(true) {
                Socket clientSocket = serverSocket.accept();

                InputStream in = clientSocket.getInputStream();
                byte[] lecture_pseudo = new byte[1024];
                int longueur_pseudo = in.read(lecture_pseudo);
                String pseudo = new String(lecture_pseudo, 0, longueur_pseudo);

                //mettre verification unicité pseudo

                liste_clients.put(clientSocket, pseudo);

                String message = "    " + pseudo + " a rejoint la conversation";
                diffuserMessage(message);

                MessageGestion message_gestion = new MessageGestion(clientSocket, pseudo);
                message_gestion.start();
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur", e);
        }
    }
}
