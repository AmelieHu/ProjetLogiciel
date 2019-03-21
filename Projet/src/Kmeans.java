import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class Kmeans {
	private int numberCluster;
	public int nbPixel = 28;
	private List<int[][]> images;
	private int N;;
	private static int[] labels;
	private int imax;

	private List<int[][]> lastCenter;
	public List<int[][]> center;
	private int[] centerLabels;
	private ArrayList<ArrayList<Integer>> classes;
	private int[] indicesClasses;


	public Kmeans(List<int[][]> images, int[] labels, int imax, int numberCluster) {
		this.numberCluster = numberCluster; 
		this.images = images;
		this.labels = labels;
		this.imax = imax;

		N = images.size();
		
		center = new ArrayList<int[][]>();
		lastCenter = new ArrayList<int[][]>();
		centerLabels = new int[numberCluster];
		indicesClasses = new int[numberCluster];
		classes = new ArrayList<ArrayList<Integer>>(numberCluster);

		for (int i = 0; i < numberCluster; i++) {
			classes.add(new ArrayList<Integer>());
		}

		for (int i = 0; i < numberCluster; i++) {
			indicesClasses[i] = 0;
		}
	}


	public void initialize(){
		int indice;

		for (int i = 0; i < numberCluster; i++) {
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
		for (int i = 0; i < numberCluster; i++) {
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

			for (int i = 0; i < numberCluster; i++) {
				classes.add(new ArrayList<Integer>());
			}
			
			lastCenter.clear();
			lastCenter.addAll(center); 

			this.searchCenter(images);

			this.calculateCenter();
			
			for (int i=0 ; i<numberCluster ; i++){
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
		//On calcule les statistiques selon chaque groupe
		for (int i = 0; i < classes.size(); i++) {
			float[] stats = new float[numberCluster];

			//Calcul du vecteur de statistique
			for (int j = 0; j < classes.get(i).size(); j++) {
				stats[labels[classes.get(i).get(j)]] += 1 ;/// (float)classes.get(i).size();
			}

			//Affichage
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
			printCenter(i);
			centerLabels[i] = max;
		}
	}
	
	public void printCenter(int h){
		for (int i = 0; i < nbPixel; i++) {
			for (int j = 0; j < nbPixel; j++) {
				if(center.get(h)[i][j] > 50){
					System.out.print(center.get(h)[i][j]);
					for (int j2 = 0; j2 < (4 - String.valueOf(center.get(h)[i][j]).length()); j2++) {
						System.out.print(" ");
					}
				}else{
					System.out.print("0   ");

				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public int reconnaissance(int[][] testeur){
		int min = 0;
		double dmin = Matrice.frobeniusNorm(Matrice.minus(testeur, center.get(0)));
		for (int[][] centre : center) {
			int[][] x = Matrice.minus(testeur, centre);
			double dx = Matrice.frobeniusNorm(x);
			if (dx < dmin) {
				dmin = dx;
				min = center.indexOf(centre);
			}
		}
		return centerLabels[min];
	}
	
	public int[][] getW(){
		int[][] W = new int[nbPixel][nbPixel];
		int[][] M = new int[nbPixel][nbPixel];
		for (int i = 0; i < numberCluster; i++) {
			int[][] mean = new int[nbPixel][nbPixel];
			mean = center.get(i);
			for (int j = 0; j < indicesClasses[i]; j++) {
				int[][] element = new int[nbPixel][nbPixel];
				element = images.get(classes.get(i).get(j)); 
			M =  Matrice.multiplication(Matrice.minus(element, mean),Matrice.transpose(Matrice.minus(element, mean))) ;
			W=Matrice.plus(M, W);
			}
			}
		return W;
	}
	
	public int[][] getB(){
		int[][] B = new int[nbPixel][nbPixel];
		int[][] M = Matrice.mean(center);
		for (int i = 0; i < numberCluster; i++) {
			int[][] X = Matrice.minus(center.get(i), M);
			
			B = Matrice.plus(B, Matrice.scalarMultiplication(indicesClasses[i], Matrice.multiplication(X, Matrice.transpose(X))));
		}
		
		return B;
	}
	
	public int ch() {
		int n = images.size();
		return Matrice.trace(getB()) * (n - numberCluster) / (Matrice.trace(getW()) * (numberCluster - 1));
	}
}
