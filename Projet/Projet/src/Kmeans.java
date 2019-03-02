import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class Kmeans {
	private int numberDigit = 10;
	int nbPixel = 28;
	private List<int[][]> images;
	private int N;;
	private static int[] labels;
	private int imax;

	private List<int[][]> lastCenter;
	public List<int[][]> center;
	private int[] centerLabels;
	private ArrayList<ArrayList<Integer>> classes;
	private int[] indicesClasses;


	public Kmeans(List<int[][]> images, int[] labels, int imax) {
		this.images = images;
		this.labels = labels;
		this.imax = imax;

		N = images.size();
		
		center = new ArrayList<int[][]>();
		lastCenter = new ArrayList<int[][]>();
		centerLabels = new int[numberDigit];
		indicesClasses = new int[numberDigit];
		classes = new ArrayList<ArrayList<Integer>>(10);

		for (int i = 0; i < 10; i++) {
			classes.add(new ArrayList<Integer>());
		}

		for (int i = 0; i < numberDigit; i++) {
			indicesClasses[i] = 0;
		}
	}


	public void initialize(){
		int indice;

		for (int i = 0; i < numberDigit; i++) {
			indice = (int) (Math.random() * N ) ;
			center.add(images.get((indice)));
			lastCenter.addAll(center);
		}
	}

	public void searchCenter(List<int[][]> images){
		for (int[][] matrice : images) {
			int min = 0;
			double dmin = Matrice.frobeniusNorm(Matrice.minus(matrice,
					center.get(0)));

			for (int[][] centre : center) {
				int[][] x = Matrice.minus(matrice, centre);
				double dx = Matrice.frobeniusNorm(x);
				if (dx < dmin) {
					dmin = dx;
					min = center.indexOf(centre);
				}
			}

			classes.get(min).add(images.indexOf(matrice));
			indicesClasses[min]++;
		}
	}

	public void calculateCenter(){
		for (int i = 0; i < numberDigit; i++) {
			int[][] somme = new int[nbPixel][nbPixel];
			for (int j = 0; j < indicesClasses[i]; j++) {
				somme = Matrice.plus(somme,
						images.get(classes.get(i).get(j)));
			}
			
			center.set(i, Matrice.normalize(somme, indicesClasses[i]));
		}
	}

	// Training : creation des classes de chiffres à partir de la base de
	// données
	public void training(double epsilon) {

		this.initialize();

		//Calcul les centres pour un nombre d'itération imax donné
		//for (int it = 0; it < imax; it++) {
		double distanceMax;
		int nb = 0;
		do{
			nb++;
			distanceMax = 0;
			
			//initialise les indicesClasses
			for (int i = 0; i < indicesClasses.length; i++) {
				indicesClasses[i] = 0;
			}

			classes.clear();

			for (int i = 0; i < 10; i++) {
				classes.add(new ArrayList<Integer>());
			}
			
			lastCenter.clear();
			lastCenter.addAll(center); 

			this.searchCenter(images);

			this.calculateCenter();
			
			for (int i=0 ; i<numberDigit ; i++){
				int[][] x = Matrice.minus(lastCenter.get(i), center.get(i));
				double dx = Matrice.frobeniusNorm(x);
				if (dx > distanceMax) {
					distanceMax = dx;
				}
			}
			System.out.println(distanceMax);
			
		} while(distanceMax > epsilon);
		System.out.println(nb);
	}

	public void statistics() {
		for (int i = 0; i < classes.size(); i++) {
			float[] stats = new float[numberDigit];

			for (int j = 0; j < classes.get(i).size(); j++) {
				stats[labels[classes.get(i).get(j)]] += 1 ;/// (float)classes.get(i).size();
			}

			System.out.print("[");
			for (int j = 0; j < stats.length; j++) {
				System.out.print(stats[j] + ", ");
			}

			int max = 0;
			for (int j = 1; j < stats.length; j++) {
				if (stats[max] < stats[j])
					max = j;
			}
			System.out.println("] " + max);
		}
	}
}
