package org.techtown.yawakeup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> datas;

    public PostAdapter(List<Post> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post data = datas.get(position);
        holder.title.setText(data.getTitle());
        holder.time.setText("시간 : " + data.getTime());
        holder.place.setText("장소 : " + data.getPlace());
        holder.count.setText("1/" + data.getCount());
    }

    public int getItemCount(){
        return datas.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView time;
        private TextView place;
        private TextView count;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.item_post_title);
            time = itemView.findViewById(R.id.item_post_time);
            place = itemView.findViewById(R.id.item_post_place);
            count = itemView.findViewById(R.id.item_post_count);
        }
    }


}

