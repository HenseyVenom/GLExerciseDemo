package lee.glexercisedemo;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.Surface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import lee.glexercisedemo.kits.ShaderUtils;

/**
 * Created by li wen hao on 2017/3/24.
 *
 * @since 0.0.1
 */

public class GLMediaRenderer implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    Context context;
    private int programId;
    private int aPositionHandle;
    private int aMatrixHandle;

    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;
    private float[] projectionMatrix = new float[16];

    private float[] vertexPosition = {
            1f, 1f, 0f,
            -1f, 1f, 0f,
            -1f, -1f, 0f,
            1f, -1f, 0f
    };
    private short[] indexData      = {
            0, 1, 2,
            2, 3, 0
    };

    private int textureId;
    private int aTextureCoordHandle;
    private int uTextureSampleHandle;
    private float[] textureVertexData = {
            1f, 0f,
            0f, 0f,
            0f, 1f,
            1f, 1f
    };
    private FloatBuffer textureBuffer;

    private final MediaPlayer player;

    private boolean        updateSurface;
    private SurfaceTexture surfaceTexture;
    private float[] sTMatrix = new float[16];
    private int uSTMatrixHandle;
    private int screenWidth;
    private int screenHeight;

    public GLMediaRenderer(Context context) {
        this.context = context;

        synchronized (this) {
            updateSurface = false;
        }
//        我们在类的构造函数中把顶点数据传递过去
        vertexBuffer = ByteBuffer.allocateDirect(vertexPosition.length * 4)//在本地内存分配的大小
                .order(ByteOrder.nativeOrder())//设置存储顺序
                .asFloatBuffer()
                .put(vertexPosition);
//        设定索引位置
        vertexBuffer.position(0);
        indexBuffer = ByteBuffer.allocateDirect(indexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(indexData);
        indexBuffer.position(0);
        textureBuffer = ByteBuffer.allocateDirect(textureVertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(textureVertexData);
        textureBuffer.position(0);
        player = MediaPlayer.create(context, R.raw.demo_video);
//        try {
//            player.setDataSource(context, Uri.parse(videoPath));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setLooping(true);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertexSource   = ShaderUtils.getRawText(context, R.raw.media_vertex_shader);
        String fragmentSource = ShaderUtils.getRawText(context, R.raw.media_fragment_shader);

        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        textureId = texture[0];
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
        ShaderUtils.checkGLError("glBindTexture textureId");
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        programId = ShaderUtils.createProgram(vertexSource, fragmentSource);
        aPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
        aMatrixHandle = GLES20.glGetUniformLocation(programId, "uMatrix");

        uTextureSampleHandle = GLES20.glGetUniformLocation(programId, "sTexture");
        aTextureCoordHandle = GLES20.glGetAttribLocation(programId, "aTexCoord");
        uSTMatrixHandle = GLES20.glGetAttribLocation(programId, "uSTMatrix");

        surfaceTexture = new SurfaceTexture(textureId);
        surfaceTexture.setOnFrameAvailableListener(this);
        Surface surface = new Surface(surfaceTexture);
        player.setSurface(surface);
        surface.release();

        player.start();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        screenWidth = width;
        screenHeight = height;
        float ratio = width > height ? (float) width / height : (float) height / width;
        if (width > height) {
            Matrix.orthoM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, -1f, 1f);
        } else {
            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -ratio, ratio, -1f, 1f);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        synchronized (this) {
            if (updateSurface) {
                surfaceTexture.updateTexImage();
                surfaceTexture.getTransformMatrix(sTMatrix);
                updateSurface = false;
            }
        }

        GLES20.glUseProgram(programId);
        GLES20.glUniformMatrix4fv(aMatrixHandle, 1, false, projectionMatrix, 0);
//        每次绘制的时候，将mSTMatrix用类似的方法传给OpenGL
        GLES20.glUniformMatrix4fv(uSTMatrixHandle, 1, false, sTMatrix, 0);
        vertexBuffer.position(0);
        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,3);画三角形
        //传递纹理坐标
        textureBuffer.position(0);
        GLES20.glEnableVertexAttribArray(aTextureCoordHandle);
        GLES20.glVertexAttribPointer(aTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 8, textureBuffer);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
        GLES20.glUniform1i(uTextureSampleHandle, 1);
        GLES20.glViewport(0, 0, screenWidth, screenHeight);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
//        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexData.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);//画图
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        updateSurface = true;
    }

}
