package org.techtown.yawakeup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PostViewActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTitle, mDate, mTime, mPlace, mCount, mContents;

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton edit, modify, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        mTitle = findViewById(R.id.post_view_title);
        mDate = findViewById(R.id.post_view_date);
        mTime = findViewById(R.id.post_view_time);
     //   mPlace = findViewById(R.id.post_view_place);
     //   mCount = findViewById(R.id.post_view_count);
        mContents = findViewById(R.id.post_view_contents);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.post_fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.post_fab_close);

        edit = (FloatingActionButton) findViewById(R.id.post_edit_fad);
        modify = (FloatingActionButton) findViewById(R.id.post_modify_fad);
        delete = (FloatingActionButton) findViewById(R.id.post_delete_fad);

        edit.setOnClickListener(this);
        modify.setOnClickListener(this);
        delete.setOnClickListener(this);


        Intent intent = getIntent();

        mTitle.setText(intent.getStringExtra("title"));
        mDate.setText("날짜 : " + intent.getStringExtra("date"));
        mTime.setText("시간 : " + intent.getStringExtra("time"));
      //  mPlace.setText("장소 : " + intent.getStringExtra("place"));
     //   mCount.setText("모집인원 : " + intent.getStringExtra("count"));
        mContents.setText("내용 : " + intent.getStringExtra("contents"));

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.post_edit_fad:
                anim();
                Toast.makeText(this, "Floating Action Button", Toast.LENGTH_SHORT).show();
                break;
            case R.id.post_modify_fad:
                anim();
                Toast.makeText(this, "modify", Toast.LENGTH_SHORT).show();
                break;
            case R.id.post_delete_fad:
                anim();
                Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void anim() {

        if (isFabOpen) {
            edit.setImageResource(R.drawable.ic_add);
            delete.startAnimation(fab_close);
            modify.startAnimation(fab_close);
            delete.setClickable(false);
            modify.setClickable(false);
            isFabOpen = false;
        } else {
            edit.setImageResource(R.drawable.ic_close);
            delete.startAnimation(fab_open);
            modify.startAnimation(fab_open);
            delete.setClickable(true);
            modify.setClickable(true);
            isFabOpen = true;
        }
    }
}