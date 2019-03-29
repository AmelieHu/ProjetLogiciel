import java.util.ArrayList;
import java.util.List;


public class Main {

	public static void main(String[] args){

		//initialisation des matrices d'images et de labels
		int[] labels = MnistReader.getLabels("train-labels.idx1-ubyte");
		List<int[][]> images = MnistReader.getImages("train-images.idx3-ubyte");

		//testNumberCluster(images, labels, 10000, 10000, 10, 200);
		trainAndTest(images, labels, 5000, 5000, 50, true);

		

	}

	public static void testNumberCluster(List<int[][]> images, int[] labels, int numberImage, int numberImageToTest, int numberMinCluster, int numberMaxCluster) {
		int N = numberImage;
		int sizeToTest = numberImageToTest;

		Kmeans kmean;

		//Initialisation des listes rï¿½duites des images
		List<int[][]> imagesReduced = new ArrayList<int[][]>();
		int[] labelsReduced = new int[N];
		imagesReduced = Util.reduceList(images, N);
		labelsReduced = Util.reducedVector(labels, N);


		//Image ï¿½ tester
		List<int[][]> imageToTest = new ArrayList<int[][]>();
		int[] labelsToTest = new int[sizeToTest];
		imageToTest = Util.reduceList(images, sizeToTest, N);
		labelsToTest = Util.reducedVector(labels, sizeToTest, N);

		float[] stats = new float[numberMaxCluster - numberMinCluster + 1];
		long[] CH = new long[numberMaxCluster - numberMinCluster + 1];

		for (int i = numberMinCluster; i < numberMaxCluster + 1; i++) {
			kmean = new Kmeans(i, imagesReduced, labelsReduced);
			kmean.train();

			kmean.statistics(false);
			float[][][] statistics = kmean.recognize(imageToTest, labelsToTest); //Statistique sur la reconnaissance
			stats[i - numberMinCluster] = statistics[1][0][0]; //Taux de reconnaissance par nombre de cluster
			CH[i - numberMinCluster] = kmean.getCH(); //Récupère le CH pour chaque cluster
		}

		//Affichage des statistiques de reconnaissances
		System.out.print("[");
		for (int j = 0; j < stats.length; j++) {
			System.out.print(stats[j] + ", ");
		}
		System.out.println("]");

		//Affiche le paramètre CH
		System.out.print("[");
		for (int j = 0; j < CH.length; j++) {
			System.out.print(CH[j] + ", ");
		}
		System.out.println("]");
	}

	public static void trainAndTest(List<int[][]> images, int[] labels, int numberImage, int numberImageToTest, int numberCluster, boolean print) {
		Kmeans kmean;

		//Initialisation des listes rï¿½duites des images
		List<int[][]> imagesReduced = new ArrayList<int[][]>();
		int[] labelsReduced = new int[numberImage];
		imagesReduced = Util.reduceList(images, numberImage);
		labelsReduced = Util.reducedVector(labels, numberImage);

		//Initialisation de l'algorithme avec les images
		Kmeans essai = new Kmeans(numberCluster, imagesReduced, labelsReduced);

		//Entrainement de l'algorithme
		essai.train();

		//Affichage des statistiques de dï¿½tection
		essai.statistics(print);

		//Image ï¿½ tester
		List<int[][]> imageToTest = new ArrayList<int[][]>();
		int[] labelsToTest = new int[numberImageToTest];
		imageToTest = Util.reduceList(images, numberImageToTest, numberImage);
		labelsToTest = Util.reducedVector(labels, numberImageToTest, numberImage);


		//Reconnaissance d'une liste de nombres manuscrits
		float[][][] statistics = essai.recognize(imageToTest, labelsToTest, print);

		//Affichage des stastiques générales
		System.out.println();
		System.out.println("Pourcentage de réussite de détection : " + statistics[1][0][0]);
		System.out.println("Score de Calinski Harabasz : " + essai.getCH());


	}
}

