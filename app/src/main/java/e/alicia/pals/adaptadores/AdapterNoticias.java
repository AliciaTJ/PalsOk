package e.alicia.pals.adaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import e.alicia.pals.R;
import e.alicia.pals.modelo.Noticia;


public class AdapterNoticias extends RecyclerView.Adapter<AdapterNoticias.ItemViewHolder> {
    private List<Noticia> mUserLsit = new ArrayList<>();
    private Context mContext;


    @Override
    public AdapterNoticias.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelnoticias, parent, false);
        return new ItemViewHolder(view);
    }

    public AdapterNoticias(Context mContext, List<Noticia> mUserLsit) {
        this.mContext = mContext;
        this.mUserLsit = mUserLsit;
    }

    @Override
    public void onBindViewHolder(AdapterNoticias.ItemViewHolder holder, int position) {
        Noticia user = mUserLsit.get(position);
        holder.tvTitular.setText(user.getTitular());
        holder.tvCuerpo.setText(user.getContenido());

        Glide
    .with(mContext)
    .load(user.getImagen())
    .into(holder.ivNoticia);


    }

    @Override
    public int getItemCount() {
        return mUserLsit.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitular, tvCuerpo;
        ImageView ivNoticia;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvCuerpo = itemView.findViewById(R.id.tvCuerpo);
            tvTitular = itemView.findViewById(R.id.tvTitular);
            ivNoticia = itemView.findViewById(R.id.ivNoticia);

        }
    }
}

