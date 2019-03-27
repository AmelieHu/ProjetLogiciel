import java.util.List;

public class Matrice {

	/**
	 * @param m1 : matrice 
	 * @param m2 : matrice
	 * @return : différence entre la matrice m1 et la matrice m2
	 */
	public static int[][] minus(int[][]m1,int[][] m2 ) {
		int[][] ms = new int[m1.length][m1[0].length ];
		for ( int i = 0 ; i < m1.length ; i++ ) {
			for ( int j = 0 ; j < m1[0].length ; j++ ) {

				ms[i][j] = m1[i][j]- m2[i][j]; } 

		}
		return ms;
	}

	/**
	 * @param A : matrice
	 * @return : norme de frobenius de la matrice
	 */
	public static double frobeniusNorm (int[][] A){
		double S = 0;
		for ( int i = 0 ; i < A.length ; i++ ) {
			for ( int j = 0 ; j < A[0].length ; j++ ) {
				S= S + Math.pow(A[i][j],2);

			}

		}
		return S;
	}

	/**
	 * @param m1 : matrice 1
	 * @param m2 : matrice 2
	 * @return : addition entre les deux matrices
	 */
	public static int[][] plus(int[][]m1,int[][] m2 ) {
		int[][] ms = new int[m1.length][m1[0].length ];
		for ( int i = 0 ; i < m1.length ; i++ ) {
			for ( int j = 0 ; j < m1[0].length ; j++ ) {

				ms[i][j] = m1[i][j] + m2[i][j]; } 

		}

		return ms;
	}

	/**
	 * @param A : matrice
	 * @param N : scalaire
	 * @return : division entre la matrice et le scalaire
	 */
	public static int[][] scalarDivision(int[][] A , int N){
		if (N != 0){

			for ( int i = 0 ; i < A.length ; i++ ) {
				for ( int j = 0 ; j < A[0].length ; j++ ) {
					A[i][j] = A[i][j]/N;
				}
			}
		}
		return A;
	}
	
	/**
	 * @param M : matrice
	 * @return : transposée de la matrice
	 */
	public static int[][] transpose(int[][] M){
		int[][] Mt = new int [M.length][M[0].length];
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M[0].length; j++) {
				Mt[i][j] = M[j][i]; 
			}
		}
		return Mt;
	}
	
	/**
	 * @param m1 : matrice 1
	 * @param m2 : matrice 2
	 * @return : produit des deux matrices
	 */
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
	
	/**
	 * @param list
	 * @return valeur moyenne de la liste
	 */
	public static int[][] mean(List<int[][]> list){
		int[][] mean = new int[list.get(0).length][list.get(0)[0].length];
		for (int i = 0; i < list.size(); i++) {
			mean = plus(list.get(i), mean);
		}
		
		return scalarDivision(mean, list.size());
	}
	
	/**
	 * Calcule la multiplication d'une matrice et d'un sclaire
	 * @param scalar : scalaire
	 * @param M : matrice
	 * @return : produit
	 */
	public static int[][] scalarMultiplication(int scalar, int[][] M){
		
		int[][] res = new int[M.length][M[0].length];
		for (int i = 0; i < M.length; i++) {
			for (int j = 0; j < M[0].length; j++) {
				res[i][j] = M[i][j] * scalar;
			}
		}
		
		return res;
	}
	
	/**
	 * @param M : matrice
	 * @return : trace de la matrice
	 */
	public static long trace(int[][] M){
		long tr = 0;
		for (int i = 0; i < M.length; i++) {
			tr += M[i][i];
		}
		return tr;
	}
 }
