package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.mygdx.game.Font;
import com.mygdx.game.Main;
import com.mygdx.game.PhysX;

import java.util.ArrayList;


public class GameScreen implements Screen {
    private final Main game;
    private final SpriteBatch batch;
    Anim animation;
    Anim animationStand;
    Anim animationRun;
    Anim animationJump;
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
    private final Texture imgChest;
    private int score;
    private final Font font;
    private final Array<RectangleMapObject> objects;
    private final Array<Rectangle> chests;
    private final int amount;

    public static ArrayList<Body> bodies;

    float x;
    float y;

    private boolean lookRight = true;

    public GameScreen(Main game) {
        bodies = new ArrayList<>();
        imgChest = new Texture("chest.png");
        this.game = game;
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new Font(28);
        font.setColor(Color.DARK_GRAY);
        amount = 5;

        animationStand = new Anim("atlas/Atlas.atlas", "stand", 1/5f, Animation.PlayMode.LOOP);
        animationRun = new Anim("atlas/Atlas.atlas", "run", 1/5f, Animation.PlayMode.LOOP);
        animationJump = new Anim("atlas/Atlas.atlas", "jump", 0.5f,Animation.PlayMode.NORMAL);
        animation = animationStand;

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 0.8f;
        music = Gdx.audio.newMusic(Gdx.files.internal("back_music.mp3"));
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

        objects = map.getLayers().get("Объекты").getObjects().getByType(RectangleMapObject.class);
        for (int i = 0; i < objects.size; i++) {
            physX.addObject(objects.get(i));
        }
        chests = new Array<>();
        for (int i = 0; i < objects.size; i++) {
            if (objects.get(i).getName().equals("Chest")) {
                chests.add(objects.get(i).getRectangle());
            }
        }
    }

    @Override
    public void show() {

    }

    private Anim setAnimation(Anim anim) {
        if (!this.animation.getFrame().isFlipX() && !lookRight) {
            anim.getFrame().flip(true, false);
        }
        if (this.animation.getFrame().isFlipX() && lookRight) {
            anim.getFrame().flip(true, false);
        }
        return anim;
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (physX.myContList.isOnGround()) animation = setAnimation(animationRun);
            lookRight = false;
            body.applyForceToCenter(new Vector2(-1.5f, 0), true);
        }else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (physX.myContList.isOnGround()) animation = setAnimation(animationRun);
            lookRight = true;
            body.applyForceToCenter(new Vector2(1.5f, 0), true);
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP) && physX.myContList.isOnGround()) {
            animation = setAnimation(animationJump);
            body.applyForceToCenter(new Vector2(0, 12f), true);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            camera.position.y -= STEP;
        } else if(physX.myContList.isOnGround()) {
            animation = setAnimation(animationStand);
        }
        camera.position.x = body.getPosition().x * physX.PPM;
        camera.position.y = body.getPosition().y * physX.PPM;
        camera.update();
        ScreenUtils.clear(0.69f, 0.88f, 0.97f, 1);



        heroRect.x = body.getPosition().x - heroRect.width / 2;
        heroRect.y = body.getPosition().y - heroRect.height / 2;

        mapRenderer.setView(camera);
        mapRenderer.render(bg);
        mapRenderer.render(l1);

        float x = Gdx.graphics.getWidth()/2 - heroRect.getWidth()/2/camera.zoom;
        float y = Gdx.graphics.getHeight()/2 - heroRect.getHeight()/2/camera.zoom;
        Sprite spr = new Sprite(animation.getFrame());
        spr.setOriginCenter();
        spr.scale(0f);
        spr.setPosition(x, y);
        animation.setTime(Gdx.graphics.getDeltaTime());
        batch.begin();
        spr.draw(batch);
        batch.end();

        batch.begin();
        for (int i = 0; i < chests.size; i++) {
            x = Gdx.graphics.getWidth()/2 + (chests.get(i).x - camera.position.x) / camera.zoom;
            y = Gdx.graphics.getHeight()/2 + (chests.get(i).y - camera.position.y) / camera.zoom;
            batch.draw(imgChest, x, y, chests.get(i).width, chests.get(i).height);
        }
        batch.end();

        batch.begin();
        font.render(batch, "Chests collected: " + String.valueOf(score), 0, Gdx.graphics.getHeight());
        batch.end();

        physX.step();
        physX.debugDraw(camera);

        for (int i = 0; i < bodies.size(); i++) {
            physX.destroyBody(bodies.get(i));
            score++;
        }
        bodies.clear();

        if(score == amount) {
            game.setScreen(new GameScreen(game));
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
        this.animationStand.dispose();
        this.animationJump.dispose();
        this.animationRun.dispose();
        this.imgChest.dispose();
    }
}
