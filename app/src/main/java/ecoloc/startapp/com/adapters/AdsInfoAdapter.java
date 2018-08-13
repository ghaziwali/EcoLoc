package ecoloc.startapp.com.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ecoloc.startapp.com.R;
import ecoloc.startapp.com.constractors.Ads;

/**
 * Created by ghazi on 25/05/18.
 */

public class AdsInfoAdapter extends RecyclerView.Adapter<AdsInfoAdapter.ViewHolder> {



    public List<Ads> infoList;
    public Context context;

    public AdsInfoAdapter(Context context, List<Ads> infoList) {
        this.infoList = infoList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public TextView title,description,price,model,trans,type,ville;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            title = (TextView) mView.findViewById(R.id.title_txt);
            price = (TextView) mView.findViewById(R.id.price_txt);
            description = (TextView) mView.findViewById(R.id.description);
            model = (TextView) mView.findViewById(R.id.model_txt);
            type = (TextView) mView.findViewById(R.id.type_txt);
            trans = (TextView) mView.findViewById(R.id.trans_txt);
            ville = (TextView) mView.findViewById(R.id.ville_txt);

        }
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_info, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(AdsInfoAdapter.ViewHolder holder, final int position) {
        holder.title.setText(infoList.get(position).getTitle());
        holder.price.setText(infoList.get(position).getPrice());
        holder.description.setText(infoList.get(position).getDescription());
        holder.model.setText(infoList.get(position).getModel());
        holder.trans.setText(infoList.get(position).getTrans());
        holder.type.setText(infoList.get(position).getType());
        holder.ville.setText(infoList.get(position).getVille());

    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }
}
