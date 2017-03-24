package lee.glexercisedemo.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.TextView;

import lee.glexercisedemo.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.draw_triangle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (false)
                    startActivity(new Intent(getApplicationContext(), DrawTriangleActivity.class));
            }
        });
        findViewById(R.id.play_media).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MediaPlayerActivity.class));
            }
        });
    }
}
