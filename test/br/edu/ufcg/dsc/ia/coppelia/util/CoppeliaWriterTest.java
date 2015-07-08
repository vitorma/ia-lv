package br.edu.ufcg.dsc.ia.coppelia.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.uncommons.maths.random.MersenneTwisterRNG;

import br.edu.ufcg.dsc.ia.coppelia.evolution.chromossome.Action;
import br.edu.ufcg.dsc.ia.coppelia.evolution.chromossome.Coppelia;
import br.edu.ufcg.dsc.ia.coppelia.infrastructure.CoppeliaWriter;

public class CoppeliaWriterTest {

    @Test
    public final void testFilename() {
        final Random rng = new MersenneTwisterRNG();
        List<Action> actions = new ArrayList<Action>();

        actions.add(new Action.SetAhead(rng));
        actions.add(new Action.SetFire(rng));
        actions.add(new Action.SetTurnRadarLeft(rng));

        Coppelia coppelia = new Coppelia()
                .withRunActions(actions)
                .withHitRobotActions(actions)
                .withHitWallActions(actions)
                .withScanEnemyActions(actions)
                .withHitByBulletActions(actions);

        String srcFolder = "src/";
        String expectedFilename = srcFolder + coppelia.getClass().getCanonicalName().replaceAll("\\.", "/");
        expectedFilename += coppelia.instanceNumber();
        expectedFilename += ".java";

        assertEquals(expectedFilename, new CoppeliaWriter().filenameOf(coppelia));
        System.out.println(new CoppeliaWriter().filenameOf(coppelia));
    }
}
