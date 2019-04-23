package e.alicia.pals.adaptadores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import e.alicia.pals.R;
import e.alicia.pals.VerPlan;
import e.alicia.pals.modelo.Plan;

public class AdapterPlanes extends RecyclerView.Adapter<AdapterPlanes.ItemViewHolder> {
    private List<Plan> mUserLsit = new ArrayList<>();
    private Context mContext;
    private CardView cv;
    private ItemClickListener itemClickListener;

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model, parent, false);
        return new ItemViewHolder(view);
    }

    public AdapterPlanes(Context mContext, List<Plan> mUserLsit) {
        this.mContext = mContext;
        this.mUserLsit = mUserLsit;

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Plan user = mUserLsit.get(position);
        holder.tvTitulo.setText(user.getNombre());
        holder.tvFecha.setText(user.getFecha());
        holder.ivFoto.setImageResource(ponerFoto(user.getTipo()));
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                abrirDetalle(mUserLsit.get(pos).getCodigo());
            }
        });
        if (position%2==0) {
           // holder.cv.setBackgroundColor(R.color.places_autocomplete_list_background);
            //holder.ivFoto.setBackgroundColor(R.color.places_autocomplete_list_background);
        }else{
           // holder.cv.setBackgroundColor(R.color.quantum_black_divider);
           // holder.ivFoto.setBackgroundColor(R.color.quantum_black_divider);
        }
    }

    @Override
    public int getItemCount() {
        return mUserLsit.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder  implements  View.OnClickListener{
        TextView tvTitulo, tvFecha;
        ImageView ivFoto;
        CardView cv;
        ItemClickListener itemClickListener;
        public ItemViewHolder(View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.list_desc);
            tvTitulo = itemView.findViewById(R.id.list_title);
            cv = itemView.findViewById(R.id.cv);
            ivFoto = itemView.findViewById(R.id.ivFoto);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener=itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(this.getLayoutPosition());
        }
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

    private void abrirDetalle(String codigo)
    {
        Intent i=new Intent(this.mContext, VerPlan.class);

        i.putExtra("codigo",codigo);


        this.mContext.startActivity(i);
    }
}