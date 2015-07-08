package br.edu.ufcg.dsc.ia.coppelia.evolution;

import java.io.FileNotFoundException;
import java.io.IOError;
import java.util.Random;

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import br.edu.ufcg.dsc.ia.coppelia.evolution.chromossome.ActionFactory;
import br.edu.ufcg.dsc.ia.coppelia.evolution.chromossome.Coppelia;
import br.edu.ufcg.dsc.ia.coppelia.infrastructure.CoppeliaWriter;

public class CoppeliaCandidateFactory extends AbstractCandidateFactory<Coppelia> {

    @Override
    public Coppelia generateRandomCandidate(Random rng) {
        ActionFactory actionFactory = new ActionFactory(rng);

        Coppelia candidate = new Coppelia()
                .withRunActions(actionFactory.randomBodyForRunMethod())
                .withHitRobotActions(actionFactory.randomBodyForOnHitRobotMethod())
                .withHitWallActions(actionFactory.randomBodyForOnHitWallMethod())
                .withScanEnemyActions(actionFactory.randomBodyForOnScannedRobotMethod())
                .withHitByBulletActions(actionFactory.randomBodyForOnHitByBulletMethod());

        try {
            new CoppeliaWriter().write(candidate);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IOError(e);
        }

        System.out.println("Born candidate" + candidate.instanceNumber() + " by abiogenesis");

        return candidate;
    }
}
