package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity
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
        words.add( new Word( "father", "әpә", R.drawable.family_father, R.raw.family_father ) );
        words.add( new Word( "mother", "әṭa", R.drawable.family_mother, R.raw.family_mother ) );
        words.add( new Word( "son", "angsi", R.drawable.family_son, R.raw.family_son ) );
        words.add( new Word( "daughter", "tune", R.drawable.family_daughter, R.raw.family_daughter ) );
        words.add( new Word( "older brother", "taachi", R.drawable.family_older_brother, R.raw.family_older_brother ) );
        words.add( new Word( "younger brother", "chalitti", R.drawable.family_younger_brother, R.raw.family_younger_brother ) );
        words.add( new Word( "older sister", "teṭe", R.drawable.family_older_sister, R.raw.family_older_sister ) );
        words.add( new Word( "younger sister", "kolliti", R.drawable.family_younger_sister, R.raw.family_younger_sister ) );
        words.add( new Word( "grandmother", "ama", R.drawable.family_grandmother, R.raw.family_grandmother ) );
        words.add( new Word( "grandfather", "paapa", R.drawable.family_grandfather, R.raw.family_grandfather ) );

        WordAdapter adapter = new WordAdapter( this, words, R.color.category_family );
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

                Log.v( "FamilyActivity", "Current word: " + currentWord );

                releaseMediaPlayer();

                int requestResult = mAudioManager.requestAudioFocus( mAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT );

                if ( requestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED )
                {
                    mediaPlayer = MediaPlayer.create( FamilyActivity.this, currentWord.getAudioResourceID() );
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
