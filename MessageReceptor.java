import java.io.InputStream;
import java.net.Socket;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

//Thread qui gère la réception des messages d'un client
public class MessageReceptor extends Thread {
    private Socket client;

    public MessageReceptor(Socket client) { this.client = client; }

    @Override
    public void run(){
        try {
            InputStream in = client.getInputStream();
            byte[] stockage_message = new byte[1024];
            String message;
            int longueur_message;

            //tant que le thread n'est pas interrompu
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    longueur_message = in.read(stockage_message);
                    if (longueur_message != -1) { //si la connexion avec le serveur est toujours établie
                        message = new String(stockage_message, 0, longueur_message);
                        System.out.println(message);
                    }
                    else {
                        interrupt();
                    }
                } catch (IOException e) {
                    System.out.println("Le serveur s'est arrêté.");
                    interrupt();
                }
            }
        }
        catch (IOException e) {
            Logger.getLogger(MessageReceptor.class.getName()).log(Level.SEVERE, "Erreur", e);
        }finally {
            try {
                //on libère le socket
                client.close();
            }catch(IOException e){
                Logger.getLogger(MessageGestion.class.getName()).log(Level.SEVERE, "Erreur", e);
            }
            //on libère le thread
            interrupt();
        }
    }
}
