package ServidorChat;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class ServidorChat {
    private ServerSocket servidorSocket;
    private List<ComunicacionCliente> clientes;

    public ServidorChat(InetAddress direccionIP, int puerto) {
        try {
            if (direccionIP != null) {
                servidorSocket = new ServerSocket(puerto, 0, direccionIP);
            } else {
                servidorSocket = new ServerSocket(puerto);
            }
            clientes = new ArrayList<>();
            System.out.println("Servidor de Chat en ejecuciÃ³n en la direcciÃ³n IP " + direccionIP.getHostAddress() + " y el puerto " + puerto);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ha fallado al inicializar el servidor");
            // AquÃ­ puedes agregar un mensaje especÃ­fico de error.
        }
    }

    public void iniciar() {
        while (true) {
            try {
                Socket socketCliente = servidorSocket.accept();
                System.out.println("Nuevo cliente conectado: " + socketCliente.getInetAddress().getHostAddress());

                ComunicacionCliente manejadorCliente = new ComunicacionCliente(socketCliente, this);
                clientes.add(manejadorCliente);

                new Thread(manejadorCliente).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void transmitirMensaje(String remitente, String mensaje) {
        for (ComunicacionCliente cliente : clientes) {
            cliente.enviarMensaje(remitente, mensaje);
        }
    }

    public synchronized void removerCliente(ComunicacionCliente cliente) {
        clientes.remove(cliente);
    }

    public synchronized List<String> getUsuariosConectados() {
        List<String> usuariosConectados = new ArrayList<>();
        for (ComunicacionCliente cliente : clientes) {
            usuariosConectados.add(cliente.getApodo());
        }
        return usuariosConectados;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java ServidorChat <direcciÃ³n_IP> <puerto>");
            return;
        }

        InetAddress direccionIP = null;
        try {
            direccionIP = InetAddress.getByName(args[0]);
        } catch (IOException e) {
            System.out.println("La direcciÃ³n IP especificada no es vÃ¡lida.");
            return;
        }
        
        int puerto = Integer.parseInt(args[1]);

        ServidorChat servidorChat = new ServidorChat(direccionIP, puerto);
        servidorChat.iniciar();
    }
}
