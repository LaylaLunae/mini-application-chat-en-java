import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

public class MessageTransmitter extends Thread {
    private Socket client;

    public MessageTransmitter(Socket client) { this.client = client; }

    @Override
    public void run(){
        try {
            OutputStream out = client.getOutputStream();
            Scanner sc = new Scanner(System.in);
            String message;

            while (!Thread.currentThread().isInterrupted()) {
                message = sc.nextLine();
                out.write(message.getBytes());
                if (message.equalsIgnoreCase("exit")) {
                    client.close();
                    System.exit(0);
                    break;
                }
            }
        }
        catch (IOException e) {
            Logger.getLogger(MessageTransmitter.class.getName()).log(Level.SEVERE, "Erreur", e);
        }
        finally {
            try {
                client.close();
            }catch(IOException e){
                Logger.getLogger(MessageGestion.class.getName()).log(Level.SEVERE, "Erreur", e);
            }
            interrupt();
        }
    }
}
