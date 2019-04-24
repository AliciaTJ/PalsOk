package e.alicia.pals.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
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
import e.alicia.pals.VerPlan;
import e.alicia.pals.baseDatos.DataBasePlan;
import e.alicia.pals.modelo.Plan;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class AdapterVerPlan extends RecyclerView.Adapter<AdapterVerPlan.ItemViewHolder> {
    private List<Plan> mUserLsit;
    private Context mContext;
    private Plan plan;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
    DatabaseReference databaseReference = firebaseDatabase.getReference("planes");
    DataBasePlan dataBasePlan = new DataBasePlan(databaseReference);


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelverplan, parent, false);
        return new ItemViewHolder(view);
    }

    public AdapterVerPlan(Context mContext, List<Plan> mUserLsit) {

        this.mContext = mContext;
        this.mUserLsit = mUserLsit;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        plan = mUserLsit.get(position);

        holder.tvNombre.setText(plan.getNombre());
        holder.tvFecha.setText("Fecha: " + plan.getFecha());
        holder.tvInformacion.setText("Informacion: " + plan.getInformacion());
        holder.tvUbicacion.setText("Lugar: " + plan.getLugar());
        holder.tvUsuarios.setText("Usuarios apuntados: " + plan.getUsuariosapuntados().size());
        holder.botonApuntar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBasePlan.apuntarseAlPlan(plan, firebaseUser.getUid());

            }
        });

        if (plan.getEstado().equalsIgnoreCase("cerrado")){
            holder.tvInformacion.setText("Este plan ha sido cerrado");
            holder.botonDejar.setEnabled(false);
            holder.botonApuntar.setEnabled(false);
            holder.botonChat.setVisibility(INVISIBLE);
            holder.botonEliminar.setVisibility(INVISIBLE);
        }
       else if (plan.getEstado().equalsIgnoreCase("vencido")){
            holder.tvInformacion.setText("Este plan ha vencido. Ya no puedes apuntarte, " +
                    "pero podrás seguir accediendo al chat durante 7 días");
            holder.botonDejar.setEnabled(true);
            holder.botonApuntar.setVisibility(INVISIBLE);
            holder.botonChat.setVisibility(VISIBLE);
            holder.botonEliminar.setVisibility(INVISIBLE);
        }
        else if(plan.getEstado().equalsIgnoreCase("abierto")) {
            if (plan.getUsuariocreador().equalsIgnoreCase(firebaseUser.getUid())) {
                holder.botonDejar.setVisibility(INVISIBLE);
                holder.botonApuntar.setVisibility(INVISIBLE);
                holder.botonChat.setVisibility(VISIBLE);
                holder.botonEliminar.setVisibility(VISIBLE);
            } else {
                for (int i = 0; i < plan.getUsuariosapuntados().size(); i++) {
                    if (firebaseUser.getUid().equalsIgnoreCase(plan.getUsuariosapuntados().get(i))) {
                        holder.botonApuntar.setVisibility(INVISIBLE);
                        holder.botonDejar.setVisibility(VISIBLE);
                        holder.botonChat.setVisibility(VISIBLE);
                    }
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return mUserLsit.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView ivFoto;
        TextView tvNombre, tvUbicacion, tvFecha, tvUsuarios, tvInformacion;
        Button botonApuntar, botonChat, botonDejar, botonEliminar;

        public ItemViewHolder(View itemView) {
            super(itemView);
            botonDejar = itemView.findViewById(R.id.botonDejar);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvUbicacion = itemView.findViewById(R.id.tvUbicacion);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvUsuarios = itemView.findViewById(R.id.tvUsuarios);
            tvInformacion = itemView.findViewById(R.id.tvInformacion);
            botonApuntar = itemView.findViewById(R.id.botonApuntarse);
            botonChat = itemView.findViewById(R.id.botonChat);
            botonChat.setVisibility(INVISIBLE);
            ivFoto = itemView.findViewById(R.id.ivRandom);
            botonEliminar = (Button) itemView.findViewById(R.id.botonEliminar);
            botonEliminar.setVisibility(INVISIBLE);

            botonEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    borrarPlan();
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

                    System.out.println("oa");
                    dejarPlan();
                }
            });

            botonChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    abrirChat(plan.getCodigo());
                }
            });
        }


        public int ponerFoto(String i) {
            switch (i) {
                case "freak":
                    return R.drawable.freak;

                case "cultura":
                    return R.drawable.cultura;
                case "musica":
                    return R.drawable.musica;

                case "deportes":
                    return R.drawable.deportes;

                case "otros":
                    return R.drawable.otros;

                case "turismo":
                    return R.drawable.turismo;

                case "cine":
                    return R.drawable.cine2;

                case "fiesta":
                    return R.drawable.fiesta;

                default:
                    return R.drawable.fiesta;
            }


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

        public void apuntarsePlan() {
            dataBasePlan.apuntarseAlPlan(plan, firebaseUser.getUid());
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(R.string.agregar)
                    .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
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
                            Intent i = new Intent(mContext, MisPlanes.class);
                            mContext.startActivity(i);

                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    }).show();


        }

        public void abrirChat(String codigo) {
            Intent i = new Intent(mContext, Chat.class);
            i.putExtra("codigo", codigo);
            mContext.startActivity(i);
        }
    }
}