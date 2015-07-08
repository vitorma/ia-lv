package br.edu.ufcg.dsc.ia.marvin.evolution;

import java.io.FileNotFoundException;
import java.io.IOError;
import java.util.Random;

import br.edu.ufcg.dsc.ia.marvin.evolution.chromossome.Marvin;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import br.edu.ufcg.dsc.ia.marvin.evolution.chromossome.ActionFactory;
import br.edu.ufcg.dsc.ia.marvin.infrastructure.MarvinWriter;

public class MarvinCandidateFactory extends AbstractCandidateFactory<Marvin> {

    @Override
    public Marvin generateRandomCandidate(Random rng) {
        ActionFactory actionFactory = new ActionFactory(rng);

        Marvin candidate = new Marvin()
                .withRunActions(actionFactory.randomBodyForRunMethod())
                .withHitRobotActions(actionFactory.randomBodyForOnHitRobotMethod())
                .withHitWallActions(actionFactory.randomBodyForOnHitWallMethod())
                .withScanEnemyActions(actionFactory.randomBodyForOnScannedRobotMethod())
                .withHitByBulletActions(actionFactory.randomBodyForOnHitByBulletMethod());

        try {
            new MarvinWriter().write(candidate);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IOError(e);
        }

        System.out.println("Born candidate" + candidate.instanceNumber() + " by abiogenesis");

        return candidate;
    }
}
