package e.alicia.pals.modelo;

import com.google.android.libraries.places.api.model.Place;

import java.util.List;

/**
 * Clase modelo usuario
 */
public class Usuario {
    private String nombre;
    private String codigo;
    private String email;
    private List<Plan> planes;
    private Place lugar;
    private String foto;
    private String descripcion;
    private String fechanac;
    private Boolean notificacion;


    public Usuario() {

    }

    //getter y setter
    public String getFechanac() {
        return fechanac;
    }

    public void setFechanac(String fechanac) {
        this.fechanac = fechanac;
    }

    public String getFoto() {
        if (foto == null) {
            foto = "https://firebasestorage.googleapis.com/v0/b/pals-fae71.appspot.com/o/usuarios%2Fuser.png?alt=media&token=e928a126-f91b-40fb-a852-4164f15148ed";
            return foto;
        } else {
            return foto;
        }
    }

    public Boolean getNotificacion() {


        return notificacion;
    }

    public void setNotificacion(Boolean notificacion) {
        this.notificacion = notificacion;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    public Place getLugar() {
        return lugar;
    }

    public void setLugar(Place lugar) {
        this.lugar = lugar;
    }

    public List<Plan> getPlanes() {
        return planes;
    }

    public void setPlanes(List<Plan> planes) {
        this.planes = planes;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
