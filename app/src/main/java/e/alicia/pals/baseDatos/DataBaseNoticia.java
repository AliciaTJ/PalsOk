package e.alicia.pals.baseDatos;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import e.alicia.pals.modelo.Mensaje;
import e.alicia.pals.modelo.Noticia;
import e.alicia.pals.modelo.Usuario;

public class DataBaseNoticia {

    DatabaseReference bdNoticia;
    Boolean guardar = null;
    List<Noticia> noticias = new ArrayList<>();
    Mensaje plan;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    Noticia noticia;


    public DataBaseNoticia() {
        this.bdNoticia = firebaseDatabase.getReference("noticias");

    }


    public void publicar(Noticia noticia) {

        bdNoticia.child(noticia.getCodigo()).setValue(noticia);
    }

    public void guardarFoto(Uri foto, final Noticia noticia) {
        final StorageReference referencia = firebaseStorage.getReference("noticias/")
                .child(noticia.getCodigo());

        referencia.putFile(foto);

                noticia.setImagen(referencia.child((noticia.getCodigo())).getDownloadUrl().toString());


    }



    public void borrar(Noticia noticia){
        bdNoticia.child(noticia.getCodigo()).removeValue();
    }




    }


