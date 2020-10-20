package gr.aetos.conapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class BeehiveListAdapter extends RecyclerView.Adapter<BeehiveListAdapter.BeehiveViewHolder> {
    private onItemClickListener onItemClickListener;
    private onItemLongClickListener onItemLongClickListener;

    class BeehiveViewHolder extends RecyclerView.ViewHolder {
        private final TextView beehiveItemView, beehiveSubTextView;

        private BeehiveViewHolder(View itemView) {
            super(itemView);
            beehiveItemView = itemView.findViewById(R.id.textView);
            beehiveSubTextView = itemView.findViewById(R.id.beehive_subtext);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(onItemClickListener != null && position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemCLick(beehives.get(position));
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if(onItemLongClickListener != null && position != RecyclerView.NO_POSITION) {
                        onItemLongClickListener.onItemLongCLick(beehives.get(position));
                    }
                    return true;
                }
            });
        }
    }

    private final LayoutInflater mInflater;
    private List<Beehive> beehives; // Cached copy of words

    BeehiveListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @NonNull
    @Override
    public BeehiveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new BeehiveViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BeehiveViewHolder holder, int position) {
        if (beehives != null) {
            Beehive current = beehives.get(position);
            holder.beehiveItemView.setText("Μελίσσι " + current.number);
            holder.beehiveSubTextView.setText(current.beeQueenAge);
        } else {
            // Covers the case of data not being ready yet.
            holder.beehiveItemView.setText("No Beehive");
        }
    }

    void setBeehives(List<Beehive> beehives){
        this.beehives = beehives;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (beehives != null)
            return beehives.size();
        else return 0;
    }

    public interface onItemClickListener{
        void onItemCLick(Beehive beehive);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.onItemClickListener = listener;
    }

    public interface onItemLongClickListener{
        void onItemLongCLick(Beehive beehive);
    }

    public void setOnItemLongClickListener(onItemLongClickListener listener){
        this.onItemLongClickListener = listener;
    }
}