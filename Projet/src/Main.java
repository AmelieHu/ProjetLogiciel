import java.util.ArrayList;
import java.util.List;


public class Main {

	public static void main(String[] args){
		
		int numberCluster = 30; //Nombre de cluster
		int N = 10000;//Nombre d'image utilisée pour entrainer l'algorithme
		
		
		//initialisation des matrices d'images et de labels
		int[] labels = MnistReader.getLabels("train-labels.idx1-ubyte");
		List<int[][]> images = MnistReader.getImages("train-images.idx3-ubyte");
		
		//Initialisation des listes réduites des images
		List<int[][]> imagesReduced = new ArrayList<int[][]>();
		int[] labelsReduced = new int[N];
		imagesReduced = Util.reduceList(images, N);
		labelsReduced = Util.reducedVector(labels, N);
		
		//Initialisation de l'algorithme avec les images
		Kmeans essai = new Kmeans(numberCluster, images, labels);
		
		//Entrainement de l'algorithme
		essai.train();

		//Affichage des statistiques de détection
		essai.statistics();

		//Image à tester
		List<int[][]> imageToTest = new ArrayList<int[][]>();
		imageToTest = Util.reduceList(images, 10, N);
		
		//Reconnaissance d'une liste de nombres manuscrits
		essai.recognize(imageToTest);
		
		System.out.println(essai.getCH());
	}
}
