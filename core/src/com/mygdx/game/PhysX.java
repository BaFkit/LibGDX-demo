package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

public class PhysX {
    private final World world;
    private final Box2DDebugRenderer debugRenderer;
    public final MyContList myContList;
    public final float PPM = 100;

    public PhysX() {
        world = new World(new Vector2(0, - 9.81f), true);
        myContList = new MyContList();
        world.setContactListener(myContList);
        debugRenderer = new Box2DDebugRenderer();

    }

    public Body addObject(RectangleMapObject object) {
        Rectangle rect = object.getRectangle();
        String type = (String) object.getProperties().get("BodyType");
        BodyDef def = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape polygonShape = new PolygonShape();

        if (type.equals("StaticBody")) def.type = BodyDef.BodyType.StaticBody;
        if (type.equals("DynamicBody")) def.type = BodyDef.BodyType.DynamicBody;

        def.position.set((rect.x + rect.width/2)/PPM, (rect.y + rect.height/2)/PPM);
        def.gravityScale = (float) object.getProperties().get("gravityScale");

        polygonShape.setAsBox(rect.width/2/PPM, rect.height/2/PPM);

        fdef.shape = polygonShape;
        fdef.friction = 1;
        fdef.density = 1;
        fdef.restitution = (float) object.getProperties().get("restitution");;

        Body body;
        body = world.createBody(def);
        String name = object.getName();
        body.createFixture(fdef).setUserData(name);
        if (name != null && name.equals("Hero")) {
            polygonShape.setAsBox(rect.width/3/PPM, rect.height/12/PPM, new Vector2(0, - rect.width/1.8f/PPM), 0);
            body.setFixedRotation(true);
            body.createFixture(fdef).setUserData("Legs");
        }
        polygonShape.dispose();
        return body;
    }

//    public Array<Body> getBodies(String name){
//        Array<Body> b = new Array<>();
//        world.getBodies(b);
//        Iterator<Body> it = b.iterator();
//        return b;
//    }

    public void setGravity(Vector2 gravity) {world.setGravity(gravity);}

    public void step() {world.step(1/60.0f, 3, 3);}

    public void debugDraw(OrthographicCamera cam) {debugRenderer.render(world, cam.combined);}

    public void destroyBody(Body body) {world.destroyBody(body);}

    public void dispose() {
        world.dispose();
        debugRenderer.dispose();
    }



}
