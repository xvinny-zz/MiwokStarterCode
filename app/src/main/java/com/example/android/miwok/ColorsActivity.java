package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity
{
    // Gerencia a reprodução dos arquivos de áudio.
    private MediaPlayer mediaPlayer;

    // Gerencia o foco do áudio ao tocar um arquivo de áudio.
    private AudioManager mAudioManager;

    // Listener que verifica a finalização da reprodução do áudio
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener()
    {
        @Override
        public void onCompletion( MediaPlayer mp )
        {
            releaseMediaPlayer();
        }
    };

    // Listener para as mudanças de foco do áudio
    private AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener()
    {
        @Override
        public void onAudioFocusChange( int focusChange )
        {
            switch ( focusChange )
            {
                case AudioManager.AUDIOFOCUS_LOSS:
                    releaseMediaPlayer();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    if ( mediaPlayer != null && mediaPlayer.isPlaying() )
                    {
                        mediaPlayer.pause();
                        mediaPlayer.seekTo( 0 );
                    }
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    if ( mediaPlayer != null )
                        mediaPlayer.start();
                    break;
            }
        }
    };

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {

        super.onCreate( savedInstanceState );
        setContentView( R.layout.word_list );

        mAudioManager = (AudioManager) getSystemService( Context.AUDIO_SERVICE );

        ArrayList<Word> words = new ArrayList<Word>();
        words.add( new Word( "red", "weṭeṭṭi", R.drawable.color_red, R.raw.color_red ) );
        words.add( new Word( "green", "chokokki", R.drawable.color_green, R.raw.color_green ) );
        words.add( new Word( "brown", "ṭakaakki", R.drawable.color_brown, R.raw.color_brown ) );
        words.add( new Word( "gray", "ṭopoppi", R.drawable.color_gray, R.raw.color_gray ) );
        words.add( new Word( "black", "kululli", R.drawable.color_black, R.raw.color_black ) );
        words.add( new Word( "white", "kelelli", R.drawable.color_white, R.raw.color_white ) );
        words.add( new Word( "dusty yellow", "ṭopiisә", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow ) );
        words.add( new Word( "mustard yellow", "chiwiiṭә", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow ) );

        WordAdapter adapter = new WordAdapter( this, words, R.color.category_colors );
        ListView listView = (ListView) findViewById( R.id.list );
        listView.setAdapter( adapter );
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick( AdapterView<?> parent, View view, int position, long id )
            {
                Word currentWord = (Word) parent.getAdapter().getItem( position );
                // ou...
                // Word currentWord = words.get(position)
                // mas neste caso, é necessário colocar o modificador "final" na declaração do ArrayList<Word> words.

                Log.v( "ColorsActivity", "Current word: " + currentWord );

                releaseMediaPlayer();

                int requestResult = mAudioManager.requestAudioFocus( mAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT );

                if ( requestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED )
                {
                    mediaPlayer = MediaPlayer.create( ColorsActivity.this, currentWord.getAudioResourceID() );
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener( mCompletionListener );
                }
            }
        } );
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        releaseMediaPlayer();
    }

    /**
     * Liberar os recursos usados pelo Media Player.
     */
    private void releaseMediaPlayer()
    {
        if (mediaPlayer != null)
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mAudioManager.abandonAudioFocus( mAudioFocusChangeListener );
    }
}
