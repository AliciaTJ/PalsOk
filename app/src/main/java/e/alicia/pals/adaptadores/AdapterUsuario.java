package e.alicia.pals.adaptadores;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.icu.util.Calendar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import e.alicia.pals.R;
import e.alicia.pals.baseDatos.DataBaseUsuario;
import e.alicia.pals.modelo.Usuario;

import static android.view.View.INVISIBLE;

/**
 * Adaptador de carga un unico usuario en la plantilla establecida.
 * Se utiliza un arraylist de un unico valor.
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class AdapterUsuario extends RecyclerView.Adapter<AdapterUsuario.ItemViewHolder> {
    private List<Usuario> mUserLsit;
    private Context mContext;
    private Calendar fechaC;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser firebaseUser = auth.getCurrentUser();
    private FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    public final Calendar c = Calendar.getInstance();
    private Button botonGuardar, botonEditar, cambiarFoto;
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    private EditText etNombre, etEmail, etDescripcion;
    private ImageView foto;
    private TextInputLayout tilNombre;
    private TextInputLayout tilInformacion;
    private Usuario user;
    private Button etFecha;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("usuarios");
    private DataBaseUsuario bd = new DataBaseUsuario(databaseReference);

    /**
     * Carga la plantilla del modelo usuario en la activity
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public AdapterUsuario.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelusuario, parent, false);
        return new ItemViewHolder(view);
    }

    public AdapterUsuario(Context mContext, List<Usuario> mUserLsit) {
        this.mContext = mContext;
        this.mUserLsit = mUserLsit;
    }

    /**
     * Establece los valores del usuario
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(AdapterUsuario.ItemViewHolder holder, int position) {
        user = mUserLsit.get(position);

        if (user.getCodigo().equalsIgnoreCase(firebaseUser.getUid())) {
            etFecha.setText(user.getFechanac());
            etNombre.setText(user.getNombre());
            etEmail.setText(user.getEmail());
            etDescripcion.setText(user.getDescripcion());
            firebaseStorage.getReference().child("usuarios/" + firebaseUser.getUid())

                    .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                @Override
                public void onSuccess(Uri uri) {
                    user.setFoto(uri.toString());
              
                    Glide
                            .with(mContext)
                            .load(uri)
                            .into(foto);
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Glide
                            .with(mContext)
                            .load(R.drawable.user)
                            .into(foto);

                }
            });
        }
            /**
             * El boton editar permite modificar el usuario, pone en editable los edittext
             */
            botonEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    etNombre.setEnabled(true);
                    etDescripcion.setEnabled(true);
                    cambiarFoto.setVisibility(View.VISIBLE);
                    etFecha.setEnabled(true);
                }
            });
            etFecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    obtenerFecha();
                }
            });

            /**
             * Se guarda el usuario en firebase
             */
            botonGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    user.setNombre(etNombre.getText().toString());
                    user.setEmail(etEmail.getText().toString());
                    user.setDescripcion(etDescripcion.getText().toString());
                    user.setCodigo(firebaseUser.getUid());
                    user.setFechanac(etFecha.getText().toString());
                    user.setNotificacion(false);
                    if (validarGuardar())
                        bd.modificar(user);
                    etNombre.setEnabled(false);
                    etDescripcion.setEnabled(false);
                    etFecha.setEnabled(false);
                    cambiarFoto.setVisibility(INVISIBLE);


                }


            });

        }



    @Override
    public int getItemCount() {
        return mUserLsit.size();
    }

    /**
     * Recupera los elementos de la plantilla del usuario
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder {


        public ItemViewHolder(View itemView) {
            super(itemView);
            etNombre = itemView.findViewById(R.id.etNombre);
            etEmail = itemView.findViewById(R.id.etEmail);
            etDescripcion = itemView.findViewById(R.id.etDescripcion);
            etFecha = itemView.findViewById(R.id.etFecha);
            foto = itemView.findViewById(R.id.ivImagen);
            etNombre.setEnabled(false);
            etEmail.setEnabled(false);
            etDescripcion.setEnabled(false);
            etNombre.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            etEmail.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            etDescripcion.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            etDescripcion.setTextColor(Color.BLACK);
            etNombre.setTextColor(Color.BLACK);
            etEmail.setTextColor(Color.BLACK);
            botonEditar = itemView.findViewById(R.id.botonEditar);
            cambiarFoto = itemView.findViewById(R.id.botonCambiar);
            botonGuardar = itemView.findViewById(R.id.botonGuardar);
            tilNombre = itemView.findViewById(R.id.tilnombre);
            tilInformacion = itemView.findViewById(R.id.tilinformacion);
            cambiarFoto.setVisibility(INVISIBLE);


        }
    }

    public Context getmContext() {

        return getmContext();
    }

    public void obtenerFecha() {

        DatePickerDialog recogerFecha = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10) ? 0 + String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10) ? 0 + String.valueOf(mesActual) : String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                etFecha.setText(diaFormateado + "/" + mesFormateado + "/" + year);
                fechaC = Calendar.getInstance();
                fechaC.set(year, mesActual, dayOfMonth);
                user.setFechanac(dayOfMonth + "/" + mesActual + "/" + year);
            }
        }, anio, mes, dia);

        recogerFecha.show();

    }


    private boolean esNombreValido(String nombre) {
        if (nombre.length() > 15 || nombre.length()<3) {
            tilNombre.setError("Nombre entre 3 y 15 caracteres");
            return false;
        } else {
            tilNombre.setError(null);
        }

        return true;
    }

    private boolean esInfoValido(String nombre) {

        if (nombre.length() > 200) {
            tilInformacion.setError("Introduce información (maximo 200 caracteres)");
            return false;
        } else {
            tilInformacion.setError(null);
        }

        return true;
    }


    private boolean esFechaValida(long fecha){
        if (fecha>=System.currentTimeMillis()){
            Toast.makeText(mContext, "La fecha no puede ser superior a la de hoy", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean validarGuardar() {
        String nombre = etNombre.getText().toString();
        String info = etDescripcion.getText().toString();
        String email = etEmail.getText().toString();


        boolean a = esNombreValido(nombre);
        boolean b = esInfoValido(info);
        boolean c = validarEmail(email);


        if (a && b && c ) {
            Toast.makeText(mContext, "Se modifica el registro", Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(mContext, "Error al guardar el registro", Toast.LENGTH_LONG).show();
            return false;
        }

    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }


}








