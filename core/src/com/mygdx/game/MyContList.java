package com.mygdx.game;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.screens.GameScreen;

public class MyContList implements ContactListener {

    private int count;

    public boolean isOnGround() {
        return count > 0;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("Hero") && tmpB.equals("Chest")) {
                GameScreen.bodies.add(b.getBody());
            }
            if (tmpB.equals("Hero") && tmpA.equals("Chest")) {
                GameScreen.bodies.add(a.getBody());
            }
            if (tmpA.equals("Legs") && tmpB.equals("Wall")) {
                count++;
            }
            if (tmpB.equals("Wall") && tmpA.equals("Legs")) {
                count++;
            }
        }
    }
    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        if (a.getUserData() != null && b.getUserData() != null) {
            String tmpA = (String) a.getUserData();
            String tmpB = (String) b.getUserData();
            if (tmpA.equals("Legs") && tmpB.equals("Wall")) {
                count--;
            }
            if (tmpB.equals("Wall") && tmpA.equals("Legs")) {
                count--;
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
