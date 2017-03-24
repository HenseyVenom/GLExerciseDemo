package lee.glexercisedemo.ui;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import lee.glexercisedemo.GLMediaRenderer;
import lee.glexercisedemo.R;

public class MediaPlayerActivity extends AppCompatActivity {

    private GLSurfaceView mediaSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        mediaSurface = (GLSurfaceView) findViewById(R.id.media_surface);
        mediaSurface.setEGLContextClientVersion(2);
        mediaSurface.setRenderer(new GLMediaRenderer(this));
        mediaSurface.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
}
