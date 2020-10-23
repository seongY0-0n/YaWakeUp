package org.techtown.yawakeup.alarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.yawakeup.Post;
import org.techtown.yawakeup.R;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.PostViewHolder> {

    private List<Room> datas;

    public RoomAdapter(List<Room> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public RoomAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Room data = datas.get(position);
        holder.title.setText(data.getTitle());
        holder.time.setText("시간 : " + data.getTime());
        holder.count.setText("1/" + data.getCount());
    }

    public int getItemCount(){
        return datas.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView time;
        private TextView count;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.item_room_title);
            time = itemView.findViewById(R.id.item_room_time);
            count = itemView.findViewById(R.id.item_room_count);
        }
    }


}

