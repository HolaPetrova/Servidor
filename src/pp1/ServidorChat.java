package pp1;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServidorChat {
    private ServerSocket servidorSocket;
    private List<ComunicacionCliente> clientes;

    public ServidorChat(int puerto) {
        try {
           //servidorSocket = new ServerSocket(puerto);
           servidorSocket = new ServerSocket(puerto, 0, InetAddress.getByName("192.168.200.223"));//servidor ip

            clientes = new ArrayList<>();
            System.out.println("Servidor de Chat en ejecuciÃ³n en el puerto " + puerto);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ha fallado al inicializar");
            // AquÃ­ puedes agregar un mensaje especÃ­fico de error.
        }
    }
    
//
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
        int puerto = 8081; // Cambia esto al nÃºmero de puerto deseado
        ServidorChat servidorChat = new ServidorChat(puerto);
        servidorChat.iniciar();
    }
}
