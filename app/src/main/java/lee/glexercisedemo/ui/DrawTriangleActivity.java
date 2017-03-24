package lee.glexercisedemo.ui;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import lee.glexercisedemo.GLRenderer;
import lee.glexercisedemo.R;
import lee.glexercisedemo.kits.ShaderUtils;

public class DrawTriangleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_triangle);
        GLSurfaceView surfaceView= (GLSurfaceView) findViewById(R.id.triangle_canvas);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setRenderer(new GLRenderer(this));
        surfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }
}
