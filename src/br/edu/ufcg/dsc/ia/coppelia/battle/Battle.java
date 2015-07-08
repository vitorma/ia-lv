package br.edu.ufcg.dsc.ia.coppelia.battle;

import robocode.BattleResults;
import robocode.control.BattleSpecification;
import robocode.control.BattlefieldSpecification;
import robocode.control.RobocodeEngine;
import robocode.control.RobotResults;
import robocode.control.RobotSpecification;
import robocode.control.events.BattleAdaptor;
import robocode.control.events.BattleCompletedEvent;
import robocode.control.events.IBattleListener;

public class Battle {
    private static final String SEPARATOR = ",";
    private static final boolean WAIT_TILL_OVER = true;

    private RobocodeEngine engine;
    private RobotSpecification[] opponents;
    private BattlefieldSpecification battlefield;
    private int numberOfRounds;
    private BattleResults[] results;

    private IBattleListener listener = new BattleAdaptor() {
        @Override
        public void onBattleCompleted(BattleCompletedEvent event) {
            Battle.this.results = event.getSortedResults();
        }
    };

    public Battle(RobocodeEngine engine) {
        this.engine = engine;
        this.engine.addBattleListener(this.listener);
    }

    public Battle between(String robot1, String robot2) {
        this.opponents = this.engine.getLocalRepository(robot1 + SEPARATOR + robot2);
        return this;
    }

    public Battle on(BattlefieldSpecification battlefield) {
        this.battlefield = battlefield;
        return this;
    }

    public Battle withNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
        return this;
    }

    public Battle run() {
        this.engine.runBattle(this.buildSpecification(), WAIT_TILL_OVER);
        return this;
    }

    public BattleResults[] getResults() {
        return this.results;
    }

    public RobotResults getResultsOf(String robot) {
        return new RobotResults(this.robotSpecificationOf(robot), this.battleResultsOf(robot));
    }

    /* AUXILIARY */

    private BattleSpecification buildSpecification() {
        return new BattleSpecification(this.numberOfRounds, this.battlefield, this.opponents);
    }

    private RobotSpecification robotSpecificationOf(String robot) {
        for (RobotSpecification rs : this.opponents) {
            if (rs.getName().equals(robot)) {
                return rs;
            }
        }
        return null;
    }

    private BattleResults battleResultsOf(String robot) {
        for (BattleResults br : this.results) {
            if (br.getTeamLeaderName().equals(robot)) {
                return br;
            }
        }
        return null;
    }
}
