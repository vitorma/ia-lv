package br.edu.ufcg.dsc.ia.test;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.edu.ufcg.dsc.ia.marvin.evolution.chromossome.Marvin;

public class MarvinTest {

    private Marvin myRobot;

    @Before
    public void setUp() {
        this.myRobot = new Marvin();
    }

    @Test
    public void testMyRobot() {
        Assert.assertEquals(this.myRobot, this.myRobot);
    }
}
