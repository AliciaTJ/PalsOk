package e.alicia.pals;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.FirebaseDatabase;

import e.alicia.pals.baseDatos.DataBasePlan;

/**
 * Clase que carga la activity para elegir el tipo de plan a crear
 */
public class TipoPlan extends AppCompatActivity {
String user;

ImageButton ibFreak, ibCine, ibMusica, ibFiesta, ibOtros, ibTurismo, ibCultura, ibDeporte;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tipo_plan);
        user=getIntent().getStringExtra("user");
        //-----------se establecen los enlaces
        ibDeporte=findViewById(R.id.ibDeportes);
        ibDeporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPlan(8);
            }
        });
        ibCultura=findViewById(R.id.ibCultura);
        ibCultura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPlan(7);
            }
        });
        ibTurismo=findViewById(R.id.ibTurismo);
        ibTurismo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPlan(6);
            }
        });
        ibOtros=findViewById(R.id.ibOtros);
        ibOtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPlan(5);
            }
        });
        ibFiesta=findViewById(R.id.ibFiesta);
        ibFiesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPlan(4);
            }
        });
        ibFreak=findViewById(R.id.ibFreak);
        ibFreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPlan(1);
            }
        });
        ibCine=findViewById(R.id.ibCine);
        ibCine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPlan(2);
            }
        });
        ibMusica=findViewById(R.id.ibMusica);
        ibMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPlan(3);
            }
        });
    }


    /**
     * Metodo que abre otro intent para crear el plan. Envia el dato tipo entre activitys
     * @param tipo int
     */
    public void crearPlan(int tipo){
        Intent i=new Intent(this, PlanNuevo.class);
        i.putExtra("id", tipo);

        startActivity(i);
    }

    /**
     * Metodo que devuelve a la activity  portada si se pulsa atras
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this, ActivityPortada.class);
        startActivity(i);
    }


}
