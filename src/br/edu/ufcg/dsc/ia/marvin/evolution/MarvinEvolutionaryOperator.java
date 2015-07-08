package br.edu.ufcg.dsc.ia.marvin.evolution;

import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import br.edu.ufcg.dsc.ia.marvin.evolution.chromossome.Marvin;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import br.edu.ufcg.dsc.ia.marvin.evolution.chromossome.Action;
import br.edu.ufcg.dsc.ia.marvin.infrastructure.MarvinWriter;
import br.edu.ufcg.dsc.ia.marvin.util.FluentList;

public class MarvinEvolutionaryOperator implements EvolutionaryOperator<Marvin> {
    private static final double MUTATION_PROBABILITY = 0.05;
    private static final double CROSSOVER_PROBABILITY = 0.5;

    @Override
    public List<Marvin> apply(List<Marvin> selectedCandidates, Random rng) {
        MarvinWriter writer = new MarvinWriter();
        int numberOfSelectedCandidates = selectedCandidates.size();

        List<Marvin> clones = new LinkedList<Marvin>();

        for (int i = 0; i < numberOfSelectedCandidates; ++i) {
            Marvin clone = selectedCandidates.get(i).clone();

            this.performCrossover(clone, selectedCandidates, rng);
            this.performMutations(clone, rng);

            clones.add(clone);

            try {
                writer.write(clone);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return clones;
    }

    private void performCrossover(Marvin clone, List<Marvin> others, Random rng) {
        Marvin other = others.get(rng.nextInt(others.size()));

        clone.withRunActions(this.crossover(clone.runActions(), other.runActions(), rng))
                .withScanEnemyActions(this.crossover(clone.scanEnemyActions(), other.scanEnemyActions(), rng))
                .withHitWallActions(this.crossover(clone.hitWallActions(), other.hitWallActions(), rng))
                .withHitRobotActions(this.crossover(clone.hitRobotActions(), other.hitRobotActions(), rng))
                .withHitByBulletActions(this.crossover(clone.hitByBulletActions(), other.hitByBulletActions(), rng));
    }

    private List<Action> crossover(List<Action> left, List<Action> right, Random rng) {
        return rng.nextDouble() > CROSSOVER_PROBABILITY ? new LinkedList<Action>(left) : new LinkedList<Action>(right);
    }

    private void performMutations(Marvin clone, Random rng) {
        List<List<Action>> actions = new FluentList<List<Action>>()
                .including(clone.hitRobotActions())
                .including(clone.hitWallActions())
                .including(clone.runActions())
                .including(clone.scanEnemyActions())
                .including(clone.hitByBulletActions());

        for (List<Action> list : actions) {
            for (int i = 0; i < list.size(); ++i) {
                if (rng.nextDouble() < MUTATION_PROBABILITY) {
                    Action action = list.remove(i);
                    list.add(i, action.opposite());
                }
            }
        }
    }
}
