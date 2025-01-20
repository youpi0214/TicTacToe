package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import modele.JeuTTT;
import modele.TTT_Grille;
import modele.nivSelect;

public class JeuTTTTest {

	JeuTTT jeu1, jeu2, jeu3, jeu4;

	//@formatter:off
	char[][] g1 = { { 'X', 'X', 'X' }, 
					{ 'O', 'O', ' ' }, 
					{ ' ', ' ', ' ' } },
	
			g4 = { 	{ ' ', 'O', 'X' },
					{ 'O', 'X', ' ' }, 
					{ 'X', ' ', ' ' } },
	
			g2 = { 	{ 'O', 'O', 'X' }, 
					{ ' ', ' ', 'X' }, 
					{ ' ', ' ', 'X' } },
	
			g3 = {	{ 'X', 'O', ' ' },
					{ ' ', 'X', 'O' },
					{ ' ', ' ', 'X' } },
			
			g5 = {	{ 'X', 'O', ' ' },
					{ ' ', 'X', 'O' },
					{ ' ', ' ', ' ' } };
	//@formatter:on

	@Before
	public void constructeurs() {
		jeu1 = new JeuTTT(nivSelect.ALEATOIRE);
		jeu2 = new JeuTTT(nivSelect.FACILE);
		jeu3 = new JeuTTT(nivSelect.DIFFICILE);
		jeu4 = new JeuTTT(nivSelect.JOUEUR);
	}

	@Test
	public void testJouerHumain() {
		assertTrue(jeu1.getGrille()[0][0] == ' ');
		jeu1.jouerHumain(0, 0);
		assertTrue(jeu1.getGrille()[0][0] == 'X');
		jeu1.jouerHumain(1, 2);
		assertTrue(jeu1.getGrille()[1][2] == 'X');
	}

	@Test
	public void testJouerIA() {
		assertTrue(positionsVacantes(jeu1.getGrille()).size() == 9);
		jeu1.jouerIA();
		assertTrue(positionsVacantes(jeu1.getGrille()).size() == 8);

		assertTrue(positionsVacantes(jeu2.getGrille()).size() == 9);
		jeu2.jouerHumain(0, 0);
		int[] a = jeu2.jouerIA();
		assertTrue(positionsVacantes(jeu2.getGrille()).size() == 7);
		assertTrue(a != null);

		assertTrue(positionsVacantes(jeu3.getGrille()).size() == 9);
		jeu3.jouerHumain(0, 0);
		a = jeu3.jouerIA();
		assertTrue(positionsVacantes(jeu3.getGrille()).size() == 7);
		assertTrue(a != null);

		assertTrue(positionsVacantes(jeu4.getGrille()).size() == 9);
		jeu4.jouerHumain(0, 0);
		a = jeu4.jouerIA();
		assertTrue(positionsVacantes(jeu4.getGrille()).size() == 8);
		assertTrue(a == null);
	}

	@Test
	public void testValiderGagner() {
		int[][] gagne = null;

		assertTrue((gagne = JeuTTT.validerGagner(g1, 'X')) != null);
		int[][] verif1 = { { 0, 0 }, { 0, 1 }, { 0, 2 } };
		assertTrue(estEgal(gagne, verif1));

		assertTrue((gagne = JeuTTT.validerGagner(g2, 'X')) != null);
		int[][] verif2 = { { 0, 2 }, { 1, 2 }, { 2, 2 } };
		assertTrue(estEgal(gagne, verif2));

		assertTrue((gagne = JeuTTT.validerGagner(g3, 'X')) != null);
		int[][] verif3 = { { 0, 0 }, { 1, 1 }, { 2, 2 } };
		assertTrue(estEgal(gagne, verif3));

		assertTrue((gagne = JeuTTT.validerGagner(g4, 'X')) != null);
		int[][] verif4 = { { 0, 2 }, { 1, 1 }, { 2, 0 } };
		assertTrue(estEgal(gagne, verif4));

		assertTrue((gagne = JeuTTT.validerGagner(g1, 'O')) == null);
		assertTrue((gagne = JeuTTT.validerGagner(g5, 'X')) == null);
	}

	private boolean estEgal(int[][] first, int[][] second) {
		boolean retour = true;
		if (first.length == second.length && first[0].length == second[0].length) {
			for (int i = 0; i < first.length; i++) {
				for (int j = 0; j < first[0].length; j++) {
					if (first[i][j] != second[i][j])
						retour = false;
				}
			}
		}
		return retour;
	}

	private ArrayList<int[]> positionsVacantes(char[][] grille) {
		ArrayList<int[]> retour = new ArrayList<>();
		int[] temp = new int[2];
		for (int i = 0; i < grille.length; i++) {
			for (int j = 0; j < grille[0].length; j++) {
				if (grille[i][j] == TTT_Grille.cars[2]) {
					temp[0] = i;
					temp[1] = j;
					retour.add(temp.clone());
				}
			}
		}
		return retour;
	}

}
