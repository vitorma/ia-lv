package br.edu.ufcg.dsc.ia.marvin.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.uncommons.maths.random.MersenneTwisterRNG;

import br.edu.ufcg.dsc.ia.marvin.evolution.chromossome.Action;
import br.edu.ufcg.dsc.ia.marvin.evolution.chromossome.Marvin;
import br.edu.ufcg.dsc.ia.marvin.infrastructure.MarvinWriter;

public class MarvinWriterTest {

    @Test
    public final void testFilename() {
        final Random rng = new MersenneTwisterRNG();
        List<Action> actions = new ArrayList<Action>();

        actions.add(new Action.SetAhead(rng));
        actions.add(new Action.SetFire(rng));
        actions.add(new Action.SetTurnRadarLeft(rng));

        Marvin marvin = new Marvin()
                .withRunActions(actions)
                .withHitRobotActions(actions)
                .withHitWallActions(actions)
                .withScanEnemyActions(actions)
                .withHitByBulletActions(actions);

        String srcFolder = "src/";
        String expectedFilename = srcFolder + marvin.getClass().getCanonicalName().replaceAll("\\.", "/");
        expectedFilename += marvin.instanceNumber();
        expectedFilename += ".java";

        assertEquals(expectedFilename, new MarvinWriter().filenameOf(marvin));
        System.out.println(new MarvinWriter().filenameOf(marvin));
    }
}
