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
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity
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
        words.add( new Word( "one", "lutti", R.drawable.number_one, R.raw.number_one ) );
        words.add( new Word( "two", "otiiko", R.drawable.number_two, R.raw.number_two ) );
        words.add( new Word( "three", "tolookosu", R.drawable.number_three, R.raw.number_three ) );
        words.add( new Word( "four", "oyyisa", R.drawable.number_four, R.raw.number_four ) );
        words.add( new Word( "five", "massokka", R.drawable.number_five, R.raw.number_five ) );
        words.add( new Word( "six", "temmokka", R.drawable.number_six, R.raw.number_six ) );
        words.add( new Word( "seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven ) );
        words.add( new Word( "eight", "kawinta", R.drawable.number_eight, R.raw.number_eight ) );
        words.add( new Word( "nine", "wo'e", R.drawable.number_nine, R.raw.number_nine ) );
        words.add( new Word( "ten", "na'aacha", R.drawable.number_ten, R.raw.number_ten ) );

        //PopulateScreenUsingTextViews( words );
        //PopulateScreenUsingArrayAdapter( words );
        PopulateGridViewUsingArrayAdapter( words );
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        releaseMediaPlayer();
    }

    private void PopulateGridViewUsingArrayAdapter( ArrayList<Word> words )
    {
        WordAdapter adapter = new WordAdapter( this, words, R.color.category_numbers );
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

                Log.v( "NumbersActivity", "Current word: " + currentWord );

                releaseMediaPlayer();

                int requestResult = mAudioManager.requestAudioFocus( mAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT );

                if ( requestResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED )
                {
                    // Agora que temos foco do áucio, vamos criar o MediaPlayer e tocar o áudio
                    mediaPlayer = MediaPlayer.create( NumbersActivity.this, currentWord.getAudioResourceID() );
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener( mCompletionListener );
                }
            }
        } );
    }

    /**
     * Liberar os recursos usados pelo Media Player.
     */
    private void releaseMediaPlayer()
    {
        if ( mediaPlayer != null )
        {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mAudioManager.abandonAudioFocus( mAudioFocusChangeListener );
    }

    /*private void PopulateScreenUsingArrayAdapter( ArrayList<String> words )
    {
        // Criando um array adapter que tem a lista de palavras.
        // O construtor leva o contexto atual, o arquivo de layout e a lista de objetos.
        // o segundo parametro é o layout R.layout que é o simple_list_item_1, padrão do Android
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, words);
        ListView listView = (ListView) findViewById( R.id.list);
        // Relacionando o adapter com o ListView
        listView.setAdapter(itemsAdapter);
    }*/

    /*private void PopulateScreenUsingTextViews( ArrayList<String> words )
    {
        ListView rootView = (ListView) findViewById( R.id.list );

        for (String word : words)
        {
            TextView wordView = new TextView( this );
            wordView.setText( word );
            rootView.addView( wordView );
        }
    }*/
}
