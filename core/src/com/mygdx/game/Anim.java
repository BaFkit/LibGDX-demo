package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Anim {
    private Texture img;
    private Animation<TextureRegion> anm;
    private float time;

    public Anim(String name, int col, int row, Animation.PlayMode playMode) {
        img = new Texture(name);
        TextureRegion region0 = new TextureRegion(img);

        int xCnt = region0.getRegionWidth() / col;
        int yCnt = region0.getRegionHeight() / row;

        TextureRegion[][] regions = region0.split(xCnt, yCnt);

//        TextureRegion[] region1 = new TextureRegion[regions0.length * regions0[0].length];
//
//        for (int i = 0, cnt = 0; i < regions0.length; i++) {
//            for (int j = 0; j < regions0[0].length; j++) {
//                region1[cnt++] = regions0[i][j];
//            }
//        }

        TextureRegion[] regionSelected = new TextureRegion[3];
        for (int i = 6, cnt = 0; i <= 8; i++) {
            regionSelected[cnt++] = regions[0][i];
        }

        anm = new Animation<TextureRegion>(1/5f, regionSelected);
        anm.setPlayMode(playMode);

        time += Gdx.graphics.getDeltaTime();
    }

    public TextureRegion getFrame() {return anm.getKeyFrame(time);}
    public void setTime(float time) {this.time += time;}
    public void zeroTime() {this.time = 0;}
    public boolean isAnimationOver() {return anm.isAnimationFinished(time);}
    public void setPlayMode(Animation.PlayMode playMode) {anm.setPlayMode(playMode);}

    public void dispose() {
        img.dispose();
    }
}
