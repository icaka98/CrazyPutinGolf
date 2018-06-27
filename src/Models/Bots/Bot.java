package Models.Bots;

import Core.Physics.PhysicsEngine;
import Models.Shot;

/**
 * @author Zhecho Mitev
 */
public abstract class Bot {
    protected PhysicsEngine engine;
    protected double initialX, initialY;

    protected Bot(PhysicsEngine physicsEngine) {
        this.engine = physicsEngine;
    }

    /**
     * Abstract method for executing a shot. Different for every algorithm.
     * @return the Shot to be preformed.
     */
    public abstract Shot go();
}
