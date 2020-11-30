package org.techtown.yawakeup;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Share_Activity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mDatabase;

    private RecyclerView ListrecyclerView;
    private GestureDetector gd;

    private PostAdapter mAdapter;
    private List<Post> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        gd = new GestureDetector(getApplicationContext(),new GestureDetector.SimpleOnGestureListener() {

            //누르고 뗄 때 한번만 인식하도록 하기위해서
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        ListrecyclerView = findViewById(R.id.list_recycler_view);
        ListrecyclerView.addItemDecoration(new DividerItemDecoration(ListrecyclerView.getContext(), 1));
        ListrecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if(child != null && gd.onTouchEvent(e)){

                    int position = rv.getChildAdapterPosition(child);

                    //해당 위치의 Data를 가져옴
                    if(position != RecyclerView.NO_POSITION){
                        Post currentPost = mDatas.get(position);

                        Intent intent = new Intent(rv.getContext(), PostViewActivity.class);
                        intent.putExtra("title", currentPost.getTitle());
                        intent.putExtra("date", currentPost.getDate());
                        intent.putExtra("time", currentPost.getTime());
                       // intent.putExtra("place", currentPost.getPlace());
                       // intent.putExtra("count", currentPost.getCount());
                        intent.putExtra("contents", currentPost.getContents());

                        startActivity(intent);


                       // Toast.makeText(Share_Activity.this, "현재 터치한 Item의 Title은 " + currentPost.getTitle(), Toast.LENGTH_SHORT).show();
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

        findViewById(R.id.list_post_btn).setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatas = new ArrayList<>();
        mDatabase.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null) {
                    mDatas.clear();
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Map<String, Object> shot = (Map) snap.getValue();
                        String documentId = String.valueOf(shot.get("documentId"));
                        String title = String.valueOf(shot.get("title"));
                        String date = String.valueOf(shot.get("date"));
                        String time = String.valueOf(shot.get("time"));
                        String place = String.valueOf(shot.get("place"));
                        String count = String.valueOf(shot.get("count"));
                        String contents = String.valueOf(shot.get("contents"));
                        Post data = new Post(documentId, title, date, time, contents);
                        Log.v("DATACLEAR", title);
                        mDatas.add(data);
                    }
                    mAdapter = new PostAdapter(mDatas);
                    ListrecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Share_Activity.this, "data error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, NewPostActivity.class));
    }

    public void onItemClick(View view, int position){

    }
}
