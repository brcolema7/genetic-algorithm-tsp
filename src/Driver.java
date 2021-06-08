import java.io.FileReader;
import java.util.Scanner;

public class Driver {

	private static Scanner in;
	private static Scanner sc;
	private static FileReader fr;
	private static int n;
	private static GenAlgorithm genA;

	public static void main(String[] args) {

		System.out.println("Enter a File: ");

		try {
			
			in = new Scanner(System.in);
			String input = in.nextLine();
			in.close();
			
			// String input = "tsp.txt";
			// String input = "tsp2.txt";

			fr = new FileReader(input);
			sc = new Scanner(fr);

			n = sc.nextInt();
			System.out.println("\nNumber of Cities: " + n);
			System.out.println();

			int[][] arr = mapCities();
			printMap(arr);

			genA = new GenAlgorithm(GenAlgorithm.SelectionType.TOURNAMENT, n, arr);

			genA.optimize();

			fr.close();
			sc.close();
		} catch (Exception e) {
			e.getMessage();
			System.out.println("\nFILE DOES NOT EXIST!");
		}

	}

	// ****************************************************************************
	// ****************************************************************************

	public static int[][] mapCities() {
		int[][] map = new int[n][n];

		try {

			for (int i = 0; i < map.length; i++) {

				for (int j = 0; j < map[i].length; j++) {
					map[i][j] = sc.nextInt();

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}

	// ****************************************************************************

	public static void printMap(int[][] map) {

		for (int i = 0; i < map.length; i++) {

			for (int j = 0; j < map[i].length; j++) {

				System.out.print(map[i][j] + " ");

			}
			System.out.println();
		}
		System.out.println();

	}

	// ****************************************************************************

}
