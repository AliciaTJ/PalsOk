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

public class DataBasePlan {

    DatabaseReference dbPlanes;
    Boolean guardado = null;
    DatabaseReference dbUsuarios;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();



    public DataBasePlan(DatabaseReference dbPlanes) {
        this.dbPlanes = dbPlanes;
        dbUsuarios = firebaseDatabase.getReference("usuarios");
    }


    //guardar
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


    public void apuntarseAlPlan(Plan plan, String codigoUsuario){
        plan.getUsuariosapuntados().add(codigoUsuario);
        dbPlanes.child(plan.getCodigo()).setValue(plan);
    }


    public boolean borrarPlan(Plan plan){
        plan.setEstado("cerrado");
        dbPlanes.child(plan.getCodigo()).removeValue();
        dbPlanes.getDatabase().getReference().child("planescerrados").child(plan.getCodigo()).setValue(plan);
        dbPlanes.getDatabase().getReference().child("chats").child(plan.getCodigo()).removeValue();
        return true;


    }

    public void dejarPlan(Plan plan, String codigoUsuario){
        for (int i=0; i<plan.getUsuariosapuntados().size(); i++){
            if ( plan.getUsuariosapuntados().get(i).equalsIgnoreCase(codigoUsuario)){
                plan.getUsuariosapuntados().remove(i);
            }
        }
        dbPlanes.child(plan.getCodigo()).setValue(plan);
    }


}

