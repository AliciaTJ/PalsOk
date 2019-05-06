package e.alicia.pals.baseDatos;


import android.net.Uri;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import e.alicia.pals.modelo.Noticia;

/**
 * Clase controladora de la base de datos noticia
 */

public class DataBaseNoticia {

    DatabaseReference bdNoticia;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();


    public DataBaseNoticia() {
        this.bdNoticia = firebaseDatabase.getReference("noticias");

    }


    /**
     * Metodo que publica una noticia con su numero de codigo como child
     * @param noticia
     */
    public void publicar(Noticia noticia) {
        bdNoticia.child(noticia.getCodigo()).setValue(noticia);
    }

    /**
     * Metodo que guarda la imagen de la noticia en firetorage con el numero de codigo
     * de la noticia como nombre. Posteriormente se accedera a esta imagen a traves del
     * codigo de la noticia a cargar
     * @param uri
     * @param noticia
     */
    public void guardarFoto(Uri uri, final Noticia noticia) {

        StorageReference referencia = firebaseStorage.getReference().child("noticias/" +
                noticia.getCodigo());
        referencia.putFile(uri);
        publicar(noticia);
    }


    /**
     * Metodo que elimina una noticia de la base de datos de firebase
     * @param noticia
     */
    public void borrar(Noticia noticia) {
        bdNoticia.child(noticia.getCodigo()).removeValue();
    }


}


