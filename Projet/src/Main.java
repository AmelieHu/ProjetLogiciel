import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args){
		
		//initialisation des matrices d'images et de labels
		int[] labels = MnistReader.getLabels("train-labels.idx1-ubyte");
		List<int[][]> images = MnistReader.getImages("train-images.idx3-ubyte");

		//testNumberCluster(images, labels, 100, 100, 10, 11);
		trainAndTest(images, labels, 500, 500, 200, true);
		//testNumberImages(images, labels, 200, 300, 5000, false);
	}

	public static void testNumberCluster(List<int[][]> images, int[] labels, int numberImage, int numberImageToTest, int numberMinCluster, int numberMaxCluster) {
		
		int N = numberImage;
		int sizeToTest = numberImageToTest;

		Kmeans kmean;

		//Initialisation des listes reduites des images
		List<int[][]> imagesReduced = new ArrayList<int[][]>();
		int[] labelsReduced = new int[N];
		imagesReduced = Util.reduceList(images, N);
		labelsReduced = Util.reducedVector(labels, N);

		//Image a tester
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
		
		//Initialisation des listes reduites des images
		List<int[][]> imagesReduced = new ArrayList<int[][]>();
		int[] labelsReduced = new int[numberImage];
		imagesReduced = Util.reduceList(images, numberImage);
		labelsReduced = Util.reducedVector(labels, numberImage);

		//Initialisation de l'algorithme avec les images
		Kmeans essai = new Kmeans(numberCluster, imagesReduced, labelsReduced);

		long t1 = System.currentTimeMillis();
		//Entrainement de l'algorithme
		essai.train();
		long t2 = System.currentTimeMillis();

		//Affichage des statistiques de detection
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
		System.out.println("Pourcentage de reussite de detection : " + statistics[1][0][0] + "%");
		System.out.println("Score de Calinski Harabasz : " + essai.getCH());
		System.out.println("Temps d'execution de l'entrainement : " + (t2-t1) + "ms");
	}
	
	public static void testNumberImages(List<int[][]> images, int[] labels,int numberCluster, int numberMinImage, int numberMaxImage, boolean print) {
		float[] stats = new float[(numberMaxImage - numberMinImage) / 100];
		long[] CH = new long[(numberMaxImage - numberMinImage) / 100];
		
		for (int i = 0; i < (numberMaxImage - numberMinImage) / 100; i++) {
			List<int[][]> imagesReduced = Util.reduceList(images, numberMinImage + i*100);
			int[] labelsReduced = Util.reducedVector(labels, numberMinImage + i*100);
			
			//Initialisation de l'algorithme avec les images
			Kmeans kmeans = new Kmeans(numberCluster, imagesReduced, labelsReduced);
			
			//Entrainement de l'algorithme
			kmeans.train();

			//Affichage des statistiques de detection
			kmeans.statistics(print);
			
			List<int[][]> imagesToTest = Util.reduceList(images, numberMinImage + i*100, numberMinImage + i*100);
			int[] labelsToTest = Util.reducedVector(labels, numberMinImage + i*100, numberMinImage + i*100);
			
			float[][][] statistics = kmeans.recognize(imagesToTest, labelsToTest, print); //Statistique sur la reconnaissance
			
			stats[i] = statistics[1][0][0]; //Taux de reconnaissance par nombre de cluster
			CH[i] = kmeans.getCH();
			
			System.out.println((numberMaxImage - numberMinImage) / 100 - i);
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
}

