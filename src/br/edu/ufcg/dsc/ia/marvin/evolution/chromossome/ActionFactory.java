package br.edu.ufcg.dsc.ia.marvin.evolution.chromossome;

import java.util.List;
import java.util.Random;

import br.edu.ufcg.dsc.ia.marvin.util.FluentList;

public class ActionFactory {
    private Random rng;

    public ActionFactory(Random rng) {
        this.rng = rng;
    }

    public List<Action> randomBodyForRunMethod() {
        return this.randomBodyFrom(this.actionsForMovingRobotAndRadar());
    }

    public List<Action> randomBodyForOnScannedRobotMethod() {
        return this.randomBodyFrom(this.actionsForMovingAndFiring());
    }

    public List<Action> randomBodyForOnHitWallMethod() {
        return this.randomBodyFrom(this.actionsForMovingRobot());
    }

    public List<Action> randomBodyForOnHitRobotMethod() {
        return this.randomBodyFrom(this.actionsForMovingRobotAndRadar());
    }

    public List<Action> randomBodyForOnHitByBulletMethod() {
        return this.randomBodyFrom(this.actionsForMovingRobot());
    }

    /* AUXILIARY */

    private FluentList<Action> randomBodyFrom(FluentList<Action> actions) {
        FluentList<Action> body = new FluentList<Action>();

        int numberOfActions = this.rng.nextInt(actions.size() * 2);

        for (int i = 0; i < numberOfActions; i++) {
            int actionIndex = this.rng.nextInt(actions.size());
            body.including(this.actionOrOpposite(actions.get(actionIndex)));
        }

        return body;
    }

    private Action actionOrOpposite(Action action) {
        if (this.rng.nextBoolean()) {
            return action;
        }

        return action.opposite();
    }

    private FluentList<Action> actionsForMovingRobot() {
        return new FluentList<Action>()
                .including(new Action.SetAhead(this.rng))
                .including(new Action.SetTurnRight(this.rng));
    }

    private FluentList<Action> actionsForMovingRobotAndRadar() {
        return this.actionsForMovingRobot()
                .including(new Action.SetTurnRadarRight(this.rng));
    }

    private FluentList<Action> actionsForMovingAndFiring() {
        return this.actionsForMovingRobotAndRadar()
                .including(new Action.SetTurnGunRight(this.rng))
                .including(new Action.SetFire(this.rng));
    }
}