import java.util.ArrayList;
import java.util.List;

public class Kmeans {
	private int numberCluster;
	public int nbPixel;//Nombre de pixels d'une ligne ou colonne de la matrice

	private List<int[][]> images;//Liste des images d'entrainement de l'algorithme
	private int[] labels;//Chiffre que repr�sentent les images
	private int N;//Nombre d'images

	private List<int[][]> lastCenter;//
	public List<int[][]> center;//Cluster
	private int[] centerLabels;//Chiffre que repr�sente chaque cluster
	private ArrayList<ArrayList<Integer>> classes;//Images que contiennt les cluster
	private int[] indicesClasses;//Nombre d'image par cluster


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
		Util.initializeList(classes, numberCluster);
		Util.initializeVector(indicesClasses);
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

	/**
	 * Cherche le cluster le plus proche pour chaque image
	 */
	public void searchCenter(){
		for (int[][] matrice : images) {
			//Indice du cluster qui est le plus proche de l'image
			int min = 0;
			//Distance minimale par rapport � ce cluster
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
			//Ajoute l'image � ce cluster
			classes.get(min).add(images.indexOf(matrice));
			//Augmente le nombre d'image associ� � ce cluster
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
			//D�finie le nouveau cluster comme la moyenne des points qu'il contient
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
			//Distance maximale du mouvement des clusters d'une �tape de la boucle � l'autre
			distanceMax = 0;

			//initialise les indicesClasses et les classes avec respectivement un vecteur de 0 et une liste de null
			Util.initializeVector(indicesClasses);
			Util.initializeList(classes, numberCluster);

			lastCenter.clear();
			lastCenter.addAll(center); 

			//D�finie chaque image � un cluster
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
	 * Affiche les statistiques li�s aux clusters
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
				Matrice.printMatrix(center.get(i), 4, 50);
			}
		}
	}
	
	/**
	 * Calcule le chiffre reconnu par chaque cluster
	 */
	public void statistics() {
		statistics(false);
	}

	/**
	 * @param testeur Matrice du nombre � tester
	 * @return la valeur du nombre d�tect�e correspondant � un cluster
	 */
	public int tagNumber(int[][] testeur){
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
				M = Matrice.multiplication(Matrice.minus(element, mean),Matrice.transpose(Matrice.minus(element, mean)));
				W = Matrice.plus(M, W);
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

	/**
	 * @return le param�tre CH caract�risant l'optimalit� du nombre de cluster
	 */
	public long getCH() {
		long n = images.size();

		long errorBetween = Matrice.trace(getB()) / ((long)numberCluster - 1);
		long errorWithin = Matrice.trace(getW()) / (n - (long)numberCluster);

		return errorBetween / errorWithin;
	}

	/**
	 * Effectue une reconnaissance de chiffre � l'aide de l'entrainement pr�alable
	 * @param list : liste de chiffre � reconnaitre
	 */
	public void recognize(List<int[][]> list) {
		for (int i = 0; i < list.size(); i++) {
			int nbRecognized = tagNumber(list.get(i));
			System.out.println("Nb reconnu " + nbRecognized);
		}
		System.out.println();
	}

	/**
	 * Calcule le taux de r�ussite de la reconnaissance
	 * @param list : liste de nombre � tester
	 * @param labelsList : labels des nombres de la liste
	 */
	public float[][][] recognize(List<int[][]> list, int[] labelsList) {
		float[][][] matrix = new float[2][10][10];
		float total = 0;

		for (int i = 0; i < list.size(); i++) {
			int nbRecognized = tagNumber(list.get(i));
			matrix[0][labelsList[i]][nbRecognized]++;
			total++;
		}

		int wellRecognized = (int)Matrice.trace(matrix[0]);
		
		matrix[1][0][0] = wellRecognized / total * 100f;
		/*for (int i = 0; i < recognized.length - 1; i++) {
			recognized[i] = recognized[i] / total[i] * 100f;
		}*/

		//recognized[10] = wellRecognized / (float)list.size() * 100f;

		return matrix;
	}

	/**
	 * Calcule le taux de r�ussite de la reconnaissance
	 * @param list : liste de nombre � tester
	 * @param labelsList : labels des nombres de la liste
	 * @param print : affiche le nombre et ce que la reconnaissance a d�tect�
	 */
	public float[][][] recognize(List<int[][]> list, int[] labelsList, boolean print) {
		float[][][] recognized = recognize(list, labelsList);

		if(print) {
			Matrice.printMatrix(recognized[0], 5, 0);
		}
		return recognized;
	}
}
