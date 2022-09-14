package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Font {
    private BitmapFont cyrillicFont;

    public Font(int size){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Staccato555TLRegular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.characters = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm" +
                "-+=0123456789.";
        cyrillicFont = generator.generateFont(parameter);
        generator.dispose();
    }

    public void render (SpriteBatch batch, String str, int x, int y) {cyrillicFont.draw(batch, str, x, y);}

    public int getHeight() {return (int) cyrillicFont.getCapHeight()+1;}
    public void setColor(Color color) {cyrillicFont.setColor(color);}

    public void dispose() {
        cyrillicFont.dispose();
    }

}
