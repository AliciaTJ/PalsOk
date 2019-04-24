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
    private ImageView imagen;
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

        imagen = findViewById(R.id.ivCabecera);
        db = new DataBasePlan(dbr);
        switch (tipo) {
            case 1:
                imagen.setImageResource(R.drawable.freak);
                tipoPlan = "freak";
                break;
            case 2:
                imagen.setImageResource(R.drawable.cine2);
                tipoPlan = "cine";
                break;
            case 3:
                imagen.setImageResource(R.drawable.musica);
                tipoPlan = "musica";
                break;
            case 4:
                imagen.setImageResource(R.drawable.a3);
                tipoPlan = "fiesta";
                break;
            case 5:
                imagen.setImageResource(R.drawable.a5);
                tipoPlan = "otros";
                break;
            case 6:
                imagen.setImageResource(R.drawable.a1);
                tipoPlan = "turismo";
                break;
            case 7:
                imagen.setImageResource(R.drawable.a2);
                tipoPlan = "cultura";
                break;
            case 8:
                imagen.setImageResource(R.drawable.deportes);
                tipoPlan = "deportes";
                break;

        }
        etInfo = findViewById(R.id.informacion);
        etCreado=findViewById(R.id.tvCreador);
        etCreado.setText("Creado por: " + user.getDisplayName());
        etTitulo = findViewById(R.id.titulo);
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
                        Toast.makeText(this, "Se guarda el registro", Toast.LENGTH_LONG).show();
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
                System.out.println(place.getAddress());
                System.out.println(place.getName());
                etLugar.setText(place.getAddress());
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


