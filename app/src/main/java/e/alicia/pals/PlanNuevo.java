package e.alicia.pals;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import e.alicia.pals.baseDatos.DataBasePlan;
import e.alicia.pals.modelo.Plan;

import static android.graphics.Typeface.BOLD;

@RequiresApi(api = Build.VERSION_CODES.N)
public class PlanNuevo extends AppCompatActivity {
    private String key = "AIzaSyDCQwJ6TYi2Tsr4ghyatPxy7r03w_knCOU";
    private FirebaseUser user;
    private Place place;
    private DatabaseReference dbr;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth fba;
    private DataBasePlan db;
    private EditText etInfo, etTitulo, etLugar;
    private TextView etCreado;
    private Switch fechaIndiferente;
    int tipo;
    private String tipoPlan;
    private Plan plan;
    private ImageView ivImagen;
    private TextInputLayout tilTitulo;
    private TextInputLayout tilInformacion;
    private TextInputLayout tilLugar;
    public final Calendar c = Calendar.getInstance();
    private Calendar ca=Calendar.getInstance();
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    private TextView fecha;
    private Calendar hora;
   PlacesClient placesClient;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_nuevo);
        iniciarActivity();
        fechaIndiferente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    fecha.setTextColor(Color.BLACK);
                    obtenerFecha();

                }else{
                    fecha.setText("Indiferente");
                }
            }
        });
        MobileAds.initialize(this, "ca-app-pub-6032187278566198~3677017529");

    }

    public void iniciarActivity() {

        Places.initialize(getApplicationContext(), key);
        placesClient = Places.createClient(this);
        hora = Calendar.getInstance();
        tipo = getIntent().getIntExtra("id", 0);
        fba = FirebaseAuth.getInstance();
        user = fba.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbr = firebaseDatabase.getReference("planes");
        interstitialAd=new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-6032187278566198/3861231223");
        interstitialAd.loadAd(new AdRequest.Builder().build());
        ivImagen = findViewById(R.id.ivImagen);
        db = new DataBasePlan(dbr);
        int imagen = 0;
        switch (tipo) {
            case 1:
                ArrayList<Integer> freak = new ArrayList<>();
                freak.add(R.drawable.a12);
                freak.add(R.drawable.a16);
                freak.add(R.drawable.a28);
                imagen = (int) (Math.random() * 2) + 1;
                ivImagen.setImageResource(freak.get(imagen));

                tipoPlan = "freak";
                break;
            case 2:
                ArrayList<Integer> cine = new ArrayList<>();
                cine.add(R.drawable.a32);
                cine.add(R.drawable.a37);
                cine.add(R.drawable.a38);
                imagen = (int) (Math.random() * 2) + 1;
                System.out.println(imagen);
                ivImagen.setImageResource(cine.get(imagen));
                tipoPlan = "cine";
                break;
            case 3:
                ArrayList<Integer> musica = new ArrayList<>();
                musica.add(R.drawable.a24);
                musica.add(R.drawable.a25);
                musica.add(R.drawable.a30);
                musica.add(R.drawable.a27);
                musica.add(R.drawable.a29);
                musica.add(R.drawable.a31);
                imagen = (int) (Math.random() * 5) + 1;
               ivImagen.setImageResource(musica.get(imagen));
                tipoPlan = "musica";
                break;
            case 4:
                ArrayList<Integer> fiesta = new ArrayList<>();
                fiesta.add(R.drawable.a4);
                fiesta.add(R.drawable.a19);
                fiesta.add(R.drawable.a5);
                fiesta.add(R.drawable.a27);
                fiesta.add(R.drawable.a29);
                imagen = (int) (Math.random() * 3) + 1;
               ivImagen.setImageResource(fiesta.get(imagen));
                tipoPlan = "fiesta";
                break;
            case 5:
                ArrayList<Integer> otros = new ArrayList<>();
                otros.add(R.drawable.a6);
                otros.add(R.drawable.a7);
                otros.add(R.drawable.a8);
                otros.add(R.drawable.a11);
                otros.add(R.drawable.a33);
                otros.add(R.drawable.a36);

                imagen = (int) (Math.random() * 5) + 1;
                System.out.println(imagen);
               ivImagen.setImageResource(otros.get(imagen));
                tipoPlan = "otros";
                break;
            case 6:
                ArrayList<Integer> turismo = new ArrayList<>();
                turismo.add(R.drawable.a1);
                turismo.add(R.drawable.a15);
                turismo.add(R.drawable.a20);
                turismo.add(R.drawable.a22);
                turismo.add(R.drawable.a23);
                turismo.add(R.drawable.a35);
                imagen = (int) (Math.random() * 5) + 1;
                System.out.println(imagen);
                ivImagen.setImageResource(turismo.get(imagen));
                tipoPlan = "turismo";
                break;
            case 7:
                ArrayList<Integer> imagenes = new ArrayList<>();
                imagenes.add(R.drawable.a2);
                imagenes.add(R.drawable.a3);
                imagenes.add(R.drawable.a14);
                imagenes.add(R.drawable.a21);
                imagenes.add(R.drawable.a34);
                imagen = (int) (Math.random() * 4) + 1;
               ivImagen.setImageResource(imagenes.get(imagen));
                tipoPlan = "cultura";
                break;
            case 8:
                ArrayList<Integer> deportes = new ArrayList<>();
                deportes.add(R.drawable.a8);
                deportes.add(R.drawable.a10);
                deportes.add(R.drawable.a9);
                deportes.add(R.drawable.a26);
                deportes.add(R.drawable.a13);
                deportes.add(R.drawable.a17);
                deportes.add(R.drawable.a18);
                imagen = (int) (Math.random() * 6) + 1;
                System.out.println(imagen);
                ivImagen.setImageResource(deportes.get(imagen));
                tipoPlan = "deportes";
                break;

        }
        etInfo = findViewById(R.id.informacion);
        etCreado=findViewById(R.id.tvCreador);
        etCreado.setText("Creado por: " + user.getDisplayName());
        etTitulo = findViewById(R.id.etTitulo);
        etLugar = findViewById(R.id.lugar);
        fechaIndiferente = findViewById(R.id.fechaIndiferente);
        tilInformacion = findViewById(R.id.tilinformacion);
        tilTitulo = findViewById(R.id.tiltitulo);
        tilLugar = findViewById(R.id.tillugar);
        fecha = findViewById(R.id.etFecha);


    }

    public void guardar(View view) {
        if (validarDatos()) {
            plan = new Plan();
            plan.setInformacion(etInfo.getText().toString());
            plan.setFecha(fecha.getText().toString());
            plan.setTipo(tipoPlan);
            plan.setUsuariocreador(user.getUid());
            plan.setLugar(etLugar.getText().toString());
            List<String>usuarios=new ArrayList<>();
            usuarios.add(user.getUid());
            plan.setEstado("abierto");
            plan.setUsuariosapuntados(usuarios);
            plan.setNombre(etTitulo.getText().toString());
            plan.setCodigo(hora.getTimeInMillis() + user.getUid());
            db.guardar(plan);

            if (interstitialAd.isLoaded()){
                interstitialAd.show();
                System.out.println("hola");
            }
            verPlan();
        }else{
            Snackbar.make(view, "No ha sido posible crear el plan", Snackbar.LENGTH_LONG).show();
        }
    }


    public void verPlan() {

        Intent i = new Intent(this, VerPlan.class);
        i.putExtra("codigo", plan.getCodigo());
        startActivity(i);


    }

    public void cargarLugares(View view){
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);

        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

    }

    private boolean esNombreValido(String nombre) {
        if (nombre.length() > 30 || nombre.length()<3) {
            tilTitulo.setError("El titulo debe tener entre 3 y 50 caracteres");
            return false;
        } else {
            tilTitulo.setError(null);
        }

        return true;
    }
    private boolean esLugarValido(String lugar){
        if (lugar.length()<3 || lugar.length() > 50) {
            tilLugar.setError("Introduce el lugar (entre 3 y 50 caracteres)");
            return false;
        } else {
            tilLugar.setError(null);
        }

        return true;
    }
    private boolean esInfoValido(String nombre) {

        if (nombre.length()<10) {
            tilInformacion.setError("Introduce información (minimo 10 caracteres)");
            return false;
        } else {
            tilInformacion.setError(null);
        }

        return true;
    }

    private boolean esFechaValida(String fechaPlan){
        if (fechaPlan.equalsIgnoreCase("Indiferente")){
            return true;
        }
        else{
            if (ca.getTimeInMillis()<c.getTimeInMillis()){
               fecha.setTextColor(Color.RED);
               fecha.setTextAppearance(BOLD);

                return false;
            }else{
                return true;
            }


        }
    }

    private boolean validarDatos() {
        String nombre = etTitulo.getText().toString();
        String info = etInfo.getText().toString();
        String lugar=etLugar.getText().toString();
        String fechaPlan=fecha.getText().toString();

        if (esNombreValido(nombre)) {
            if (esLugarValido(lugar)) {
                if (esInfoValido(info)) {
                    if (esFechaValida(fechaPlan)) {
                        Toast.makeText(this, "Nuevo plan generado", Toast.LENGTH_LONG).show();
                        return true;
                    }else{
                        return false;
                    }
                } else {
                    return false;
                }
            }else{
                return false;
            }
        } else {
            return false;
        }


    }


    private void obtenerFecha() {


        final DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10) ? 0 + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10) ? 0 + String.valueOf(mesActual) : String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                ca.set(year, month, dayOfMonth);
                fecha.setText(diaFormateado + "/" + mesFormateado + "/" + year);
                }

        }, anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
               place = Autocomplete.getPlaceFromIntent(data);

                System.out.println(place.getName());
                etLugar.setText(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {

                Status status = Autocomplete.getStatusFromIntent(data);


            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(this, TipoPlan.class);
        startActivity(i);
    }

}


