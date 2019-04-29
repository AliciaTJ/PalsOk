package e.alicia.pals.baseDatos;

import android.media.Image;
import android.net.Uri;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.FirebaseError;
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
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import e.alicia.pals.modelo.Plan;
import e.alicia.pals.modelo.Usuario;

public class DataBaseUsuario {

    DatabaseReference db;
    Boolean saved = null;
    Usuario miUsuario;
    List<Usuario> usuarios = new ArrayList<>();
    FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();


    public DataBaseUsuario(DatabaseReference db) {
        this.db = db;
        db.keepSynced(true);

    }


    //----------------------usuarios
    //guardar
    public Boolean save(Usuario usuario) {
        if (usuario == null) {
            saved = false;
        } else {

            try {

                db.child(usuario.getCodigo()).setValue(usuario);
                saved = true;
            } catch (DatabaseException e) {
                e.printStackTrace();
                saved = false;
            }

        }

        return saved;
    }

    public void modificar(Usuario usuario
                             ){

       db.child(usuario.getCodigo()).setValue(usuario);
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(usuario.getNombre())
                .setPhotoUri(Uri.parse(usuario.getFoto()))
                .build();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseUser.updateProfile(profileUpdates);
        firebaseUser.updateEmail(usuario.getEmail());
    }


    public List<Usuario> retrieve() {

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return usuarios;
    }

    private List<Usuario> fetchData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Usuario usuario = ds.getValue(Usuario.class);
            usuarios.add(usuario);


        }
        return usuarios;

    }


    public Usuario buscar(String codigo) {



        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usuarios.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                   miUsuario = dataSnapshot1.getValue(Usuario.class);
                    usuarios.add(miUsuario);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (miUsuario.getCodigo().equalsIgnoreCase(codigo)){
            return miUsuario;
        }
return null;
    }


    public void guardarFoto(Uri foto, Usuario usuario) {

        StorageReference storageReference = firebaseStorage.getReference("usuarios/");
        storageReference.putFile(foto);
        usuario.setFoto(foto.toString());
        save(usuario);


    }

    public Boolean comprobarNot(String usuario){
      String notificacion=  db.child(usuario).child("notificaciones").toString();
        System.out.println(notificacion);
      if (notificacion.equalsIgnoreCase("true")){
          return true;
      }else{
          return false;
      }

    }


}

