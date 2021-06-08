import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Individual {

	private int[][] map;
	private List<Integer> path;
	private int n;
	private int startCity;
	private int fitness;
	
	public Individual(int n, int[][] map, int startCity) {
		this.n = n;
		this.startCity = startCity;
		this.path = randomIndividual();
		this.map = map;
		this.fitness = this.getFitness();

	}
	
	public Individual(List<Integer> newIndiv, int n, int[][] map, int startCity) {
		this.n = n;
		this.startCity = startCity;
		this.path = newIndiv;
		this.map = map;
		this.fitness = this.getFitness();

	}
	
	
	public List<Integer> randomIndividual(){
		List<Integer> rand = new ArrayList<Integer>();
		rand.add(startCity);
		List<Integer> temp = new ArrayList<Integer>();
		
		for(int i=0;i<n;i++) {
			
			if(i != startCity) temp.add(i);
		}
		Collections.shuffle(temp);
		rand.addAll(temp);
		
		return rand;
	}
	
	public List<Integer> getIndividual(){
		return this.path;
	}
	
	public int getFitness() {
		fitness = 0;
		int currCity = startCity;
		
		for(int i=1;i<path.size();i++) {
			fitness += map[currCity][path.get(i)];
			currCity = path.get(i);
		}
		
		fitness += map[path.get(path.size()-1)][startCity];
		
		return fitness;
	}
	
	public void printIndividual() {
		
		System.out.print(" {" + this.startCity);
		for(int i=1;i<path.size();i++) {
			
			
			System.out.print("-" + path.get(i));
		}
		System.out.print("}	" + getFitness());
		
		System.out.println();
	}
	
	public boolean containsIndividual(List<Individual> list) {
		List<Integer> curr;
		List<Integer> ind = this.path;
		
		for(Individual temp : list) {
			curr = temp.path;
			int ct = 0;
			
			for(int j=0;j<curr.size();j++) {
				if(curr.get(j) != ind.get(j)) {
					break;
				}
				++ct;
			}
			if(ct == curr.size()) {
				return true;
			}
		}
		
		return false;
	}
	
	public int[][] getMap(){
		return this.map;
	}
	
	public int getStartCity() {
		return this.startCity;
	}
	
	
	

}
