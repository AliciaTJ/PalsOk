package e.alicia.pals.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import e.alicia.pals.Chat;
import e.alicia.pals.MisPlanes;
import e.alicia.pals.R;
import e.alicia.pals.baseDatos.DataBasePlan;
import e.alicia.pals.modelo.Plan;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


/**
 * Clase que carga un plan dentro de una plantilla para que sea visible en el activity
 */
public class AdapterVerPlan extends RecyclerView.Adapter<AdapterVerPlan.ItemViewHolder> {
    private List<Plan> mUserLsit;
    private Context mContext;
    private Plan plan;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    DatabaseReference databaseReference = firebaseDatabase.getReference("planes");
    DataBasePlan dataBasePlan = new DataBasePlan(databaseReference);


    /**
     * Carga el layout
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelverplan, parent, false);
        return new ItemViewHolder(view);
    }

    public AdapterVerPlan(Context mContext, List<Plan> mUserLsit) {

        this.mContext = mContext;
        this.mUserLsit = mUserLsit;
    }

    /**
     * Establece los valores del elemento plan en la plantilla cargada en
     * la activity
     *
     * @param holder
     * @param position
     */
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        plan = mUserLsit.get(position);
        holder.tvNombre.setText(plan.getNombre());
        holder.tvFecha.setText("Fecha: " + plan.getFecha());
        holder.tvInformacion.setText(plan.getInformacion());
        holder.tvUbicacion.setText("Lugar: " + plan.getLugar() + ", " + plan.getProvincia());
        holder.tvUsuarios.setText("Usuarios apuntados: " + plan.getUsuariosapuntados().size());

        personalizarAdaptador(holder);
    }

    /**
     * Método que carga una imagen superior aleatoria dependiendo
     * del tipo de plan elegido
     *
     * @param holder
     */
    private void personalizarAdaptador(ItemViewHolder holder) {
        int imagen = 0;
        switch (plan.getTipo()) {


            case "freak":
                ArrayList<Integer> freak = new ArrayList<>();
                freak.add(R.drawable.a12);
                freak.add(R.drawable.a16);
                freak.add(R.drawable.a28);
                imagen = (int) (Math.random() * 2) + 1;
                holder.ivFoto.setImageResource(freak.get(imagen));
                break;

            case "cultura":
                ArrayList<Integer> imagenes = new ArrayList<>();
                imagenes.add(R.drawable.a2);
                imagenes.add(R.drawable.a3);
                imagenes.add(R.drawable.a14);
                imagenes.add(R.drawable.a21);
                imagenes.add(R.drawable.a34);
                imagen = (int) (Math.random() * 4) + 1;
                holder.ivFoto.setImageResource(imagenes.get(imagen));
                break;
            case "musica":
                ArrayList<Integer> musica = new ArrayList<>();
                musica.add(R.drawable.a24);
                musica.add(R.drawable.a25);
                musica.add(R.drawable.a30);
                musica.add(R.drawable.a27);
                musica.add(R.drawable.a29);
                musica.add(R.drawable.a31);
                imagen = (int) (Math.random() * 5) + 1;
                holder.ivFoto.setImageResource(musica.get(imagen));
                break;
            case "deportes":

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
                holder.ivFoto.setImageResource(deportes.get(imagen));
                break;
            case "otros":
                ArrayList<Integer> otros = new ArrayList<>();
                otros.add(R.drawable.a6);
                otros.add(R.drawable.a7);
                otros.add(R.drawable.a8);
                otros.add(R.drawable.a11);
                otros.add(R.drawable.a33);
                otros.add(R.drawable.a36);

                imagen = (int) (Math.random() * 5) + 1;
                System.out.println(imagen);
                holder.ivFoto.setImageResource(otros.get(imagen));
                break;
            case "turismo":
                ArrayList<Integer> turismo = new ArrayList<>();
                turismo.add(R.drawable.a1);
                turismo.add(R.drawable.a15);
                turismo.add(R.drawable.a20);
                turismo.add(R.drawable.a22);
                turismo.add(R.drawable.a23);
                turismo.add(R.drawable.a35);
                imagen = (int) (Math.random() * 5) + 1;
                System.out.println(imagen);
                holder.ivFoto.setImageResource(turismo.get(imagen));
                break;
            case "cine":
                ArrayList<Integer> cine = new ArrayList<>();
                cine.add(R.drawable.a32);
                cine.add(R.drawable.a37);
                cine.add(R.drawable.a38);
                imagen = (int) (Math.random() * 2) + 1;
                System.out.println(imagen);
                holder.ivFoto.setImageResource(cine.get(imagen));
                break;
            case "fiesta":

                ArrayList<Integer> fiesta = new ArrayList<>();
                fiesta.add(R.drawable.a4);
                fiesta.add(R.drawable.a19);
                fiesta.add(R.drawable.a5);
                fiesta.add(R.drawable.a27);
                fiesta.add(R.drawable.a29);
                imagen = (int) (Math.random() * 3) + 1;
                holder.ivFoto.setImageResource(fiesta.get(imagen));
                break;
            default:
                ArrayList<Integer> otros2 = new ArrayList<>();
                otros2.add(R.drawable.a6);
                otros2.add(R.drawable.a7);
                otros2.add(R.drawable.a8);
                imagen = (int) (Math.random() * 2) + 1;
                System.out.println(imagen);
                holder.ivFoto.setImageResource(otros2.get(imagen));


        }
        /*
        Dependiendo del estado del plan y del usuario, se activan o desactivan los botones
         */

        if (plan.getEstado().equalsIgnoreCase("cerrado")) {
            holder.tvInformacion.setText("Este plan ha sido cerrado");
            holder.botonDejar.setEnabled(false);
            holder.botonDejar.setTextColor(Color.GRAY);
            holder.botonApuntar.setEnabled(false);
            holder.botonApuntar.setTextColor(Color.GRAY);
            holder.botonChat.setVisibility(INVISIBLE);
            holder.botonEliminar.setVisibility(INVISIBLE);
        } else if (plan.getEstado().equalsIgnoreCase("vencido")) {
            holder.tvInformacion.setText("Este plan ha vencido. Ya no puedes apuntarte, " +
                    "pero podrás seguir accediendo al chat durante 7 días");
            holder.botonDejar.setEnabled(true);
            holder.botonApuntar.setEnabled(false);
            holder.botonApuntar.setTextColor(Color.GRAY);
            holder.botonChat.setVisibility(VISIBLE);
            holder.botonEliminar.setVisibility(INVISIBLE);
        } else if (plan.getEstado().equalsIgnoreCase("abierto")) {
            if (plan.getUsuariocreador().equalsIgnoreCase(firebaseUser.getUid())) {

                holder.botonApuntar.setEnabled(false);
                holder.botonApuntar.setTextColor(Color.GRAY);
                holder.botonDejar.setEnabled(false);

                holder.botonChat.setVisibility(VISIBLE);
                holder.botonEliminar.setVisibility(VISIBLE);
            } else {
                for (int i = 0; i < plan.getUsuariosapuntados().size(); i++) {
                    if (firebaseUser.getUid().equalsIgnoreCase(plan.getUsuariosapuntados().get(i))) {
                        holder.botonApuntar.setEnabled(false);
                        holder.botonApuntar.setTextColor(Color.GRAY);
                        holder.botonDejar.setVisibility(VISIBLE);
                        holder.botonDejar.setEnabled(true);

                        holder.botonChat.setVisibility(VISIBLE);
                    }
                }

            }
        }
        if (firebaseUser.getEmail().equalsIgnoreCase("aliciavisual@gmail.com")){
            holder.botonEliminar.setVisibility(VISIBLE);
            holder.botonQuitarDenuncia.setVisibility(VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mUserLsit.size();
    }

    /**
     * Metodo que carga los elementos de la plantilla
     */

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView ivFoto;
        TextView tvNombre, tvUbicacion, tvFecha, tvUsuarios, tvInformacion;
        Button botonApuntar, botonChat, botonDejar, botonEliminar, botonQuitarDenuncia;

        public ItemViewHolder(View itemView) {
            super(itemView);
            botonQuitarDenuncia=itemView.findViewById(R.id.botonAdministrador2);
            botonDejar = itemView.findViewById(R.id.botonDejar);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvUbicacion = itemView.findViewById(R.id.tvUbicacion);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvUsuarios = itemView.findViewById(R.id.tvUsuarios);
            tvInformacion = itemView.findViewById(R.id.tvInformacion);
            botonApuntar = itemView.findViewById(R.id.botonApuntarse);
            botonChat = itemView.findViewById(R.id.botonChat);
            botonChat.setVisibility(INVISIBLE);
            botonDejar.setEnabled(false);
            botonDejar.setTextColor(Color.GRAY);
            ivFoto = itemView.findViewById(R.id.ivRandom);
            botonEliminar = (Button) itemView.findViewById(R.id.botonEliminar);
            botonEliminar.setVisibility(INVISIBLE);
            botonEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    borrarPlan();
                }
            });
            botonQuitarDenuncia.setVisibility(INVISIBLE);
            if (firebaseUser.getEmail().equalsIgnoreCase("aliciavisual@gmail.com")){
                botonQuitarDenuncia.setVisibility(VISIBLE);
            }
            botonQuitarDenuncia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quitarDenuncia();
                }
            });
            botonApuntar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    apuntarsePlan();

                    botonApuntar.setEnabled(false);
                    botonChat.setVisibility(VISIBLE);
                    botonDejar.setVisibility(VISIBLE);
                    botonDejar.setEnabled(true);

                }
            });

            botonDejar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dejarPlan();
                }
            });

            botonChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    abrirChat(plan.getCodigo(), plan.getNombre(), plan.getUsuariosapuntados());
                }
            });
        }

        public void borrarPlan() {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(R.string.seguroeliminar)
                    .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dataBasePlan.borrarPlan(plan);
                            Toast.makeText(mContext, "Has borrado el plan", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(mContext, MisPlanes.class);
                            mContext.startActivity(i);

                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }).show();

        }
        public void quitarDenuncia() {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("¿Estas seguro que quieres retirar la denuncia?")
                    .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dataBasePlan.quitarDenuncia(plan);
                            Toast.makeText(mContext, "Quitado de la lista de denunciados", Toast.LENGTH_LONG).show();


                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }).show();

        }
        public void apuntarsePlan() {

            dataBasePlan.apuntarseAlPlan(plan, firebaseUser.getUid());

            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(R.string.agregar)
                    .setPositiveButton(R.string.sichat, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            abrirChat(plan.getCodigo(), plan.getNombre(), plan.getUsuariosapuntados());

                        }
                    }).setNegativeButton(R.string.nochat, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            }).show();


        }

        public void dejarPlan() {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(R.string.segurodejar)
                    .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dataBasePlan.dejarPlan(plan, firebaseUser.getUid());
                            Toast.makeText(mContext, R.string.eliminado, Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }).show();

        }

        public void abrirChat(String codigo, String nombre, List<String> usuarios) {
            String[] listaUsuarios = new String[usuarios.size()];
            for (int i = 0; i < usuarios.size(); i++) {
                listaUsuarios[i] = usuarios.get(i);
            }
            Intent i = new Intent(mContext, Chat.class);
            i.putExtra("codigo", codigo);
            i.putExtra("nombre", nombre);
            i.putExtra("usuarios", listaUsuarios);
            mContext.startActivity(i);
        }



    }
}

