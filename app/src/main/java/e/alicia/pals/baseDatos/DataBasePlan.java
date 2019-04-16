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
                dbUsuarios.child(codigo).child("planesapuntados").child(plan.getCodigo()).setValue(plan.getCodigo());
                saved = true;
            } catch (DatabaseException e) {
                e.printStackTrace();
                saved = false;
            }

        }

        return saved;
    }


    public List<Plan> retrieve() {
        dbPlanes.child("planes").addChildEventListener(new ChildEventListener() {
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

        return planes;
    }

    private void fetchData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Plan plan = ds.getValue(Plan.class);
            planes.add(plan);


        }


    }


    public void apuntarseAlPlan(Plan plan, String codigoUsuario){
        plan.getUsuariosapuntados().add(codigoUsuario);
        dbPlanes.child(plan.getCodigo()).setValue(plan);
      dbUsuarios.child(codigoUsuario).child("planesapuntados").child(plan.getCodigo()).setValue(plan.getCodigo());

    }

    public boolean borrarPlan(Plan plan){
      for (int i=0; i<  plan.getUsuariosapuntados().size(); i++){
          dbUsuarios.child(plan.getUsuariosapuntados().get(i)).child("planesapuntados").child(plan.getCodigo()).removeValue();
      }
        dbPlanes.child(plan.getCodigo()).removeValue();
        return true;


    }

    public void dejarPlan(Plan plan, String codigoUsuario){
        for (int i=0; i<plan.getUsuariosapuntados().size(); i++){
            if ( plan.getUsuariosapuntados().get(i).equalsIgnoreCase(codigoUsuario)){
                plan.getUsuariosapuntados().remove(i);
            }
        }
        dbPlanes.child("planes").child(plan.getCodigo()).setValue(plan);
        dbUsuarios.child(codigoUsuario).child("planesapuntados").child(plan.getCodigo()).removeValue();
    }
}

