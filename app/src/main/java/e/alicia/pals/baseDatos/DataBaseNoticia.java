package e.alicia.pals.baseDatos;


import android.net.Uri;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import e.alicia.pals.modelo.Noticia;


public class DataBaseNoticia {

    DatabaseReference bdNoticia;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();


    public DataBaseNoticia() {
        this.bdNoticia = firebaseDatabase.getReference("noticias");

    }


    public void publicar(Noticia noticia) {

        bdNoticia.child(noticia.getCodigo()).setValue(noticia);
    }

    public void guardarFoto(Uri uri, final Noticia noticia) {

        StorageReference referencia = firebaseStorage.getReference().child("noticias/"+
                noticia.getCodigo());
        referencia.putFile(uri);
        publicar(noticia);
    }



    public void borrar(Noticia noticia){
        bdNoticia.child(noticia.getCodigo()).removeValue();
    }




    }


