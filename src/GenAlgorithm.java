import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.util.ArrayList;

public class GenAlgorithm {

	private int generationSize; // number of Individuals in each generation
	private int individualSize; // equal to the number of cities - 1
	private int numCities; // equal to the number of cities
	private int crossOverRate; // number of Individuals that will be seleceted to crossover
	private int maxIterations; // max number of generations
	private float mutationRate; // frequency of mutations for each generation
	private int startCity; // index of the starting city
	private int targetFitness; // fitness the best Individual must reach
	private int tourneySize; // number of Individuals picked for tourneySelect
	private int[][] map; // 2D array of travel prices for each city
	private SelectionType selectionType; // determines the type of selection used

	public enum SelectionType {
		TOURNAMENT, ROULETTE;
	}

	public GenAlgorithm(SelectionType selectionType, int numCities, int[][] map) {

		this.generationSize = (numCities * 7);
		this.individualSize = numCities;
		this.numCities = numCities;
		this.crossOverRate = (int) ((generationSize * 0.7));
		this.maxIterations = 30000;
		this.mutationRate = (float) 0.4;
		// this.targetFitness = 7;
		this.tourneySize = (int) (generationSize * 0.2);
		this.map = map;
		this.selectionType = selectionType;

	}

	// ****************************************************************************

	// SELECTION FUNCTION(S)
	// ****************************************************************************

	public List<Individual> pickRandInd(List<Individual> list, int n) {
		Random r = new Random();
		int size = list.size();

		if (size < n) {
			return null;
		}

		for (int i = size - 1; i >= size - n; --i) {
			Collections.swap(list, i, r.nextInt(i + 1));
		}

		return list.subList(size - n, size);
	}

	public List<Individual> selection(List<Individual> population) {
		List<Individual> selected = new ArrayList<Individual>();

		for (int i = 0; i < crossOverRate; i++) {

			if (selectionType == SelectionType.TOURNAMENT) {

				Individual min = tourneySelection(population);
				population.remove(min);
				selected.add(min);

				// selected.add(tourneySelection(population));

			} else if (selectionType == SelectionType.ROULETTE) {
				selected.add(rouletteSelection(population));
			}

		}

		return selected;
	}

	public Individual tourneySelection(List<Individual> population) {
		List<Individual> selected = pickRandInd(population, tourneySize);

		int a;
		Individual min = selected.get(0);

		for (int i = 1; i < selected.size(); i++) {
			a = selected.get(i).getFitness();

			if (a < min.getFitness()) {
				min = selected.get(i);
			}
		}

		return min;
	}

	public Individual rouletteSelection(List<Individual> population) {

		int totalFitness = 0;
		for (Individual temp : population) {
			totalFitness += temp.getFitness();
		}

		Random r = new Random();
		int randValue = r.nextInt(totalFitness);

		float value = (float) 1 / randValue;

		float sum = 0;
		for (Individual temp : population) {
			sum += (float) (1 / temp.getFitness());

			if (sum >= value) {
				return temp;
			}
		}

		int selectRand = r.nextInt(generationSize);
		return population.get(selectRand);
	}

	public Individual min(List<Individual> population) {

		int a;
		Individual min = population.get(0);

		for (int i = 1; i < population.size(); i++) {
			a = population.get(i).getFitness();

			if (a < min.getFitness()) {
				min = population.get(i);
			}
		}

		return min;
	}

	// ****************************************************************************
	/*
	 * // SELECTION TEST METHOD public void bestSelected(List<Individual>
	 * population) {
	 * 
	 * int a; Individual min = population.get(0);
	 * 
	 * for (int i = 1; i < population.size(); i++) { a =
	 * population.get(i).getFitness();
	 * 
	 * if (a < min.getFitness()) { min = population.get(i); } }
	 * 
	 * min.printIndividual(); }
	 */
	// ****************************************************************************

	// CROSSOVER FUNCTION(S)
	// ****************************************************************************

	public List<Individual> crossover(List<Individual> parents) {
		List<Individual> children = new ArrayList<Individual>();

		List<Integer> parent1 = new ArrayList<Integer>(parents.get(0).getIndividual());
		List<Integer> parent2 = new ArrayList<Integer>(parents.get(1).getIndividual());

		// System.out.println("p1: " + parent1 + " p2: " + parent2);

		Random r = new Random();
		int genesCrossed = r.nextInt(individualSize);
		// System.out.println("break at index: " + genesCrossed);

		for (int i = 0; i <= genesCrossed; i++) {
			int gene;
			gene = parent2.get(i);
			Collections.swap(parent1, parent1.indexOf(gene), i);
		}

		children.add(new Individual(parent1, numCities, map, startCity));
		// System.out.print("Ch1:");
		// children.get(0).printIndividual();
		// System.out.println();

		parent1 = parents.get(0).getIndividual();

		for (int i = genesCrossed + 1; i < individualSize; i++) {
			int gene;
			gene = parent1.get(i);
			Collections.swap(parent2, parent2.indexOf(gene), i);
		}

		children.add(new Individual(parent2, numCities, map, startCity));
		// System.out.print("Ch2:");
		// children.get(1).printIndividual();
		// System.out.println();

		return children;
	}

	// MUTATION FUNCTION(S)
	// ****************************************************************************

	public Individual mutate(Individual individual) {

		Random r = new Random();
		float mutate = r.nextFloat();

		if (mutate <= mutationRate) {
			List<Integer> path = individual.getIndividual();
			Collections.swap(path, r.nextInt(individualSize), r.nextInt(individualSize));
		}

		return individual;
	}

	// CREATING NEW GENERATIONS FUNCTION(S)
	// ****************************************************************************

	public List<Individual> createGeneration(List<Individual> population) {

		List<Individual> generation = new ArrayList<Individual>();

		int currGenSize = 0;

		while (currGenSize < generationSize - 2) {

			List<Individual> parents = pickRandInd(population, 2);
			List<Individual> children = crossover(parents);
			children.set(0, mutate(children.get(0)));
			children.set(1, mutate(children.get(1)));
			generation.addAll(children);
			currGenSize += 2;
			Collections.shuffle(population);

		}

		return generation;
	}

	public List<Individual> findElite(List<Individual> pop) {
		List<Individual> elite = new ArrayList<>();

		for (int i = 0; i < 2; i++) {
			Individual min = min(pop);
			elite.add(min);
			pop.remove(min);
		}

		return elite;
	}

	public void printPopulation(List<Individual> population) {
		/*
		 * System.out.println("------------------------");
		 * System.out.println(" Individuals	Fitness");
		 * System.out.println("------------------------");
		 * 
		 * for (int i = 0; i < population.size(); i++) {
		 * population.get(i).printIndividual(); }
		 */
		Individual elite = min(population);
		System.out.println("------------------------");
		System.out.print("Best:");
		elite.printIndividual();
		System.out.println("------------------------\n");

	}

	// CREATE INITIAL POPULATION FUNCTION(S)
	// ****************************************************************************

	public List<Individual> intialPopulation() {
		Random r = new Random();
		List<Individual> pop = new ArrayList<Individual>();

		int rand;
		int i = 0;
		while (i != generationSize) {
			rand = r.nextInt(numCities);
			Individual path = new Individual(numCities, map, rand);

			if (!(path.containsIndividual(pop))) {
				pop.add(path);
				i++;
			}
		}

		// printPopulation(pop);
		return pop;

	}

	// TERMINATING FUNCTION(S)
	// ****************************************************************************

	public Individual optimize() {

		System.out.println("\n (Initial Population) ");

		List<Individual> population = intialPopulation();
		printPopulation(population);

		Individual best = population.get(0);

		for (int i = 1; i <= maxIterations; i++) {
			List<Individual> elite = findElite(population);
			List<Individual> selected = selection(population);

			/*
			 * System.out.println(" (Tourney Selected) "); printPopulation(selected);
			 */

			population = createGeneration(selected);
			population.addAll(elite);
			Collections.shuffle(population);

			System.out.println("\n (Generation: " + i + ")");
			printPopulation(population);

			best = min(population);
			if (best.getFitness() < targetFitness) {
				break;
			}
		}

		System.out.println("*************************");
		System.out.print("Final Path:");
		best.printIndividual();
		System.out.println("*************************");

		return best;
	}

}
