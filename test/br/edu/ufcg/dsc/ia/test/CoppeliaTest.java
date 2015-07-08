package br.edu.ufcg.dsc.ia.test;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.edu.ufcg.dsc.ia.coppelia.evolution.chromossome.Coppelia;

public class CoppeliaTest {

    private Coppelia myRobot;

    @Before
    public void setUp() {
        this.myRobot = new Coppelia();
    }

    @Test
    public void testMyRobot() {
        Assert.assertEquals(this.myRobot, this.myRobot);
    }
}
