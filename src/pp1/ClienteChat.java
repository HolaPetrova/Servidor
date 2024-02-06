package pp1;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteChat {

    public static void main(String[] args) {
        String servidorIP = "localhost"; //ip servidor 
        String servidorIPLocal = "192.168.200.41";// Puedes cambiar esto a la IP del servidor si estÃ¡ en otra mÃ¡quina
        
        int puertoServidor = 8081; // AsegÃºrate de que coincida con el puerto del servidor

        try {
            Socket socketCliente = new Socket(servidorIPLocal, puertoServidor);
            ObjectOutputStream salidaStream = new ObjectOutputStream(socketCliente.getOutputStream());
            ObjectInputStream entradaStream = new ObjectInputStream(socketCliente.getInputStream());

            Scanner scanner = new Scanner(System.in);

            System.out.print("Ingresa tu apodo: ");
            String apodo = scanner.nextLine();

            // Registro de usuario
            MensajeChat mensajeRegistro = new MensajeChat(apodo, "Se ha unido al chat.");
            salidaStream.writeObject(mensajeRegistro);

            new Thread(() -> {
                try {
                    while (true) {
                        // Escuchar mensajes del servidor
                        MensajeChat mensajeChat = (MensajeChat) entradaStream.readObject();
                        System.out.println(mensajeChat.getRemitente() + ": " + mensajeChat.getMensaje());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();

            // Enviar mensajes al servidor
            while (true) {
                String mensaje = scanner.nextLine();
                MensajeChat mensajeChat = new MensajeChat(apodo, mensaje);
                salidaStream.writeObject(mensajeChat);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
