package br.edu.ufcg.dsc.ia.marvin.infrastructure;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.edu.ufcg.dsc.ia.marvin.evolution.chromossome.Marvin;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;

import br.edu.ufcg.dsc.ia.marvin.util.Comparables;
import br.edu.ufcg.dsc.ia.marvin.util.Files;

import com.thoughtworks.xstream.persistence.FilePersistenceStrategy;
import com.thoughtworks.xstream.persistence.XmlMap;

public class EvolutionRegister {
    private static final File DIRECTORY = Files.ensuredDirectory("data");
    private static final Integer INVALID_GENERATION = -1;

    private static EvolutionRegister instance = new EvolutionRegister();

    private Map<Integer, List<EvaluatedCandidate<Marvin>>> entries;

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

    public List<Marvin> firstPopulation() {
        return this.populationOfGeneration(this.firstGeneration());
    }

    public List<EvaluatedCandidate<Marvin>> firstEvaluatedPopulation() {
        return this.evaluatedPopulationOfGeneration(this.firstGeneration());
    }

    public List<Marvin> lastPopulation() {
        return this.populationOfGeneration(this.lastGeneration());
    }

    public List<EvaluatedCandidate<Marvin>> lastEvaluatedPopulation() {
        return this.evaluatedPopulationOfGeneration(this.lastGeneration());
    }

    public List<Marvin> populationOfGeneration(int generation) {
        List<Marvin> result = new LinkedList<Marvin>();

        for (EvaluatedCandidate<Marvin> ecc : this.evaluatedPopulationOfGeneration(generation)) {
            result.add(ecc.getCandidate());
        }

        return result;
    }

    public List<EvaluatedCandidate<Marvin>> evaluatedPopulationOfGeneration(int generation) {
        return this.entries.get(generation);
    }

    public Map<Integer, List<Marvin>> allPopulations() {
        Map<Integer, List<Marvin>> result = new HashMap<Integer, List<Marvin>>();

        for (int generation : this.entries.keySet()) {
            result.put(generation, this.populationOfGeneration(generation));
        }

        return result;
    }

    public Map<Integer, List<EvaluatedCandidate<Marvin>>> allEvaluatedPopulations() {
        Map<Integer, List<EvaluatedCandidate<Marvin>>> result = new HashMap<Integer, List<EvaluatedCandidate<Marvin>>>();
        result.putAll(this.entries);
        return result;
    }

    public EvolutionRegister put(int generation, List<EvaluatedCandidate<Marvin>> population) {
        this.entries.put(generation, population);
        return this;
    }

    public EvolutionRegister remove(int generation) {
        this.entries.remove(generation);
        return this;
    }

    /* AUXILIARY */

    @SuppressWarnings("unchecked")
    private Map<Integer, List<EvaluatedCandidate<Marvin>>> entriesFromXmlFile() {
        return new XmlMap(new FilePersistenceStrategy(DIRECTORY));
    }
}
