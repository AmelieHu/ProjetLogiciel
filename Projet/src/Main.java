import java.util.ArrayList;
import java.util.List;


public class Main {

	public static void main(String[] args){

		int numberCluster = 20; //Nombre de cluster
		int N = 5000;//Nombre d'image utilisée pour entrainer l'algorithme


		//initialisation des matrices d'images et de labels
		int[] labels = MnistReader.getLabels("train-labels.idx1-ubyte");
		List<int[][]> images = MnistReader.getImages("train-images.idx3-ubyte");

		//Initialisation des listes réduites des images
		List<int[][]> imagesReduced = new ArrayList<int[][]>();
		int[] labelsReduced = new int[N];
		imagesReduced = Util.reduceList(images, N);
		labelsReduced = Util.reducedVector(labels, N);

		//Initialisation de l'algorithme avec les images
		Kmeans essai = new Kmeans(numberCluster, imagesReduced, labelsReduced);

		//Entrainement de l'algorithme
		essai.train();

		//Affichage des statistiques de détection
		essai.statistics(false);

		int sizeToTest = 500;

		//Image à tester
		List<int[][]> imageToTest = new ArrayList<int[][]>();
		int[] labelsToTest = new int[sizeToTest];
		imageToTest = Util.reduceList(images, sizeToTest, N);
		labelsToTest = Util.reducedVector(labels, sizeToTest, N);


		//Reconnaissance d'une liste de nombres manuscrits
		essai.recognize(imageToTest, labelsToTest, true);

		System.out.println(essai.getCH());
		
		/*int N = 3000;
		Kmeans kmean;

		//Initialisation des listes réduites des images
		List<int[][]> imagesReduced = new ArrayList<int[][]>();
		int[] labelsReduced = new int[N];
		imagesReduced = Util.reduceList(images, N);
		labelsReduced = Util.reducedVector(labels, N);
		
		int sizeToTest = 500;
		
		//Image à tester
		List<int[][]> imageToTest = new ArrayList<int[][]>();
		int[] labelsToTest = new int[sizeToTest];
		imageToTest = Util.reduceList(images, sizeToTest, N);
		labelsToTest = Util.reducedVector(labels, sizeToTest, N);

		float[] stats = new float[190];
		long[] CH = new long[190];
		
		for (int i = 10; i < 200; i++) {
			kmean = new Kmeans(i, imagesReduced, labelsReduced);
			kmean.train();
			
			kmean.statistics(false);
			stats[i - 10] = kmean.recognize(imageToTest, labelsToTest, false);
			CH[i - 10] = kmean.getCH();
		}
		
		System.out.print("[");
		for (int j = 0; j < stats.length; j++) {
			System.out.print(stats[j] + ", ");
		}
		System.out.println("]");
		
		System.out.print("[");
		for (int j = 0; j < CH.length; j++) {
			System.out.print(CH[j] + ", ");
		}
		System.out.println("]");*/
	}
}
