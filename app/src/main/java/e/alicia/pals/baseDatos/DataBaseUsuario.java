package e.alicia.pals.baseDatos;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import e.alicia.pals.modelo.Usuario;

/**
 * Clase controladora que accede a la base de datos de usuarios de firebase
 */
public class DataBaseUsuario {

    DatabaseReference db;
    Boolean guardado = null;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


    public DataBaseUsuario(DatabaseReference db) {
        this.db = db;
        db.keepSynced(true);

    }

    /**
     * Guarda un nuevo usuario en la base de datos.
     *
     * @param usuario
     * @return boolean
     */
    public Boolean guardar(Usuario usuario) {
        if (usuario == null) {
            guardado = false;
        } else {

            try {

                db.child(usuario.getCodigo()).setValue(usuario);
                guardado = true;
            } catch (DatabaseException e) {
                e.printStackTrace();
                guardado = false;
            }

        }

        return guardado;
    }

    public boolean comprobarNotificacionUsuario(String usuario) {
     /*   String notificacion = db.child(usuario).child("notificaciones");
        System.out.println(notificacion);
        if (notificacion.equalsIgnoreCase("true"))
            return true;
        else*/
            return false;
    }


    /**
     * Modifica la informacion de un usuario. Tambien modifica, no solo el usuario que
     * esta en mi base de datos, si no el usuario que se crea en Database auth de google.
     * Esto se hace para que no haya conflicto entre los datos de ambos sitios.
     *
     * @param usuario
     */
    public void modificar(Usuario usuario
    ) {

        db.child(usuario.getCodigo()).setValue(usuario);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(usuario.getNombre())
                .setPhotoUri(Uri.parse(usuario.getFoto()))
                .build();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseUser.updateProfile(profileUpdates);
        firebaseUser.updateEmail(usuario.getEmail());
    }


    /**
     * Metodo que guarda la fto del usuario en firestorage con el codigo del usuario
     *
     * @param foto
     * @param usuario
     */
    public void guardarFoto(Uri foto, final Usuario usuario) {

        StorageReference storageReference = firebaseStorage.getReference("usuarios/" + usuario.getCodigo());
        storageReference.putFile(foto);
        storageReference

                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

            @Override
            public void onSuccess(Uri uri) {
                usuario.setFoto(uri.toString());

                modificar(usuario);
                guardar(usuario);
            }
        });
    }

    /**
     * Comprueba si el usuario tiene notificaciones activas
     *
     * @param usuario
     * @return boolean
     */
    public Boolean comprobarNot(String usuario) {
        String notificacion = db.child(usuario).child("notificaciones").toString();

        if (notificacion.equalsIgnoreCase("true")) {
            return true;
        } else {
            return false;
        }

    }
}




