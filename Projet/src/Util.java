import java.util.ArrayList;
import java.util.List;

public class Util {

	/**
	 * @param list : liste initiale
	 * @param number : nombre d'éléments de la liste finale
	 * @return : liste réduite
	 */
	public static List<int[][]> reduceList(List<int[][]> list, int number) {

		List<int[][]> reducedList = new ArrayList<int[][]>();
		for (int i=0; i < number; i++){
			reducedList.add(list.get(i));
		}
		return reducedList;
	}
	
	/**
	 * @param list : liste initiale
	 * @param number : nombre d'éléments de la liste finale
	 * @param beginning : début des éléments à conserver
	 * @return : liste réduite
	 */
	public static List<int[][]> reduceList(List<int[][]> list, int number, int beginning) {

		List<int[][]> reducedList = new ArrayList<int[][]>();
		for (int i=0; i < number; i++){
			reducedList.add(list.get(beginning + i));
		}
		return reducedList;
	}

	/**
	 * @param vector : vecteur initial
	 * @param number : nombre d'éléments à conserver
	 * @return : vecteur réduit
	 */
	public static int[] reducedVector(int[] vector, int number) {

		int[] reducedVector = new int[number];
		for (int i=0; i < number; i++){
			reducedVector[i] = vector[i];
		}
		return reducedVector;
	}
}
