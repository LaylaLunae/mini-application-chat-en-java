import java.io.InputStream;
import java.net.Socket;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

            while (true) {
                longueur_message = in.read(stockage_message);
                message = new String(stockage_message, 0, longueur_message);
                System.out.println(message);
            }

        }
        catch (IOException e) {
            Logger.getLogger(MessageReceptor.class.getName()).log(Level.SEVERE, "Erreur", e);
        }
    }
}
