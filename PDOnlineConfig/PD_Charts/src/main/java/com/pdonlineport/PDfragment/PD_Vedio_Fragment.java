package com.pdonlineport.PDfragment;

import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.pdonlineport.R;
import com.pdonlineport.Utils.FullTextureView;

import java.io.IOException;

/**
 * Created by SONG on 2019/5/5 17:56.
 * The final explanation right belongs to author
 */
public class PD_Vedio_Fragment extends Fragment implements TextureView.SurfaceTextureListener, MediaPlayer.OnPreparedListener {
    private MediaPlayer m_mediaPlayer = new MediaPlayer();
    private View m_View;
    private FullTextureView m_vedioView;
    private Surface m_Surface;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       //m_mediaPlayer = inflater.inflate(R.id)

        m_View =   inflater.inflate(R.layout.pd_vedio_layout,container,false);
        InitInfo();
        return m_View;
    }

    private void InitInfo() {
        m_vedioView = m_View.findViewById(R.id.pd_vedio);
        m_vedioView.setSurfaceTextureListener(this);
		  m_vedioView.setRotation(90);
        m_vedioView.setScaleX(1280f / 720);
        m_vedioView.setScaleY(720 / 1280f);
    }

    /**
     *
     * @param url
     * @return  播放器是否准备成功
     */
    public boolean SetUrlAndStart(String url)
    {
        if(m_Surface==null)
        {
            return false;
        }
        m_mediaPlayer.reset();
        m_mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            m_mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        m_mediaPlayer.setSurface(m_Surface);
        m_mediaPlayer.prepareAsync();
        m_mediaPlayer.setOnPreparedListener(this);


        return true;

    }

    public void StopVedio()
    {
        if(m_mediaPlayer.isPlaying())
        {
            m_mediaPlayer.stop();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        m_Surface = new Surface(surface);
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

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }
}
