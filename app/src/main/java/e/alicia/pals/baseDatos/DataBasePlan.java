package e.alicia.pals.baseDatos;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import e.alicia.pals.modelo.Plan;
import e.alicia.pals.modelo.Usuario;

/**
 * Clase controladora de la base de datos de planes
 */
public class DataBasePlan {

    DatabaseReference dbPlanes;
    Boolean guardado = null;
    DatabaseReference dbUsuarios;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();



    public DataBasePlan(DatabaseReference dbPlanes) {
        this.dbPlanes = dbPlanes;
        dbUsuarios = firebaseDatabase.getReference("usuarios");
    }


    /**
     * Metodo que guarda un plan en la base de datos.
     * @param plan
     * @return boolean
     */
    public Boolean guardar(Plan plan
    ) {
        if (plan == null) {
            guardado = false;
        } else {

            try {
                dbPlanes.child(plan.getCodigo()).setValue(plan);
                guardado = true;
            } catch (DatabaseException e) {
                e.printStackTrace();
                guardado = false;
            }

        }

        return guardado;
    }

    /**
     * Metodo que añade a un usuarios al arraylist de usuarios apuntados a cada plan.
     * Tambien establece a true las notificaciones del usuario creador
     * para que sea avisado de que tiene nuevos usuarios.
     * @param plan
     * @param codigoUsuario
     */
    public void apuntarseAlPlan(Plan plan, String codigoUsuario){
        plan.getUsuariosapuntados().add(codigoUsuario);
        dbPlanes.child(plan.getCodigo()).setValue(plan);
        dbUsuarios.child(plan.getUsuariocreador()).child("notificaciones").setValue(true);

    }

    /**
     * Metodo que mueve el estado de un plan a cerrado. Se quedara en la base de datos
     * hasta que sea borrado por un administrador.
     * Tambien borra el historial de chats del plan.
     * @param plan
     * @return
     */

    public boolean borrarPlan(Plan plan){
        plan.setEstado("cerrado");
        dbPlanes.child(plan.getCodigo()).removeValue();
        dbPlanes.getDatabase().getReference().child("planescerrados").child(plan.getCodigo()).setValue(plan);
        dbPlanes.getDatabase().getReference().child("chats").child(plan.getCodigo()).removeValue();
        return true;


    }

    /**
     * Metodo que quita del arraylist de usuarios de un plan al usuario que lo quiera
     * abandonar.
     * @param plan
     * @param codigoUsuario
     */
    public void dejarPlan(Plan plan, String codigoUsuario){
        for (int i=0; i<plan.getUsuariosapuntados().size(); i++){
            if ( plan.getUsuariosapuntados().get(i).equalsIgnoreCase(codigoUsuario)){
                plan.getUsuariosapuntados().remove(i);
            }
        }
        dbPlanes.child(plan.getCodigo()).setValue(plan);
    }
        public void denunciarPlan(Plan plan, String codigoUsuario){
            dbPlanes.getDatabase().getReference().child("denuncias").child(codigoUsuario).setValue(plan);
        }

}

