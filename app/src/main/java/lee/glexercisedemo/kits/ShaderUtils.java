package lee.glexercisedemo.kits;

import android.content.Context;
import android.opengl.GLES20;
import android.support.annotation.RawRes;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by li wen hao on 2017/3/23.
 *
 * @since 0.0.1
 */

public class ShaderUtils {
    private static final String TAG = "ShaderUtils";

    //    读取Raw文件
    public static final String getRawText(Context context, @RawRes int rawId) {
        InputStream rawStream = context.getResources().openRawResource(rawId);
        try {
            BufferedReader reader  = new BufferedReader(new InputStreamReader(rawStream));
            StringBuilder  builder = new StringBuilder();
            String         line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            reader.close();
            return builder.toString();
        } catch (IOException e) {
            Log.e(TAG, "getRawText: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * 加载gl脚本文件
     *
     * @param source     脚本文件转成的String
     * @param shaderType 着色器类型，有GLES20.GL_VERTEX_SHADER和GLES20.GL_FRAGMENT_SHADER
     * @return 非零
     */
    public static final int loadShader(String source, int shaderType) {
        int shader = GLES20.glCreateShader(shaderType);
        if (shader != 0) {
            GLES20.glShaderSource(shader, source);
            GLES20.glCompileShader(shader);
            int[] compiled = new int[1];
            //获取编译状态，0编译失败，删除shader
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                Log.e(TAG, "loadShader: Could not compile shader " + shaderType + ":");
                Log.e(TAG, "loadShader: " + GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    public static final void checkGLError(String label) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, label + " error: " + error);
            throw new RuntimeException(label + " error: " + error);
        }
    }

    public static final int createProgram(String vertex, String fragment) {
        int vertexShader = loadShader(vertex, GLES20.GL_VERTEX_SHADER);
        if (vertexShader == 0) {
            return 0;
        }
        int fragmentShader = loadShader(fragment, GLES20.GL_FRAGMENT_SHADER);
        if (fragmentShader == 0) {
            return 0;
        }
        int program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, vertexShader);
            checkGLError("attach shader");
            GLES20.glAttachShader(program, fragmentShader);
            checkGLError("attach shader");
            GLES20.glLinkProgram(program);
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                Log.e(TAG, "could not create program " + program + " :");
                Log.e(TAG, GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }
}
