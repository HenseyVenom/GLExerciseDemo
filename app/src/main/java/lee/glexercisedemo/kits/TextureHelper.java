package lee.glexercisedemo.kits;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.support.annotation.IdRes;
import android.support.annotation.RawRes;
import android.util.Log;

/**
 * Created by li wen hao on 2017/3/24.
 *
 * @since 0.0.1
 */

public class TextureHelper {
    public static final String TAG = "TextureHelper";

    public static int loadTexture(Context context, @RawRes int resourceId) {
        int[] textureObject = new int[1];
        GLES20.glGenTextures(1, textureObject, 0);
        if (textureObject[0] == 0) {
            Log.e(TAG, "创建纹理失败");
            return 0;
        }
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, option);

        if (bitmap == null) {
            Log.e(TAG, "加载位图失败");
            GLES20.glDeleteTextures(1, textureObject, 0);
            return 0;
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObject[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return textureObject[0];
    }
}
