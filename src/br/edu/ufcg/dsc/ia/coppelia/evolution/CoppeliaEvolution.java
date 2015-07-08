package br.edu.ufcg.dsc.ia.coppelia.evolution;

import java.util.List;
import java.util.Random;

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.watchmaker.framework.CachingFitnessEvaluator;
import org.uncommons.watchmaker.framework.CandidateFactory;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.FitnessEvaluator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.TerminationCondition;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;

import robocode.control.RobocodeEngine;
import br.edu.ufcg.dsc.ia.coppelia.evolution.chromossome.Coppelia;
import br.edu.ufcg.dsc.ia.coppelia.infrastructure.EvolutionRegister;
import br.edu.ufcg.dsc.ia.coppelia.util.LocalProperties;

public class CoppeliaEvolution {
    private static final int POPULATION_SIZE = 100;
    private static final int ELITISM = (int) Math.ceil(0.1 * POPULATION_SIZE);
    private static final int NUMBER_OF_GENERATIONS_PER_LOOP = 1;
    private static final TerminationCondition TERMINATION_CONDITION =
            new TerminationCondition() {
                @Override
                public boolean shouldTerminate(PopulationData<?> populationData) {
                    return populationData.getGenerationNumber() > NUMBER_OF_GENERATIONS_PER_LOOP;
                }
            };
    private static final CandidateFactory<Coppelia> CANDIDATE_FACTORY =
            new CoppeliaCandidateFactory();
    private static final EvolutionaryOperator<Coppelia> EVOLUTIONARY_OPERATOR =
            new CoppeliaEvolutionaryOperator();
    private static final FitnessEvaluator<Coppelia> FITNESS_EVALUATOR =
            new CachingFitnessEvaluator<Coppelia>(new CoppeliaFitnessEvaluator());
    private static final RouletteWheelSelection SELECTION_STRATEGY =
            new RouletteWheelSelection();
    private static final Random RANDOM_NUMBER_GENERATOR =
            new MersenneTwisterRNG();
    private static final EvolutionObserver<Coppelia> EVOLUTION_OBSERVER =
            new EvolutionObserver<Coppelia>() {
                @Override
                public void populationUpdate(PopulationData<? extends Coppelia> data) {
                    CoppeliaEvolution.robocodeEngine = newRobocodeEngine();
                }
            };
    private static final GenerationalEvolutionEngine<Coppelia> EVOLUTION_ENGINE =
            new GenerationalEvolutionEngine<Coppelia>(
                    CANDIDATE_FACTORY,
                    EVOLUTIONARY_OPERATOR,
                    FITNESS_EVALUATOR,
                    SELECTION_STRATEGY,
                    RANDOM_NUMBER_GENERATOR);
    static {
        EVOLUTION_ENGINE.addEvolutionObserver(EVOLUTION_OBSERVER);
        EVOLUTION_ENGINE.setSingleThreaded(true);
    }
    private static final EvolutionRegister EVOLUTION_REGISTER = EvolutionRegister.instance();
    private static final int NUMBER_OF_ITERATIONS = LocalProperties.instance().numberOfIterations();

    private static RobocodeEngine robocodeEngine = newRobocodeEngine();

    public static void main(String[] args) {
        runEvolution(NUMBER_OF_ITERATIONS);

        robocodeEngine.close();
    }

    public static RobocodeEngine robocodeEngine() {
        return robocodeEngine;
    }

    private static RobocodeEngine newRobocodeEngine() {
        RobocodeEngine engine = new RobocodeEngine(LocalProperties.instance().robocodeDirectory());
        engine.setVisible(false);
        return engine;
    }

    private static void runEvolution(int numberOfIterations) {
        for (int i = 0; i < numberOfIterations; i++) {
            evolvePopulation();
        }
    }

    private static void evolvePopulation() {
        if (EVOLUTION_REGISTER.isEmpty()) {
            EVOLUTION_REGISTER.put(NUMBER_OF_GENERATIONS_PER_LOOP, evolveRandomPopulation());
        } else {
            System.out.println("Retrieving generation " + EVOLUTION_REGISTER.lastGeneration());

            List<Coppelia> seedPopulation = EVOLUTION_REGISTER.lastPopulation();
            int newGeneration = EVOLUTION_REGISTER.lastGeneration() + NUMBER_OF_GENERATIONS_PER_LOOP;
            EVOLUTION_REGISTER.put(newGeneration, evolve(seedPopulation));

            System.out.println("Saved generation " + EVOLUTION_REGISTER.lastGeneration());
        }
    }

    private static List<EvaluatedCandidate<Coppelia>> evolveRandomPopulation() {
        return EVOLUTION_ENGINE.evolvePopulation(POPULATION_SIZE, ELITISM, TERMINATION_CONDITION);
    }

    private static List<EvaluatedCandidate<Coppelia>> evolve(List<Coppelia> seedPopulation) {
        return EVOLUTION_ENGINE.evolvePopulation(POPULATION_SIZE, ELITISM, seedPopulation, TERMINATION_CONDITION);
    }
}
