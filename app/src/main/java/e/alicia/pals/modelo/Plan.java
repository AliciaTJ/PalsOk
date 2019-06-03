package e.alicia.pals.modelo;

import java.util.List;

/**
 * Clase modelo plan
 */
public class Plan {
    private String nombre;
    private String informacion;
    private String fecha;
    private String estado;
    private String lugar;
    private String usuariocreador;
    private String codigo;
    private List<String> usuariosapuntados;
    private String tipo;
    private String provincia;

    public Plan() {

    }
    public Plan (String nombre){
        this.nombre=nombre;
    }


    //getter y setter
    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public List<String> getUsuariosapuntados() {
        return usuariosapuntados;
    }

    public void setUsuariosapuntados(List<String> usuariosapuntados) {
        this.usuariosapuntados = usuariosapuntados;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String maxusuarios) {
        this.estado = maxusuarios;
    }

    public String getUsuariocreador() {
        return usuariocreador;
    }

    public void setUsuariocreador(String usuariocreador) {
        this.usuariocreador = usuariocreador;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getInformacion() {
        return informacion;
    }

    public void setInformacion(String informacion) {
        this.informacion = informacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setMaximo(String maximo) {
        this.estado = maximo;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }




}
