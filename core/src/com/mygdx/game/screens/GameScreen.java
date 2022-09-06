package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Anim;
import com.mygdx.game.Main;
import com.mygdx.game.PhysX;

import java.util.ArrayList;
import java.util.Iterator;


public class GameScreen implements Screen {
    private final Main game;
    private final SpriteBatch batch;
    Anim animation;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private float STEP = 20;
    private ShapeRenderer shapeRenderer;
    private PhysX physX;
    private Body body;
    private final Rectangle heroRect;
    private final Music music;
    private final int[] bg;
    private final int[] l1;

    public static ArrayList<Body> bodies;

    float x;
    float y;

    public GameScreen(Main game) {
        bodies = new ArrayList<>();
        this.game = game;
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        //animation = new Anim("mesomorph.png", 9, 6, Animation.PlayMode.LOOP, 7,8,9);
        animation = new Anim("atlas/unnamed.atlas", Animation.PlayMode.LOOP);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.5f;
        music = Gdx.audio.newMusic(Gdx.files.internal("game_music.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();




        map = new TmxMapLoader().load("map/Tiles_map_project.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        bg = new int[1];
        bg[0] = map.getLayers().getIndex("Фон");
        l1 = new int[2];
        l1[0] = map.getLayers().getIndex("Слой 2");
        l1[1] = map.getLayers().getIndex("Слой 3");

        physX = new PhysX();
        map.getLayers().get("Объекты").getObjects().getByType(RectangleMapObject.class);
        RectangleMapObject tmp = (RectangleMapObject) map.getLayers().get("Setting").getObjects().get("Hero");
        heroRect = tmp.getRectangle();
        body = physX.addObject(tmp);

        Array<RectangleMapObject> objects = map.getLayers().get("Объекты").getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < objects.size; i++) {
            physX.addObject(objects.get(i));
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) body.applyForceToCenter(new Vector2(- 1000000, 0), true);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) body.applyForceToCenter(new Vector2(1000000, 0), true);
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) body.applyForceToCenter(new Vector2(0, 10000000), true);
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) camera.position.y -= STEP;

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) camera.zoom += 0.1f;
        if (Gdx.input.isKeyJustPressed(Input.Keys.O) && camera.zoom > 0) camera.zoom -= 0.1f;

        camera.position.x = body.getPosition().x;
        camera.position.y = body.getPosition().y;
        camera.update();
        ScreenUtils.clear(0.69f, 0.88f, 0.97f, 1);

        animation.setTime(Gdx.graphics.getDeltaTime());




        mapRenderer.setView(camera);
        mapRenderer.render(bg);


//        shapeRenderer.setProjectionMatrix(camera.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(Color.YELLOW);
//        for (int i = 0; i < objects.size; i++) {
//            Rectangle mapSize = objects.get(i).getRectangle();
//            shapeRenderer.rect(mapSize.x, mapSize.y, mapSize.width, mapSize.height);
//        }
//        shapeRenderer.end();

        mapRenderer.render(l1);

        batch.setProjectionMatrix(camera.combined);
        heroRect.x = body.getPosition().x - heroRect.width/2;
        heroRect.y = body.getPosition().y - heroRect.height/2;
        batch.begin();
        batch.draw(animation.getFrame(), heroRect.x, heroRect.y, heroRect.width, heroRect.height);
        batch.end();

        physX.step();
        physX.debugDraw(camera);

        for (int i = 0; i < bodies.size(); i++) {
            physX.destroyBody(bodies.get(i));
        }
        bodies.clear();
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
