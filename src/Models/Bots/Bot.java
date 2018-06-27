package Models.Bots;

import Core.Physics.PhysicsEngine;
import Models.Shot;

public abstract class Bot {
    protected PhysicsEngine engine;
    protected double initialX, initialY;

    protected Bot(PhysicsEngine physicsEngine) {
        this.engine = physicsEngine;
    }

    public abstract Shot go();
}
