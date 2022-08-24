package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Anim animation;
	boolean dir;
	float x;
	float y;
	float speed = 5;

	@Override
	public void create () {
		batch = new SpriteBatch();
		//animation = new Anim("mesomorph.png", 9, 6, Animation.PlayMode.LOOP, 7,8,9);
		animation = new Anim("atlas/unnamed.atlas", Animation.PlayMode.LOOP);

	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0.5f, 0.5f, 1);

		animation.setTime(Gdx.graphics.getDeltaTime());

		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
			dir = true;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
			dir = false;
		}

		x = x + speed;

		if (animation.getFrame().isFlipX() && !dir) {
			animation.getFrame().flip(true, false);
			speed = 5;
		}
		if (!animation.getFrame().isFlipX() && dir) {
			animation.getFrame().flip(true, false);
			speed = -5;
		}

		if (x > Gdx.graphics.getWidth() - animation.getFrame().getRegionWidth()) {
			dir = true;
		}
		if (x < 0) {
			dir = false;
		}

		batch.begin();
		batch.draw(animation.getFrame(), x, y);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		animation.dispose();
	}
}
