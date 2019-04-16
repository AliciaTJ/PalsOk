package e.alicia.pals.adaptadores;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.icu.util.Calendar;

import java.util.ArrayList;
import java.util.List;

import e.alicia.pals.R;
import e.alicia.pals.baseDatos.DataBaseUsuario;
import e.alicia.pals.modelo.Usuario;


@RequiresApi(api = Build.VERSION_CODES.N)
public class AdapterUsuario extends RecyclerView.Adapter<AdapterUsuario.ItemViewHolder> {
    private List<Usuario> mUserLsit = new ArrayList<>();
    private Context mContext;
    Calendar fechaC;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser firebaseUser=auth.getCurrentUser();
    public final Calendar c = Calendar.getInstance();
    Button botonGuardar, botonEditar, cambiarFoto;
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);
    EditText etNombre, etEmail, etDescripcion;
    TextView etFecha;
    ImageView foto;
    TextInputLayout tilNombre;
    TextInputLayout tilInformacion;
    TextInputLayout tilEmail;
    TextView tvFecha;
    Usuario user;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=firebaseDatabase.getReference("usuarios");
    DataBaseUsuario bd=new DataBaseUsuario(databaseReference);


    @Override
    public AdapterUsuario.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelusuario, parent, false);
        return new ItemViewHolder(view);
    }

    public AdapterUsuario(Context mContext, List<Usuario> mUserLsit) {
        this.mContext = mContext;
        this.mUserLsit = mUserLsit;
    }

    @Override
    public void onBindViewHolder(AdapterUsuario.ItemViewHolder holder, int position) {
       user = mUserLsit.get(position);
        if (user.getCodigo().equalsIgnoreCase(firebaseUser.getUid())) {
            etFecha.setText(user.getFechanac());
            etNombre.setText(user.getNombre());
            etEmail.setText(user.getEmail());
            etDescripcion.setText(user.getDescripcion());
            Glide
                    .with(mContext)
                    .load(user.getFoto())
                    .into(foto);

            botonEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    etNombre.setEnabled(true);
                    etDescripcion.setEnabled(true);
                    etEmail.setEnabled(true);
                    cambiarFoto.setVisibility(View.VISIBLE);

                }
            });
            etFecha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    obtenerFecha();
                }
            });

            botonGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        user.setNombre(etNombre.getText().toString());
                        user.setEmail(etEmail.getText().toString());
                        user.setDescripcion(etDescripcion.getText().toString());
                        user.setCodigo(firebaseUser.getUid());

                        bd.modificar(user);

                    }


            });

        }

    }

    @Override
    public int getItemCount() {
        return mUserLsit.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {


        public ItemViewHolder(View itemView) {
            super(itemView);
            tvFecha= itemView.findViewById(R.id.fecha);
            etNombre = itemView.findViewById(R.id.etNombre);
            etEmail = itemView.findViewById(R.id.etEmail);
            etDescripcion = itemView.findViewById(R.id.etDescripcion);
            etFecha = itemView.findViewById(R.id.etFecha);
            foto = itemView.findViewById(R.id.ivCabecera);
            etNombre.setEnabled(false);
            etEmail.setEnabled(false);
            etDescripcion.setEnabled(false);
            etNombre.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            etEmail.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            etDescripcion.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            etDescripcion.setTextColor(Color.BLACK);
            etNombre.setTextColor(Color.BLACK);
            etEmail.setTextColor(Color.BLACK);
            botonEditar=itemView.findViewById(R.id.botonEditar);
            cambiarFoto = itemView.findViewById(R.id.botonCambiar);
            botonGuardar = itemView.findViewById(R.id.botonGuardar);
            tilNombre = itemView.findViewById(R.id.tilnombre);
            tilEmail = itemView.findViewById(R.id.tilemail);
            tilInformacion = itemView.findViewById(R.id.tilinformacion);
            cambiarFoto.setVisibility(View.INVISIBLE);


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
                user.setFechanac(dayOfMonth+"/"+mesActual+"/"+year);
            }
        }, anio, mes, dia);

        recogerFecha.show();

    }






}



