package com.feicuiedu.hunttreasure;


import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileDescriptor;
import java.io.IOException;


/**
 * 用于视频播放的Fragment
 */
public class MainMP4Fragment extends Fragment implements TextureView.SurfaceTextureListener {


    private TextureView mTextureView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mTextureView = new TextureView(getContext());
        return mTextureView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 设置
        mTextureView.setSurfaceTextureListener(this);
    }

    // 确实准备好了
    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture surface, int width, int height) {
        try {
            AssetFileDescriptor afd = getContext().getAssets().openFd("welcome.mp4");
            FileDescriptor fd = afd.getFileDescriptor();
            final MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fd,afd.getStartOffset(),afd.getLength());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Surface mySurface = new Surface(surface);
                    mediaPlayer.setSurface(mySurface);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
