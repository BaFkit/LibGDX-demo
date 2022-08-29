package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.Main;

public class GameScreen implements Screen {
    private final Main game;
    private final SpriteBatch batch;
    Anim animation;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Vector2 mapPosition;

    boolean dir;
    float x;
    float y;
    float speed = 5;

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        //animation = new Anim("mesomorph.png", 9, 6, Animation.PlayMode.LOOP, 7,8,9);
        animation = new Anim("atlas/unnamed.atlas", Animation.PlayMode.LOOP);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        map = new TmxMapLoader().load("map/Tiles_map_project.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        mapPosition = new Vector2();

        map.getLayers().get("Объекты").getObjects().getByType(RectangleMapObject.class);
        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("Объекты").getObjects().get("Камера");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        camera.update();
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

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(animation.getFrame(), x, y);
        batch.end();

        mapRenderer.setView(camera);
        mapRenderer.render();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY)) {
            dispose();
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        this.batch.dispose();
        this.animation.dispose();
    }
}
