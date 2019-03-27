import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class Kmeans {
	private int numberCluster;
	public int nbPixel;
	
	private List<int[][]> images;
	private int[] labels;
	private int N;

	private List<int[][]> lastCenter;
	public List<int[][]> center;
	private int[] centerLabels;
	private ArrayList<ArrayList<Integer>> classes;
	private int[] indicesClasses;


	public Kmeans(int numberCluster, List<int[][]> images, int[] labels) {
		//Initialisation de la base de donn�e
		this.numberCluster = numberCluster;
		this.images = images;
		this.labels = labels;
		this.nbPixel = images.get(0).length;
		N = images.size();
		
		//Cr�ation des clusters
		center = new ArrayList<int[][]>();
		lastCenter = new ArrayList<int[][]>();
		
		//Cr�ation de la liste contenant les chiffres d�tect�s des clusters
		centerLabels = new int[numberCluster];
		//Nombre d'�l�ments par cluster
		indicesClasses = new int[numberCluster];
		//Indices des �l�ments appartenant au cluster
		classes = new ArrayList<ArrayList<Integer>>(numberCluster);

		//Initialisation des listes de classes et d'indicesClasses
		for (int i = 0; i < numberCluster; i++) {
			classes.add(new ArrayList<Integer>());
		}

		for (int i = 0; i < numberCluster; i++) {
			indicesClasses[i] = 0;
		}
	}


	/**
	 * Initialise la premi�re liste de centre avec des �l�ments pris au hasard dans la liste d'images
	 */
	public void initializeFirstCenter(){
		int indice;

		for (int i = 0; i < numberCluster; i++) {
			indice = (int) (Math.random() * N ) ;
			center.add(images.get((indice)));
			lastCenter.addAll(center);
		}
	}

	public void searchCenter(){
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
			
			center.set(i, Matrice.scalarDivision(somme, indicesClasses[i]));
		}
	}

	
	/**
	 * Entraine le programme pour reconnaitre des nombres manuscrits
	 */
	public void train() {
		//On initialise les premiers centres
		this.initializeFirstCenter();

		double distanceMax;
		
		do{
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

			this.searchCenter();

			this.calculateCenter();
			
			for (int i=0 ; i<numberCluster ; i++){
				int[][] x = Matrice.minus(lastCenter.get(i), center.get(i));
				double dx = Matrice.frobeniusNorm(x);
				if (dx > distanceMax) {
					distanceMax = dx;
				}
			}
			System.out.println(distanceMax);
			
		} while(distanceMax != 0);
	}

	/**
	 * Affiche les statistiques li�s aux clusters
	 */
	public void statistics() {
		//On calcule les statistiques selon chaque groupe
		for (int i = 0; i < classes.size(); i++) {
			float[] stats = new float[10];

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
			System.out.println("], nombre reconnu : " + max);
			centerLabels[i] = max;
		}
	}
	
	/**
	 * Affiche les statistiques li�s aux clusters
	 * @param centerPrinting : affiche la matrice des centres 
	 */
	public void statistics(boolean centerPrinting) {
		//On calcule les statistiques selon chaque groupe
		for (int i = 0; i < classes.size(); i++) {
			float[] stats = new float[10];

			//Calcul du vecteur de statistique
			for (int j = 0; j < classes.get(i).size(); j++) {
				stats[labels[classes.get(i).get(j)]] += 1 ;/// (float)classes.get(i).size();
			}

			//Affichage
			System.out.print("[");
			for (int j = 0; j < 9; j++) {
				System.out.print(stats[j] + ", ");
			}

			int max = 0;
			for (int j = 1; j < stats.length; j++) {
				if (stats[max] < stats[j])
					max = j;
			}
			System.out.println("] " + max);
			if(centerPrinting)
				printCenter(i);
			centerLabels[i] = max;
		}
	}
	
	/**
	 * Affiche le centre h dans la console
	 * @param h : position du cluster
	 */
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
	
	/**
	 * @param testeur Matrice du nombre � tester
	 * @return la valeur du nombre d�tect�e correspondant � un cluster
	 */
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
	
	/**
	 * @return la matrice W correspondant � la dispersion au sein des clusters
	 */
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
	
	/**
	 * @return la matrice B correspondant � la dispertion entre les clusters
	 */
	public int[][] getB(){
		int[][] B = new int[nbPixel][nbPixel];
		int[][] M = Matrice.mean(center);
		for (int i = 0; i < numberCluster; i++) {
			int[][] X = Matrice.minus(center.get(i), M);
			
			B = Matrice.plus(B, Matrice.scalarMultiplication(indicesClasses[i], Matrice.multiplication(X, Matrice.transpose(X))));
		}
		
		return B;
	}

	public int getCH() {
		int n = images.size();
		return Matrice.trace(getB()) * (n - numberCluster) / (Matrice.trace(getW()) * (numberCluster - 1));
	}
	
	/**
	 * Effectue une reconnaissance de chiffre � l'aide de l'entrainement pr�alable
	 * @param list : liste de chiffre � reconnaitre
	 */
	public void recognize(List<int[][]> list) {
		float wellRecognized = 0;
		for (int i = 0; i < list.size(); i++) {
			int nbRecognized = reconnaissance(images.get(i));
			if(nbRecognized == labels[i])
				wellRecognized++;
			System.out.println("Nb reconnu " + nbRecognized + "et nb reel " + labels[i]);
		}
		System.out.println(wellRecognized / (float)list.size() * 100f + "% de r�ussite");
		System.out.println();
	}
}
