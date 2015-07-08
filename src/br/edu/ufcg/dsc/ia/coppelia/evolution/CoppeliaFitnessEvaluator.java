package br.edu.ufcg.dsc.ia.coppelia.evolution;

import java.util.List;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import robocode.BattleResults;
import robocode.control.BattlefieldSpecification;
import br.edu.ufcg.dsc.ia.coppelia.battle.Battle;
import br.edu.ufcg.dsc.ia.coppelia.evolution.chromossome.Coppelia;

public class CoppeliaFitnessEvaluator implements FitnessEvaluator<Coppelia> {
    private static final String OPPONENT = "sample.SpinBot";
    private static final BattlefieldSpecification BATTLEFIELD = new BattlefieldSpecification();
    private static final int NUMBER_OF_ROUNDS = 20;

    @Override
    public double getFitness(Coppelia candidate, List<? extends Coppelia> population) {

        String candidateName = nameOf(candidate);

        Battle battle = new Battle(CoppeliaEvolution.robocodeEngine())
                .between(candidateName, OPPONENT)
                .on(BATTLEFIELD)
                .withNumberOfRounds(NUMBER_OF_ROUNDS)
                .run();

        double totalScore = 0;

        for (BattleResults b : battle.getResults()) {
            totalScore += b.getScore();
        }

        double score = new Double(battle.getResultsOf(candidateName).getScore()) / totalScore;

        System.out.println("Score (" + candidate.instanceNumber() + ") : " + score);

        return score;
    }

    @Override
    public boolean isNatural() {
        return true;
    }

    private static String nameOf(Coppelia robot) {
        return robot.getClass().getName() + robot.instanceNumber() + "*";
    }
}
