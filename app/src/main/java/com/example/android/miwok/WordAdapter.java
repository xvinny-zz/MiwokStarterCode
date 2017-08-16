package com.example.android.miwok;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Vinicius on 07/07/2017.
 */

public class WordAdapter extends ArrayAdapter<Word>
{

    /**
     * ID da cor de fundo de cada item.
     */
    private int mColorResourceID;

    /**
     * Inicializar o objeto {@link WordAdapter}.
     * O contexto é usado para inflar o arquivo de layout, e a lista são os dados que
     * queremos popular nas lists.
     * @param context Contexto atual. USado para inflar o arquivo de layout.
     * @param words Lista de palavras a serem exibidas.
     * @param colorResourceID ID do resource da cor de plano de fundo.
     */
    public WordAdapter( Activity context, ArrayList<Word> words , int colorResourceID)
    {
        /* Construtor do ArrayAdapter:
        *  ArrayAdapter(Context context, int resource, List<T> objects)
        *  Passamos 0 no 2º parâmetro (resource) porque não precisamos depender
        *  da superclasse para que infle ou crie uma exibição do item da lista.
        *  Ao inves disso, o método getView vai se responsabilizar por inflar o layout,
        *  usando o recurso do ID do layout.*/
        super( context, 0, words );
        mColorResourceID = colorResourceID;
    }

    /**
     * Retornar uma view para uma AdapterView (ListView, GridView, etc.).
     * @param position Posição na lista que esse layout deve representar.
     * @param convertView A view reciclada que precisa ser repopulada.
     * @param parent O ViewGroup pai que é usado para inflar. Os listItems vão ser adicionados como filhos desse parent ViewGroup.
     * @return A View para a posição na AdapterView.
     */
    @NonNull
    @Override
    public View getView( int position, @Nullable View convertView, @NonNull ViewGroup parent )
    {
        Word currentWord = getItem( position );

        View listItemView = convertView;

        // A convertView pode ser nula quando abrimos a activity pela primeira vez e
        // criamos os itens da lista pela primeira vez nela.
        // Se não houver uma listItemView pronta, temos que inflar uma nova,
        // que irá gerar uma nova View no formato da list_item.xml (LinearLayout com 2 TextViews dentro)
        // O LayoutInflater traduz o XML num objeto View real.
        if (listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate( R.layout.list_item, parent, false);
        }

        TextView defaultTranslationTextView = (TextView) listItemView.findViewById( R.id.default_text_view );
        defaultTranslationTextView.setText( currentWord.getDefaultTranslation() );

        TextView miwokTranslationTextView = (TextView) listItemView.findViewById( R.id.miwok_text_view );
        miwokTranslationTextView.setText( currentWord.getMiwokTranslation() );

        ImageView imageView = (ImageView) listItemView.findViewById( R.id.image );

        if (  currentWord.hasImage() )
        {
            imageView.setImageResource( currentWord.getImageResourceID() );

            imageView.setVisibility( View.VISIBLE );
        }
        else
        {
            imageView.setVisibility( View.GONE );
        }

        View textContainerView = listItemView.findViewById( R.id.text_container );
        int color = ContextCompat.getColor( getContext(), mColorResourceID );
        textContainerView.setBackgroundColor( color );


        return listItemView;
    }
}
