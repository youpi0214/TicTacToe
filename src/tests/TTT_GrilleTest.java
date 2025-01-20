package tests;

import static org.junit.Assert.*;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.Test;

import modele.TTT_Grille;

public class TTT_GrilleTest {

	/**@formatter:off
	 * 							1|2|3
	 *  {1,2,3,4,5,6,7,8,9} => 	4|5|6 
	 * 							7|8|9
	 * 
	 @formatter:on*/
	@Test
	public void testTransfertVecAMatPos() {
		int[] vecteur = { 0, 1, 2 };
		int[][] resultat;
		Function<int[], int[][]> transfert = (int[] vec) -> TTT_Grille.transfertVecAMatPos(vec);
		BiFunction<int[][], int[][], Boolean> verification = (int[][] first, int[][] second) -> {
			boolean reussi = true;
			if (first.length == second.length && first[0].length == second[0].length) {
				for (int i = 0; i < first.length && reussi; i++) {
					for (int j = 0; j < first[0].length && reussi; j++) {
						if (first[i][j] != second[i][j])
							reussi = false;
					}
				}
			} else
				reussi = false;
			return new Boolean(reussi);
		};

		int[][] verif1 = { { 0, 0 }, { 0, 1 }, { 0, 2 } };
		assertTrue((resultat = transfert.apply(vecteur)) != null && verification.apply(resultat, verif1));
		int[][] verif2 = { { 0, 0 }, { 1, 0 }, { 2, 0 } };
		int[] vecteur2 = { 0, 3, 6 };
		assertTrue((resultat = transfert.apply(vecteur2)) != null && verification.apply(resultat, verif2));

		int[][] verif3 = { { 0, 0 }, { 1, 1 }, { 2, 2 } };
		int[] vecteur3 = { 0, 4, 8 };
		assertTrue((resultat = transfert.apply(vecteur3)) != null && verification.apply(resultat, verif3));

		int[][] verif4 = { { 0, 2 }, { 1, 1 }, { 2, 0 } };
		int[] vecteur4 = { 2, 4, 6 };
		assertTrue((resultat = transfert.apply(vecteur4)) != null && verification.apply(resultat, verif4));

		int[][] verif5 = { { 0, 0 }, { 1, 0 }, { 1, 2 } };
		int[] vecteur5 = { 0, 3, 5 };
		assertTrue((resultat = transfert.apply(vecteur5)) != null && verification.apply(resultat, verif5));

	}

	/**@formatter:off
	 * 	1|2|3
	 *  4|5|6 => {1,2,3,4,5,6,7,8,9} 
	 * 	7|8|9
	 * 
	 @formatter:on*/
	@Test
	public void testTransfertMatVec() {
		Function<char[][], char[]> transfert = (char[][] vec) -> TTT_Grille.transfertMatVec(vec);
		BiFunction<char[], char[], Boolean> verification = (char[] first, char[] second) -> {
			boolean reussi = true;
			if (first.length == second.length) {
				for (int i = 0; i < first.length && reussi; i++) {
					if (first[i] != second[i]) {
						reussi = false;
					}
				}
			} else
				reussi = false;
			return new Boolean(reussi);
		};

		char[] resultat;

		char[][] mat1 = { { 'X', ' ', ' ' }, { ' ', 'X', ' ' }, { ' ', ' ', 'X' } };
		char[] verif1 = { 'X', ' ', ' ', ' ', 'X', ' ', ' ', ' ', 'X' };
		assertTrue((resultat = transfert.apply(mat1)) != null && verification.apply(resultat, verif1));

		char[][] mat2 = { { 'X', 'X', 'X' }, { 'X', 'X', ' ' }, { ' ', 'X', 'X' } };
		char[] verif2 = { 'X', 'X', 'X', 'X', 'X', ' ', ' ', 'X', 'X' };
		assertTrue((resultat = transfert.apply(mat2)) != null && verification.apply(resultat, verif2));

		char[][] mat3 = { { 'X', ' ', 'O' }, { 'O', 'X', ' ' }, { ' ', 'O', 'X' } };
		char[] verif3 = { 'X', ' ', 'O', 'O', 'X', ' ', ' ', 'O', 'X' };
		assertTrue((resultat = transfert.apply(mat3)) != null && verification.apply(resultat, verif3));

		char[][] mat4 = { { 'X', 'X', 'X' }, { 'X', 'X', 'X' }, { 'X', 'O', 'X' } };
		char[] verif4 = { 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X' };
		resultat = transfert.apply(mat4);
		assertTrue(resultat != null);
		assertFalse(verification.apply(resultat, verif4));
	}

	@Test
	public void testPlacer() {
		char[][] mat1 = { { 'X', ' ', ' ' }, { ' ', 'X', ' ' }, { ' ', ' ', 'X' } };
		char[][] mat2 = { { 'X', ' ', ' ' }, { ' ', 'X', ' ' }, { ' ', ' ', 'X' } };
		BiFunction<char[][], char[][], Boolean> verification = (char[][] first, char[][] second) -> {
			boolean reussi = true;
			if (first.length == second.length && first[0].length == second[0].length) {
				for (int i = 0; i < first.length && reussi; i++) {
					for (int j = 0; j < first[0].length && reussi; j++) {
						if (first[i][j] != second[i][j]) {
							reussi = false;
						}
					}

				}
			} else
				reussi = false;
			return new Boolean(reussi);
		};

		assertTrue(verification.apply(mat1, mat2));
		assertFalse(TTT_Grille.placer(mat1, 0, 0, 'X'));
		assertTrue(verification.apply(mat1, mat2));

		assertFalse(TTT_Grille.placer(mat1, 0, 0, 'O'));
		assertTrue(verification.apply(mat1, mat2));

		assertTrue(TTT_Grille.placer(mat1, 0, 1, 'X'));
		assertFalse(verification.apply(mat1, mat2));
	}

}
