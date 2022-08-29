package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
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
    private float STEP = 20;
    private Rectangle mapSize;
    private ShapeRenderer shapeRenderer;

    float x;
    float y;

    public GameScreen(Main game) {
        this.game = game;
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        //animation = new Anim("mesomorph.png", 9, 6, Animation.PlayMode.LOOP, 7,8,9);
        animation = new Anim("atlas/unnamed.atlas", Animation.PlayMode.LOOP);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        map = new TmxMapLoader().load("map/Tiles_map_project.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        map.getLayers().get("Объекты").getObjects().getByType(RectangleMapObject.class);
        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("Объекты").getObjects().get("Камера");

        camera.position.x = tmp.getRectangle().x;
        camera.position.y = tmp.getRectangle().y;

        tmp = (RectangleMapObject) (map.getLayers().get("Объекты").getObjects().get("Граница"));
        mapSize = tmp.getRectangle();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && mapSize.x < (camera.position.x - 1) ) camera.position.x -= STEP;
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)  && (mapSize.x + mapSize.width) > (camera.position.x + 1) ) camera.position.x += STEP;
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) camera.position.y += STEP;
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) camera.position.y -= STEP;

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) camera.zoom += 0.1f;
        if (Gdx.input.isKeyJustPressed(Input.Keys.O) && camera.zoom > 0) camera.zoom -= 0.1f;

        camera.update();
        ScreenUtils.clear(0, 0.5f, 0.5f, 1);

        animation.setTime(Gdx.graphics.getDeltaTime());


        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(animation.getFrame(), x, y);
        batch.end();

        mapRenderer.setView(camera);
        mapRenderer.render();


        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.rect(mapSize.x, mapSize.y, mapSize.width, mapSize.height);
        shapeRenderer.end();
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
