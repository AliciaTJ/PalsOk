package e.alicia.pals.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;

import e.alicia.pals.R;
import e.alicia.pals.modelo.Mensaje;


public class AdapterChat extends RecyclerView.Adapter<AdapterChat.ItemViewHolder> {
    private List<Mensaje> mUserLsit;
    private Context mContext;
    private Mensaje mensaje;



    @Override
    public AdapterChat.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelmensaje, parent, false);
        return new AdapterChat.ItemViewHolder(view);
    }

    public AdapterChat(Context mContext, List<Mensaje> mUserLsit) {

        this.mContext = mContext;
        this.mUserLsit = mUserLsit;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(AdapterChat.ItemViewHolder holder, int position) {
        Mensaje mensaje=mUserLsit.get(position);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mensaje.getFechaHora());

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.get(Calendar.HOUR);
        holder.tvMensaje.setText(mensaje.getMensaje());
        holder.tvFecha.setText(mDay+"/"+mMonth+"/"+mYear+"\t"
                + calendar.get(Calendar.HOUR)
                +":"+calendar.get(Calendar.MINUTE)
                +":"+calendar.get(Calendar.SECOND));

        holder.tvUsuario.setText(mensaje.getUsuario());


    }

    @Override
    public int getItemCount() {
        return mUserLsit.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {


        TextView tvUsuario, tvMensaje, tvFecha;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvMensaje = itemView.findViewById(R.id.tvMensaje);
            tvUsuario = itemView.findViewById(R.id.tvUsuario);

        }
    }
}


