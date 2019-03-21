import java.util.ArrayList;
import java.util.List;


public class Matrice {

	public static int[][] minus(int[][]m1,int[][] m2 ) {
		int[][] ms = new int[m1.length][m1[0].length ];
		for ( int i = 0 ; i < m1.length ; i++ ) {
			for ( int j = 0 ; j < m1[0].length ; j++ ) {

				ms[i][j] = m1[i][j]- m2[i][j]; } 

		}
		return ms;
	}


	public static double frobeniusNorm (int[][] A){
		double S = 0;
		for ( int i = 0 ; i < A.length ; i++ ) {
			for ( int j = 0 ; j < A[0].length ; j++ ) {
				S= S + Math.pow(A[i][j],2);

			}

		}
		return S;
	}

	public static int[][] plus(int[][]m1,int[][] m2 ) {
		int[][] ms = new int[m1.length][m1[0].length ];
		for ( int i = 0 ; i < m1.length ; i++ ) {
			for ( int j = 0 ; j < m1[0].length ; j++ ) {

				ms[i][j] = m1[i][j] + m2[i][j]; } 

		}

		return ms;
	}



	public static int[][] normalize(int[][] A , int N){
		if (N != 0){

			for ( int i = 0 ; i < A.length ; i++ ) {
				for ( int j = 0 ; j < A[0].length ; j++ ) {
					A[i][j] = A[i][j]/N;
				}
			}
		}
		return A;
	}

	public static void test(){
		int[][] A = {{0, 0}, {0, 0}};
		int[][] B = {{0, 0}, {0, 0}};

		int[][] m = minus(A, B);
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				System.out.print(m[i][j] + " ");				
			}
			System.out.println();
		}

		System.out.println(frobeniusNorm(m));
	}
	
	public static int[][] transpose(int[][] M){
		int[][] Mt = new int [M.length][M[0].length];
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M[0].length; j++) {
				Mt[i][j] = M[j][i]; 
			}
		}
		return Mt;
	}
	
	public static int[][] multiplication(int[][] m1, int[][] m2){
		int l= m1.length;
		int c = m1[0].length;
		int[][] produit = new int[l][c];
		for (int row =0; row <l;row++){
			for (int col =0; col<c;col ++){
				for(int i = 0; i < m1.length; i++)
				produit[row][col]+= m1[row][i] *m2[i][col];
						}
		}
		return produit; 
	}
	
	public static int[][] mean(List<int[][]> list){
		int[][] mean = new int[28][28];
		for (int i = 0; i < list.size(); i++) {
			mean = plus(list.get(i), mean);
		}
		
		return normalize(mean, list.size());
	}
	
	public static int[][] scalarMultiplication(int scalar, int[][] M){
		
		int[][] res = new int[28][28];
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M[0].length; j++) {
				res[i][j] = M[i][j] * scalar;
			}
		}
		
		return res;
	}
	
	public static int trace(int[][] M){
		int tr = 0;
		for (int i = 0; i < M.length; i++) {
			tr += M[i][i];
		}
		return tr;
	}
	
	public static void merde(){
		int[][] m1 = new int[][]{new int[]{1, 0}, new int[]{0, 1}};
		int[][] m2 = new int[][]{new int[]{4, 3}, new int[]{1, 9}};
		System.out.println(multiplication(m1, m2)[0][0] + multiplication(m1, m2)[0][1] + multiplication(m1, m2)[1][0] + multiplication(m1, m2)[1][1]);
	}
 }
