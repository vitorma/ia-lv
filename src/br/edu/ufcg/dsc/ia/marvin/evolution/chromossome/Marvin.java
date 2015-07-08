package br.edu.ufcg.dsc.ia.marvin.evolution.chromossome;

import java.util.LinkedList;
import java.util.List;


public class Marvin {
    private static int instanceCount = 0;

    private List<Action> runActions;
    private List<Action> hitWallActions;
    private List<Action> hitRobotActions;
    private List<Action> scanEnemyActions;
    private List<Action> hitByBulletActions;
    private int instanceNumber;

    public Marvin() {
        this.instanceNumber = Marvin.instanceCount;
        instanceCount += 1;
    }

    @Override
    public Marvin clone() {
        return new Marvin()
                .withHitRobotActions(new LinkedList<Action>(this.hitRobotActions()))
                .withHitWallActions(new LinkedList<Action>(this.hitWallActions()))
                .withHitWallActions(new LinkedList<Action>(this.hitWallActions()))
                .withRunActions(new LinkedList<Action>(this.runActions()))
                .withScanEnemyActions(new LinkedList<Action>(this.scanEnemyActions()))
                .withHitByBulletActions(new LinkedList<Action>(this.hitByBulletActions()));
    }

    public int instanceNumber() {
        return this.instanceNumber;
    }

    public Marvin withRunActions(List<Action> runActions) {
        this.runActions = runActions;
        return this;
    }

    public Marvin withHitWallActions(List<Action> hitWallActions) {
        this.hitWallActions = hitWallActions;
        return this;
    }

    public Marvin withHitRobotActions(List<Action> hitRobotActions) {
        this.hitRobotActions = hitRobotActions;
        return this;
    }

    public Marvin withScanEnemyActions(List<Action> scanEnemyActions) {
        this.scanEnemyActions = scanEnemyActions;
        return this;
    }

    public Marvin withHitByBulletActions(List<Action> hitByBulletActions) {
        this.hitByBulletActions = hitByBulletActions;
        return this;
    }

    public List<Action> runActions() {
        return this.runActions;
    }

    public List<Action> hitWallActions() {
        return this.hitWallActions;
    }

    public List<Action> hitRobotActions() {
        return this.hitRobotActions;
    }

    public List<Action> scanEnemyActions() {
        return this.scanEnemyActions;
    }

    public List<Action> hitByBulletActions() {
        return this.hitByBulletActions;
    }
}
