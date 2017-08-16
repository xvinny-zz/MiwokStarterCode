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

public class PhrasesActivity extends AppCompatActivity
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
        words.add( new Word( "Where are you going?", "minto wuksus", R.raw.phrase_where_are_you_going ) );
        words.add( new Word( "What is your name?", "tinnә oyaase'nә", R.raw.phrase_what_is_your_name ) );
        words.add( new Word( "My name is...", "oyaaset...", R.raw.phrase_my_name_is ) );
        words.add( new Word( "How are you feeling?", "michәksәs?", R.raw.phrase_how_are_you_feeling ) );
        words.add( new Word( "I’m feeling good.", "kuchi achit", R.raw.phrase_im_feeling_good ) );
        words.add( new Word( "Are you coming?", "әәnәs'aa?", R.raw.phrase_are_you_coming ) );
        words.add( new Word( "Yes, I’m coming.", "hәә’ әәnәm", R.raw.phrase_yes_im_coming ) );
        words.add( new Word( "I’m coming.", "әәnәm", R.raw.phrase_im_coming ) );
        words.add( new Word( "Let’s go.", "yoowutis", R.raw.phrase_lets_go ) );
        words.add( new Word( "Come here.", "әnni'nem", R.raw.phrase_come_here ) );


        WordAdapter adapter = new WordAdapter( this, words, R.color.category_phrases );
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

                releaseMediaPlayer();
                Log.v( "PhrasesActivity", "Current word: " + currentWord );

                int requestResult = mAudioManager.requestAudioFocus( mAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT );

                if ( requestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED )
                {
                    mediaPlayer = MediaPlayer.create( PhrasesActivity.this, currentWord.getAudioResourceID() );
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
