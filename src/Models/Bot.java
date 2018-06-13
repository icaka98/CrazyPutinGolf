package Models;

import Core.PhysicsEngine;
import Utils.Shot;

public abstract class Bot {
    protected PhysicsEngine engine;
    protected double initialX, initialY;

    protected Bot(PhysicsEngine physicsEngine) {
        this.engine = physicsEngine;
    }

    public abstract Shot go();
}
