package lee.glexercisedemo;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import lee.glexercisedemo.kits.ShaderUtils;
import lee.glexercisedemo.kits.TextureHelper;

/**
 * Created by li wen hao on 2017/3/23.
 *
 * @since 0.0.1
 */
public class GLRenderer implements GLSurfaceView.Renderer {

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
            0f, 2f,
            1f, 2f
    };
    private FloatBuffer textureBuffer;

    public GLRenderer(Context context) {
        this.context = context;
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
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        String vertexSource   = ShaderUtils.getRawText(context, R.raw.vertex_shader);
        String fragmentSource = ShaderUtils.getRawText(context, R.raw.fragment_shader);
        textureId = TextureHelper.loadTexture(context, R.raw.most_wanted);
        programId = ShaderUtils.createProgram(vertexSource, fragmentSource);
        aPositionHandle = GLES20.glGetAttribLocation(programId, "aPosition");
        aMatrixHandle = GLES20.glGetUniformLocation(programId, "uMatrix");

        uTextureSampleHandle = GLES20.glGetUniformLocation(programId, "sTexture");
        aTextureCoordHandle = GLES20.glGetAttribLocation(programId, "aTexCoord");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
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
        GLES20.glUseProgram(programId);
        GLES20.glUniformMatrix4fv(aMatrixHandle, 1, false, projectionMatrix, 0);
        GLES20.glEnableVertexAttribArray(aPositionHandle);
        GLES20.glVertexAttribPointer(aPositionHandle, 3, GLES20.GL_FLOAT, false, 12, vertexBuffer);
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,3);画三角形
        //传递纹理坐标
        GLES20.glEnableVertexAttribArray(aTextureCoordHandle);
        GLES20.glVertexAttribPointer(aTextureCoordHandle, 2, GLES20.GL_FLOAT, false, 8, textureBuffer);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        GLES20.glUniform1i(uTextureSampleHandle, 1);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexData.length, GLES20.GL_UNSIGNED_SHORT, indexBuffer);//画图
    }
}
