package e.alicia.pals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import e.alicia.pals.adaptadores.AdapterPlanes;
import e.alicia.pals.adaptadores.AdapterUsuario;
import e.alicia.pals.adaptadores.AdapterVerPlan;
import e.alicia.pals.adaptadores.ItemClickListener;
import e.alicia.pals.baseDatos.DataBasePlan;
import e.alicia.pals.baseDatos.DataBaseUsuario;
import e.alicia.pals.modelo.Plan;
import e.alicia.pals.modelo.Usuario;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class VerPlan extends AppCompatActivity {



    ImageView ivRandom;
    DataBaseUsuario usuario;
    DataBasePlan dataBasePlan;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    Plan plan;
    String codigo;
    FirebaseUser firebaseUser;
RecyclerView rv;
List<Plan> planes=new ArrayList<>();
AdapterVerPlan adapterVerPlan;

Button botonApuntar, botonChat, botonDejar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verplan);
        iniciarActivity();
        codigo = getIntent().getStringExtra("codigo");

        cargarPlan(codigo);

    }



    public void iniciarActivity() {

        rv=(RecyclerView)findViewById(R.id.rv);
        botonApuntar=(Button)findViewById(R.id.botonApuntarse);
        botonDejar=(Button)findViewById(R.id.botonDejar);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("planes");
        dataBasePlan = new DataBasePlan(databaseReference);
        usuario = new DataBaseUsuario(firebaseDatabase.getReference("usuarios"));
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        rv.setLayoutManager(new LinearLayoutManager(this));
        //falta random imagenes

    }

    public void cargarPlan(final String codigo) {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                planes.clear();
                try{
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Plan user = dataSnapshot1.getValue(Plan.class);
                    if (user.getCodigo().equals(codigo)) {
                        planes.add(user);
                    }
                }
                    adapterVerPlan = new AdapterVerPlan(VerPlan.this, planes);
                    rv.setAdapter(adapterVerPlan);
                    adapterVerPlan.notifyDataSetChanged();
                }catch(NullPointerException npe){
                    System.out.println("nooooooooooooooooooooooo");
                }finally{
                    adapterVerPlan = new AdapterVerPlan(VerPlan.this, planes);
                    rv.setAdapter(adapterVerPlan);
                    adapterVerPlan.notifyDataSetChanged();
                    System.out.println(planes.size());
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this, ActivityPortada.class);
        startActivity(i);
    }
}
