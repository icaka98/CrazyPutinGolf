package Models;

import Core.PhysicsEngine;
import Utils.Shot;

public abstract class Bot {
    protected PhysicsEngine engine;

    protected double initialX;
    protected double initialY;

    public Bot(PhysicsEngine physicsEngine) {
        engine = physicsEngine;
    }

    public abstract Shot go();
}
