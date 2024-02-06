package pp1;

import java.io.Serializable;

public class MensajeChat implements Serializable {
    private static final long serialVersionUID = 1L;

    private String remitente;
    private String mensaje;

    public MensajeChat(String remitente, String mensaje) {
        this.remitente = remitente;
        this.mensaje = mensaje;
    }

    public String getRemitente() {
        return remitente;
    }

    public String getMensaje() {
        return mensaje;
    }
    public String toJson() {
        return "{\"remitente\":\"" + remitente + "\",\"mensaje\":\"" + mensaje + "\"}";
    }
}
