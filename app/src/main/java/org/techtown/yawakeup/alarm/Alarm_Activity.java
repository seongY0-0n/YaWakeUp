package org.techtown.yawakeup.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.techtown.yawakeup.NewPostActivity;
import org.techtown.yawakeup.Post;
import org.techtown.yawakeup.PostAdapter;
import org.techtown.yawakeup.PostViewActivity;
import org.techtown.yawakeup.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Alarm_Activity extends Activity implements View.OnClickListener{

    private DatabaseReference mDatabase;

    private RecyclerView ListrecyclerView;
    private GestureDetector gd;

    private RoomAdapter mAdapter;
    private List<Room> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        gd = new GestureDetector(getApplicationContext(),new GestureDetector.SimpleOnGestureListener() {

            //누르고 뗄 때 한번만 인식하도록 하기위해서
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        ListrecyclerView = findViewById(R.id.recycelView);
        ListrecyclerView.addItemDecoration(new DividerItemDecoration(ListrecyclerView.getContext(), 1));
        ListrecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gd.onTouchEvent(e)){

                    int position = rv.getChildAdapterPosition(child);

                    //해당 위치의 Data를 가져옴
                    if(position != RecyclerView.NO_POSITION){
                        Room currentRoom = mDatas.get(position);

                        Intent intent = new Intent(rv.getContext(), RoomView_Activity.class);
                        intent.putExtra("title", currentRoom.getTitle());
                        intent.putExtra("date", currentRoom.getDate());
                        intent.putExtra("time", currentRoom.getTime());
                        intent.putExtra("count", currentRoom.getCount());
                        intent.putExtra("contents", currentRoom.getContents());
                        startActivity(intent);

                    }
                    return true;
                }
                return false;
            }


            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        findViewById(R.id.postBtn).setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatas = new ArrayList<>();
        mDatabase.child("rooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    mDatas.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Map<String, Object> shot = (Map) snap.getValue();
                        shot.get("key");
                        String documentId = String.valueOf(shot.get("documentId"));
                        String title = String.valueOf(shot.get("title"));
                        String date = String.valueOf(shot.get("date"));
                        String time = String.valueOf(shot.get("time"));
                        String count = String.valueOf(shot.get("count"));
                        String contents = String.valueOf(shot.get("contents"));

                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        String newTime = format.format(Integer.parseInt(time));

                        Room data = new Room(documentId, title, date, newTime, count, contents);
                        Log.v("DATACLEAR", title);
                        mDatas.add(data);
                    }
                    mAdapter = new RoomAdapter(mDatas);
                    ListrecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Alarm_Activity.this, "data error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View v) {

        startActivity(new Intent(this, NewRoomActivity.class));
    }

    public void onItemClick(View view, int position){

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
