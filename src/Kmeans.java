import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class Kmeans {
	private int k = 10;
	int nbPixel = 28;
	private List<int[][]> images;
	private int N;;
	private static int[] labels;
	private int imax;

	private List<int[][]> center;
	private int[] centerLabels;
	private ArrayList<ArrayList<Integer>> classes;
	private ArrayList<ArrayList<Integer>> emptyClasses;
	private int[] indicesClasses;

	public Kmeans(List<int[][]> images, int[] labels, int imax) {
		this.images = images;
		this.labels = labels;
		this.imax = imax;
		
		N = images.size();
		center = new ArrayList<int[][]>();
		centerLabels = new int[k];
		indicesClasses = new int[k];
		classes = new ArrayList<ArrayList<Integer>>(10);
		
		for (int i = 0; i < 10; i++) {
			classes.add(new ArrayList<Integer>());
		}
		
		for (int i = 0; i < k; i++) {
			indicesClasses[i] = 0;
		}
	}

	// Training : creation des classes de chiffres à partir de la base de
	// données
	public void training() {

		int indice;

		for (int i = 0; i < k; i++) {
			indice = (int) (Math.random() * N ) ;
			center.add(images.get((indice)));
			//centerLabels[i] = labels[indice];
		}

		for (int it = 0; it < imax; it++) {
			for (int i = 0; i < indicesClasses.length; i++) {
				indicesClasses[i] = 0;
			}
			classes.clear();
			
			for (int i = 0; i < 10; i++) {
				classes.add(new ArrayList<Integer>());
			}
			
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
			
			for (int i = 0; i < k; i++) {
				int[][] somme = new int[nbPixel][nbPixel];
				for (int j = 0; j < indicesClasses[i]; j++) {
					somme = Matrice.plus(somme,
							images.get(classes.get(i).get(j)));
				}

				if (indicesClasses[i] != 0) {
					center.set(i, Matrice.normalize(somme, indicesClasses[i]));
				}
			}
		}
		
		for (int j = 0 ; j< classes.size() ; j++){
		for (int i = 0; i < classes.get(j).size(); i++) {
			System.out.println(" classe " + j + ":" + labels[classes.get(j).get(i)] + "");
		}
	}
	}

	public int reconnaissance(int[][] chiffre) {

		for (int j = 0; j < centerLabels.length; j++) {
			System.out.print(centerLabels[j] + " ");				
		}

		int min = 0;
		double dmin = Matrice.frobeniusNorm(Matrice.minus(chiffre,
				center.get(0)));
		for (int[][] centre : center) {
			int[][] x = Matrice.minus(chiffre, centre);
			double dx = Matrice.frobeniusNorm(x);
			if (dx < dmin) {
				dmin = dx;
				min = center.indexOf(centre);
			}
		}
		return centerLabels[min];
	}
}
