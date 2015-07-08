package br.edu.ufcg.dsc.ia.coppelia.infrastructure;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.uncommons.watchmaker.framework.EvaluatedCandidate;

import br.edu.ufcg.dsc.ia.coppelia.evolution.chromossome.Coppelia;
import br.edu.ufcg.dsc.ia.coppelia.util.Comparables;
import br.edu.ufcg.dsc.ia.coppelia.util.Files;

import com.thoughtworks.xstream.persistence.FilePersistenceStrategy;
import com.thoughtworks.xstream.persistence.XmlMap;

public class EvolutionRegister {
    private static final File DIRECTORY = Files.ensuredDirectory("data");
    private static final Integer INVALID_GENERATION = -1;

    private static EvolutionRegister instance = new EvolutionRegister();

    private Map<Integer, List<EvaluatedCandidate<Coppelia>>> entries;

    private EvolutionRegister() {
        this.entries = this.entriesFromXmlFile();
    }

    public static EvolutionRegister instance() {
        return instance;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public int size() {
        return this.entries.size();
    }

    public int firstGeneration() {
        return Comparables.minimum(this.entries.keySet(), INVALID_GENERATION);
    }

    public int lastGeneration() {
        return Comparables.maximum(this.entries.keySet(), INVALID_GENERATION);
    }

    public List<Coppelia> firstPopulation() {
        return this.populationOfGeneration(this.firstGeneration());
    }

    public List<EvaluatedCandidate<Coppelia>> firstEvaluatedPopulation() {
        return this.evaluatedPopulationOfGeneration(this.firstGeneration());
    }

    public List<Coppelia> lastPopulation() {
        return this.populationOfGeneration(this.lastGeneration());
    }

    public List<EvaluatedCandidate<Coppelia>> lastEvaluatedPopulation() {
        return this.evaluatedPopulationOfGeneration(this.lastGeneration());
    }

    public List<Coppelia> populationOfGeneration(int generation) {
        List<Coppelia> result = new LinkedList<Coppelia>();

        for (EvaluatedCandidate<Coppelia> ecc : this.evaluatedPopulationOfGeneration(generation)) {
            result.add(ecc.getCandidate());
        }

        return result;
    }

    public List<EvaluatedCandidate<Coppelia>> evaluatedPopulationOfGeneration(int generation) {
        return this.entries.get(generation);
    }

    public Map<Integer, List<Coppelia>> allPopulations() {
        Map<Integer, List<Coppelia>> result = new HashMap<Integer, List<Coppelia>>();

        for (int generation : this.entries.keySet()) {
            result.put(generation, this.populationOfGeneration(generation));
        }

        return result;
    }

    public Map<Integer, List<EvaluatedCandidate<Coppelia>>> allEvaluatedPopulations() {
        Map<Integer, List<EvaluatedCandidate<Coppelia>>> result = new HashMap<Integer, List<EvaluatedCandidate<Coppelia>>>();
        result.putAll(this.entries);
        return result;
    }

    public EvolutionRegister put(int generation, List<EvaluatedCandidate<Coppelia>> population) {
        this.entries.put(generation, population);
        return this;
    }

    public EvolutionRegister remove(int generation) {
        this.entries.remove(generation);
        return this;
    }

    /* AUXILIARY */

    @SuppressWarnings("unchecked")
    private Map<Integer, List<EvaluatedCandidate<Coppelia>>> entriesFromXmlFile() {
        return new XmlMap(new FilePersistenceStrategy(DIRECTORY));
    }
}
