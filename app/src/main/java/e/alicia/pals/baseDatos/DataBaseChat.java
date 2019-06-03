package e.alicia.pals.baseDatos;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import e.alicia.pals.modelo.Mensaje;

/**
 * Clase controladora del elemento chat. Actualiza, borra o crea elementos chat
 * en la base de datos de firebase
 */
public class DataBaseChat {

    DatabaseReference bdChat;
    List<Mensaje> mensajes = new ArrayList<>();
    DatabaseReference dbUsuarios;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();


    public DataBaseChat() {
        this.bdChat = firebaseDatabase.getReference("chats");
        dbUsuarios = firebaseDatabase.getReference("usuarios");
    }


    /**
     * Metodo que muestra los mensajes. Filtra por codigo de plan.
     * @param codigo
     * @return
     */
    public List<Mensaje> mostrar(String codigo) {
        bdChat.child("chats").child(codigo).addChildEventListener(new ChildEventListener() {
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

        return mensajes;
    }


    private void fetchData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Mensaje mensaje = ds.getValue(Mensaje.class);
            mensajes.add(mensaje);


        }


    }

    /**
     * Metodo que envia un mensaje.
     * @param mensaje
     * @param codigo
     * @return
     */
    public Boolean enviar(Mensaje mensaje, String codigo
    ) {
        Boolean guardado=null;
        if (mensaje == null) {
            guardado = false;
        } else {

            try {
                bdChat.child(codigo).push().setValue(mensaje);
                guardado = true;
            } catch (DatabaseException e) {
                e.printStackTrace();
                guardado = false;
            }

        }

        return guardado;
    }

}