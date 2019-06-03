package e.alicia.pals.modelo;

/**
 * Clase modelo noticia
 */
public class Noticia {

    private String titular;
    private String cuerpo;
    private String codigo;

    /**
     * Constructor vacio noticia
     */
    public Noticia() {

    }

    //getter y setter
    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

}
