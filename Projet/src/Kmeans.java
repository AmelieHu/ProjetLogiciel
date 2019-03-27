import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class Kmeans {
	private int numberCluster;
	public int nbPixel;//Nombre de pixels d'une ligne ou colonne de la matrice

	private List<int[][]> images;//Liste des images d'entrainement de l'algorithme
	private int[] labels;//Chiffre que représentent les images
	private int N;//Nombre d'images

	private List<int[][]> lastCenter;//
	public List<int[][]> center;//Cluster
	private int[] centerLabels;//Chiffre que représente chaque cluster
	private ArrayList<ArrayList<Integer>> classes;//Images que contiennt les cluster
	private int[] indicesClasses;//Nombre d'image par cluster


	public Kmeans(int numberCluster, List<int[][]> images, int[] labels) {
		//Initialisation de la base de donnée
		this.numberCluster = numberCluster;
		this.images = images;
		this.labels = labels;
		this.nbPixel = images.get(0).length;
		N = images.size();

		//Création des clusters
		center = new ArrayList<int[][]>();
		lastCenter = new ArrayList<int[][]>();

		//Création de la liste contenant les chiffres détectés des clusters
		centerLabels = new int[numberCluster];
		//Nombre d'éléments par cluster
		indicesClasses = new int[numberCluster];
		//Indices des éléments appartenant au cluster
		classes = new ArrayList<ArrayList<Integer>>(numberCluster);

		//Initialisation des listes de classes et d'indicesClasses
		Util.initializeList(classes, numberCluster);
		Util.initializeVector(indicesClasses);
	}


	/**
	 * Initialise la première liste de centre avec des éléments pris au hasard dans la liste d'images
	 */
	public void initializeFirstCenter(){
		int indice;

		for (int i = 0; i < numberCluster; i++) {
			indice = (int) (Math.random() * N ) ;
			center.add(images.get((indice)));
			lastCenter.addAll(center);
		}
	}

	/**
	 * Cherche le cluster le plus proche pour chaque image
	 */
	public void searchCenter(){
		for (int[][] matrice : images) {
			//Indice du cluster qui est le plus proche de l'image
			int min = 0;
			//Distance minimale par rapport à ce cluster
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
			//Ajoute l'image à ce cluster
			classes.get(min).add(images.indexOf(matrice));
			//Augmente le nombre d'image associé à ce cluster
			indicesClasses[min]++;
		}
	}

	/**
	 * Calcule la nouvelle liste de cluster par rapport aux nouveaux points qu'ils contiennt
	 */
	public void calculateCenter(){
		for (int i = 0; i < numberCluster; i++) {
			int[][] somme = new int[nbPixel][nbPixel];

			//Fait la somme des matrices qui appartiennent au cluster
			for (int j = 0; j < indicesClasses[i]; j++) {
				somme = Matrice.plus(somme,
						images.get(classes.get(i).get(j)));
			}
			//Définie le nouveau cluster comme la moyenne des points qu'il contient
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

		//Tant que les clusters bougent
		do{
			//Distance maximale du mouvement des clusters d'une étape de la boucle à l'autre
			distanceMax = 0;

			//initialise les indicesClasses et les classes avec respectivement un vecteur de 0 et une liste de null
			Util.initializeVector(indicesClasses);
			Util.initializeList(classes, numberCluster);

			lastCenter.clear();
			lastCenter.addAll(center); 

			//Définie chaque image à un cluster
			this.searchCenter();

			//Change la position du cluster
			this.calculateCenter();

			//Calcule la distance maximale du mouvement des clusters
			for (int i=0 ; i < numberCluster ; i++){
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
	 * Calcule le chiffre reconnu par chaque cluster
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
	 * Affiche les statistiques liés aux clusters
	 * @param print : affichage 
	 */
	public void statistics(boolean print) {
		//On calcule les statistiques selon chaque groupe
		for (int i = 0; i < classes.size(); i++) {
			float[] stats = new float[10];

			//Calcul du vecteur de statistique
			for (int j = 0; j < classes.get(i).size(); j++) {
				stats[labels[classes.get(i).get(j)]] += 1 ;/// (float)classes.get(i).size();
			}

			int max = 0;
			for (int j = 1; j < stats.length; j++) {
				if (stats[max] < stats[j])
					max = j;
			}
			centerLabels[i] = max;
			
			if(print) {
				//Affichage
				System.out.print("[");
				for (int j = 0; j < stats.length; j++) {
					System.out.print(stats[j] + ", ");
				}
				System.out.println("], nombre reconnu : " + max);
				printCenter(i);
			}
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
				}else
					System.out.print("0   ");
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * @param testeur Matrice du nombre à tester
	 * @return la valeur du nombre détectée correspondant à un cluster
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
	 * @return la matrice W correspondant à la dispersion au sein des clusters
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
				M = Matrice.multiplication(Matrice.minus(element, mean),Matrice.transpose(Matrice.minus(element, mean)));
				W = Matrice.plus(M, W);

			}
		}
		return W;
	}

	/**
	 * @return la matrice B correspondant à la dispertion entre les clusters
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

	/**
	 * @return le paramètre CH caractérisant l'optimalité du nombre de cluster
	 */
	public long getCH() {
		long n = images.size();
		
		long errorBetween = Matrice.trace(getB()) / ((long)numberCluster - 1);
		long errorWithin = Matrice.trace(getW()) / (n - (long)numberCluster);
		
		return errorBetween / errorWithin;
	}

	/**
	 * Effectue une reconnaissance de chiffre à l'aide de l'entrainement préalable
	 * @param list : liste de chiffre à reconnaitre
	 */
	public void recognize(List<int[][]> list) {
		for (int i = 0; i < list.size(); i++) {
			int nbRecognized = reconnaissance(list.get(i));
			System.out.println("Nb reconnu " + nbRecognized);
		}
		System.out.println();
	}

	/**
	 * Calcule le taux de réussite de la reconnaissance
	 * @param list : liste de nombre à tester
	 * @param labelsList : labels des nombres de la liste
	 */
	public float recognize(List<int[][]> list, int[] labelsList) {
		float wellRecognized = 0;
		for (int i = 0; i < list.size(); i++) {
			int nbRecognized = reconnaissance(list.get(i));
			if(nbRecognized == labelsList[i])
				wellRecognized++;
			System.out.println("Nb reconnu " + nbRecognized + "et nb reel " + labelsList[i]);
		}
		System.out.println(wellRecognized / (float)list.size() * 100f + "% de réussite");
		System.out.println();
		return wellRecognized / (float)list.size() * 100f;
	}

	/**
	 * Calcule le taux de réussite de la reconnaissance
	 * @param list : liste de nombre à tester
	 * @param labelsList : labels des nombres de la liste
	 * @param print : affiche le nombre et ce que la reconnaissance a détecté
	 */
	public float[] recognize(List<int[][]> list, int[] labelsList, boolean print) {
		float[] total = new float[10];
		float[] recognized = new float[11];
		
		float wellRecognized = 0;
		for (int i = 0; i < list.size(); i++) {
			int nbRecognized = reconnaissance(list.get(i));
			if(nbRecognized == labelsList[i]) {
				wellRecognized++;
				recognized[labelsList[i]]++;
			}
			total[labelsList[i]]++;
			if(print) {
				System.out.println("Nb reconnu " + nbRecognized + "et nb reel " + labelsList[i]);
				System.out.print("[");
			}
		}
		
		for (int i = 0; i < recognized.length - 1; i++) {
			recognized[i] = recognized[i] / total[i] * 100f;
			if(print) {
				System.out.print(recognized[i] + ", ");
			}
		}
		
		if(print)
			System.out.println("]");
		
		recognized[10] = wellRecognized / (float)list.size() * 100f;
		
		System.out.println(recognized[10] + "% de réussite");
		System.out.println();
		return recognized;
	}
}
