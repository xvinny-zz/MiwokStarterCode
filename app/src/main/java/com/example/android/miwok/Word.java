package com.example.android.miwok;

/**
 * Created by Vinicius on 07/07/2017.
 * {@link Word} representa uma palavra de vocabulário que o usuário quer aprender.
 * Contém uma tradução padrão e uma tradução Miwok para a palavra selecionada.
 */
public class Word
{
    private String mDefaultTranslation;
    private String mMiwokTranslation;
    private int mImageResourceID = NO_IMAGE_PROVIDED;
    private int mAudioResourceID;

    private static final int NO_IMAGE_PROVIDED = -1;

    /**
     * Inicializar o objeto {@link Word}.
     * @param defaultTranslation Tradução padrão da palavra.
     * @param miwokTranslation Tradução Miwok da palavra.
     * @param audioResourceID Resource ID do áudio que será tocado nesta palavra.
     */
    public Word( String defaultTranslation, String miwokTranslation , int audioResourceID )
    {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioResourceID = audioResourceID;
    }

    /**
     * Inicializar o objeto {@link Word}.
     * @param defaultTranslation Tradução padrão da palavra.
     * @param miwokTranslation Tradução Miwok da palavra.
     * @param imageResourceID Resource ID da imagem que representa esta palavra.
     * @param audioResourceID Resource ID do áudio que será tocado nesta palavra.
     */
    public Word( String defaultTranslation, String miwokTranslation , int imageResourceID , int audioResourceID)
    {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImageResourceID = imageResourceID;
        mAudioResourceID = audioResourceID;
    }

    /**
     * Retornar a traduçào padrão da palavra.
     * @return {@link String} contendo a tradução padrão da palavra.
     */
    public String getDefaultTranslation()
    {
        return mDefaultTranslation;
    }

    /**
     * Retornar a traduçào Miwok da palavra.
     * @return {@link String} contendo a tradução padrão da palavra.
     */
    public String getMiwokTranslation()
    {
        return mMiwokTranslation;
    }

    /**
     * Retornar o ID do resource de imagem.
     * @return {@link int} contendo o ID do resource.
     */
    public int getImageResourceID()
    {
        return mImageResourceID;
    }

    /**
     * Verificar se há imagem atrelada ou não para esta palavra.
     * @return {@Link boolean} com o resultado.
     */
    public boolean hasImage()
    {
        return mImageResourceID != NO_IMAGE_PROVIDED;
    }

    /**
     * Retornar o ID do resource de áudio.
     * @return {@Link int} com o ID do resource de áudio.
     */
    public int getAudioResourceID()
    {
        return mAudioResourceID;
    }

    /**
     * Sobrecarga do método toString para a classe {@Link Word}.
     * @return {@Link String} contendo os dados da Word.
     */
    @Override
    public String toString()
    {
        return "Word{" +
                "mDefaultTranslation='" + mDefaultTranslation + '\'' +
                ", mMiwokTranslation='" + mMiwokTranslation + '\'' +
                ", mImageResourceID=" + mImageResourceID +
                ", mAudioResourceID=" + mAudioResourceID +
                '}';
    }
}
