package e.alicia.pals.modelo;

import java.util.Date;

/**
 * Clase modelo mensaje
 */
public class Mensaje {

    //atributos del mensaje
    String mensaje;
    String usuario;
    Long fechaHora;

    /**
     * Conostructor vacio
     */
    public Mensaje() {

    }

    /**
     * Consutrctor mensaje
     * @param mensaje string
     * @param usuario string
     * @param fechaHora long
     */
    public Mensaje(String mensaje, String usuario, Long fechaHora) {
        this.mensaje = mensaje;
        this.usuario = usuario;
        this.fechaHora = new Date().getTime();
    }


    //getter y setter
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Long getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Long fechaHora) {
        this.fechaHora = fechaHora;
    }
}
