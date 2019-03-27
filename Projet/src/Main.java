import java.util.ArrayList;
import java.util.List;


public class Main {

	public static void main(String[] args){
		
		//Matrice.training();

		int[] labels = MnistReader.getLabels("train-labels.idx1-ubyte");
		List<int[][]> image = MnistReader.getImages("train-images.idx3-ubyte");
		
	
		//Matrice.test(); 
		
		int[] labelsTest = MnistReader.getLabels("t10k-labels.idx1-ubyte");
		List<int[][]> imageTest = MnistReader.getImages("t10k-images.idx3-ubyte");
		int nbTesteur = 20;

		double epsilon = 0;
		int numberCluster = 20;
		int imax = 10;
		int N = 10000;
		int nbPixel = 28;
		
		List<int[][]> imageReduit = new ArrayList<int[][]>();
		int[] labelsReduit = new int[N];
		for (int i=0; i<N; i++){
			imageReduit.add(image.get(i));
			labelsReduit[i]= labels[i];
		}
		Kmeans essai = new Kmeans(imageReduit, labelsReduit, imax, numberCluster);
		essai.training(epsilon);

		essai.statistics();

		for(int i=0; i< nbTesteur; i++ ){
			int nbReconnu = essai.reconnaissance(imageTest.get(i));
			System.out.println("Nb reconnu " + nbReconnu + "et nb reel " + labelsTest[i]);
			
		}
		
		System.out.println(essai.ch());
		
	}
}
