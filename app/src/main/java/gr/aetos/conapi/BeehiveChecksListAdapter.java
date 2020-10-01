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
    private onItemClickListener listener;

    class BeehiveChecksViewHolder extends RecyclerView.ViewHolder {
        private final TextView beehiveItemView;

        private BeehiveChecksViewHolder(View itemView) {
            super(itemView);
            beehiveItemView = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemCLick(beehiveChecks.get(position));
                    }
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
            holder.beehiveItemView.setText("Έλεγχος " + position);
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
        this.listener = listener;
    }
}