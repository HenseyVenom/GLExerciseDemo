package lee.glexercisedemo.ui;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by li wen hao on 2017/3/24.
 *
 * @since 0.0.1
 */

public class MediaStatus {
    public static final int IDLE=0;
    public static final int PREPARED=1;
    public static final int BUFFERING=2;
    public static final int PLAYING=3;
    public static final int PAUSE_BY_USER=4;
    public static final int PAUSED=5;
    public static final int STOPPED=6;
    public static final int COMPLETE=7;
    public static final int ERROR=8;
    @IntDef({IDLE,PREPARED,BUFFERING,PLAYING,PAUSE_BY_USER,PAUSED,STOPPED,COMPLETE,ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status{}


}
