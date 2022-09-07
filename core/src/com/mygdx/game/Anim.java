package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Anim {
    private Texture img;
    private TextureAtlas atlas;
    private Animation<TextureRegion> anm;
    private float time;

    public Anim(String texture, int col, int row, Animation.PlayMode playMode, int... numb) {
        img = new Texture(texture);
        TextureRegion region0 = new TextureRegion(img);

        int xCnt = region0.getRegionWidth() / col;
        int yCnt = region0.getRegionHeight() / row;

        TextureRegion[][] regions = region0.split(xCnt, yCnt);

        TextureRegion[] regionsInRow = new TextureRegion[regions.length * regions[0].length];

        for (int i = 0, cnt = 0; i < regions.length; i++) {
            for (int j = 0; j < regions[0].length; j++) {
                regionsInRow[cnt++] = regions[i][j];
            }
        }

        for (int i = 0; i < numb.length; i++) {
            numb[i] -= 1;
        }

        TextureRegion[] regionSelected = new TextureRegion[numb.length];
        for (int i = 0, cnt = 0; i < numb.length; i++) {
            regionSelected[cnt++] = regionsInRow[numb[i]];
        }

        anm = new Animation<TextureRegion>(1/5f, regionSelected);
        anm.setPlayMode(playMode);
        time += Gdx.graphics.getDeltaTime();
    }

    public Anim(String atlasName, String animName, Float frameDuration, Animation.PlayMode playMode) {
        atlas = new TextureAtlas(atlasName);
        anm = new Animation<TextureRegion>(frameDuration, atlas.findRegions(animName));
        anm.setPlayMode(playMode);
        time += Gdx.graphics.getDeltaTime();
    }

    public TextureRegion getFrame() {return anm.getKeyFrame(time);}
    public void setTime(float time) {this.time += time;}
    public void zeroTime() {this.time = 0;}
    public boolean isAnimationOver() {return anm.isAnimationFinished(time);}
    public void setPlayMode(Animation.PlayMode playMode) {anm.setPlayMode(playMode);}

    public void dispose() {
        if (img != null) img.dispose();
        if (atlas !=null) atlas.dispose();
    }
}
