package gr.aetos.conapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class BeehiveChecksListAdapter extends RecyclerView.Adapter<BeehiveChecksListAdapter.BeehiveChecksViewHolder> {
    private onItemClickListener onItemClickListener;
    private onItemLongClickListener onItemLongClickListener;

    class BeehiveChecksViewHolder extends RecyclerView.ViewHolder {
        private final TextView beehiveItemView;

        private BeehiveChecksViewHolder(View itemView) {
            super(itemView);
            beehiveItemView = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemCLick(beehiveChecks.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if(onItemLongClickListener != null && position != RecyclerView.NO_POSITION) {
                        onItemLongClickListener.onItemLongCLick(beehiveChecks.get(position));
                    }
                    return true;
                }
            });
        }
    }

    private final LayoutInflater mInflater;
    private List<BeehiveCheck> beehiveChecks; // Cached copy of words

    BeehiveChecksListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @NonNull
    @Override
    public BeehiveChecksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item_beehive_check, parent, false);
        return new BeehiveChecksViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BeehiveChecksViewHolder holder, int position) {
        if (beehiveChecks != null) {
            BeehiveCheck current = beehiveChecks.get(position);
            holder.beehiveItemView.setText("Έλεγχος " + current.date);
        } else {
            // Covers the case of data not being ready yet.
            holder.beehiveItemView.setText("No Beehive");
        }
    }

    void setBeehiveChecks(List<BeehiveCheck> beehiveChecks){
        this.beehiveChecks = beehiveChecks;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (beehiveChecks != null)
            return beehiveChecks.size();
        else return 0;
    }

    public interface onItemClickListener{
        void onItemCLick(BeehiveCheck beehiveCheck);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.onItemClickListener = listener;
    }

    public interface onItemLongClickListener{
        void onItemLongCLick(BeehiveCheck beehiveCheck);
    }

    public void setOnItemLongClickListener(onItemLongClickListener listener){
        this.onItemLongClickListener = listener;
    }
}