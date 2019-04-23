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
    Boolean saved = null;
    List<Plan> planes = new ArrayList<>();
    Plan plan;
    DatabaseReference dbUsuarios;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();



    public DataBasePlan(DatabaseReference dbPlanes) {
        this.dbPlanes = dbPlanes;
        dbUsuarios = firebaseDatabase.getReference("usuarios");
    }




    //---------------------------planes

    //guardar
    public Boolean save(Plan plan, String codigo
    ) {
        if (plan == null) {
            saved = false;
        } else {

            try {
                dbPlanes.child("planes").child(plan.getCodigo()).setValue(plan);
                saved = true;
            } catch (DatabaseException e) {
                e.printStackTrace();
                saved = false;
            }

        }

        return saved;
    }


    public void apuntarseAlPlan(Plan plan, String codigoUsuario){
        plan.getUsuariosapuntados().add(codigoUsuario);
        dbPlanes.child(plan.getCodigo()).setValue(plan);
    }

    public boolean borrarPlan(Plan plan){
        dbPlanes.child(plan.getCodigo()).removeValue();
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

