package e.alicia.pals.modelo;

import java.util.List;

public class Plan {
    private String nombre;
    private String informacion;
    private String fecha;
    private String maxusuarios;
    private String lugar;
    private String usuariocreador;
    private String codigo;
    private List<String> usuariosapuntados;
    String tipo;

    public Plan() {

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

    public String getMaxusuarios() {
        return maxusuarios;
    }

    public void setMaxusuarios(String maxusuarios) {
        this.maxusuarios = maxusuarios;
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
        this.maxusuarios = maximo;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }




}
