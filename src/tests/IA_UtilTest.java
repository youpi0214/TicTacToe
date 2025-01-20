package tests;

import static org.junit.Assert.*;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.junit.Test;

import modele.IA_Util;
import modele.TTT_Grille;

public class IA_UtilTest
{

	// @formatter:off
		char[][] g1 = { { 'X', ' ', ' ' }, 
						{ ' ', ' ', ' ' }, 
						{ ' ', ' ', ' ' } },
				
				g2 = { 	{ ' ', ' ', ' ' },
						{ ' ', ' ', ' ' }, 
						{ 'X', ' ', ' ' } },
				
				g3 = { 	{ ' ', ' ', ' ' }, 
						{ ' ', ' ', ' ' }, 
						{ ' ', ' ', 'X' } },
				
				g4 = {	{ ' ', ' ', 'X' },
						{ ' ', ' ', ' ' },
						{ ' ', ' ', ' ' } },
				
				g5 = {	{ ' ', ' ', ' ' },
						{ ' ', 'X', ' ' },
						{ ' ', ' ', ' ' } },
				
				g6 = {	{ ' ', 'X', ' ' }, 
						{ ' ', ' ', ' ' },
						{ ' ', ' ', ' ' } },
				
				g7 = {	{ ' ', 'X', 'X' },
						{ ' ', 'O', ' ' }, 
						{ ' ', ' ', ' ' } },
				
				g8 = {	{ 'O', 'X', 'X' },
						{ 'X', 'O', ' ' }, 
						{ ' ', ' ', ' ' } },
				
				g9 = {	{ ' ', ' ', 'X' },
						{ ' ', 'O', ' ' }, 
						{ ' ', ' ', 'X' } },
				
				g10 = {	{ ' ', 'X', 'X' },
						{ 'O', 'O', ' ' }, 
						{ ' ', 'X', ' ' } };
	//@formatter:on
	@Test
	public void testPlayRandom()
	{
		ArrayList<int[]> positionsValides = new ArrayList<>();
		int cpt0 = 0, cpt1 = 0, cpt2 = 0, cpt3 = 0, cpt4 = 0;
		for (int i = 0; i < 5; i++)
		{
			int[] a = new int[2];
			a[0] = i;
			a[1] = i;
			positionsValides.add(a);
		}
		for (int i = 0; i < 250; i++)
		{
			int[] a = IA_Util.playRandom(positionsValides);
			switch (a[0])
			{
				case 0:
					cpt0++;
					break;
				case 1:
					cpt1++;
					break;
				case 2:
					cpt2++;
					break;
				case 3:
					cpt3++;
					break;
				case 4:
					cpt4++;
					break;

			}
			assertTrue(positionsValides.contains(a));
		}
		DecimalFormat format = new DecimalFormat("#.#");
		assertTrue(
				Double.parseDouble(format.format((double) cpt0 / 250)) >= 0.1);
		assertTrue(
				Double.parseDouble(format.format((double) cpt1 / 250)) >= 0.1);
		assertTrue(
				Double.parseDouble(format.format((double) cpt2 / 250)) >= 0.1);
		assertTrue(
				Double.parseDouble(format.format((double) cpt3 / 250)) >= 0.1);
		assertTrue(
				Double.parseDouble(format.format((double) cpt4 / 250)) >= 0.1);
		positionsValides = new ArrayList<int[]>();
		assertNull(IA_Util.playRandom(positionsValides));
		positionsValides = null;
		assertNull(IA_Util.playRandom(positionsValides));

	}

	@Test
	public void testPlayFacile()
	{
		int[] a = new int[2];
		ArrayList<int[]> positionsValides = positionsVacantes(g1);
		int[] pos =
		{ 0, 0 };
		testFacile(pos, positionsValides);
		positionsValides = positionsVacantes(g2);
		pos[0] = 2;
		pos[1] = 0;
		testFacile(pos, positionsValides);
		positionsValides = positionsVacantes(g6);
		pos[0] = 0;
		pos[1] = 1;
		testFacile(pos, positionsValides);

	}

	private void testFacile(int[] pos, ArrayList<int[]> positionsValides)
	{
		for (int i = 0; i < 250; i++)
		{
			boolean reussi = false;
			int[] resultat = IA_Util.playFacile(pos, positionsValides);
			for (int x = -1; x <= 1 && !reussi; x++)
			{
				for (int y = -1; y <= 1 && !reussi; y++)
				{
					if (!(x == 0 && y == 0) && resultat[0] == pos[0] + x
							&& resultat[1] == pos[1] + y)
						reussi = true;
				}
			}
			if (!reussi)
				fail();
		}
	}

	@Test
	public void testPlayDifficile()
	{
		ArrayList<int[]> posVides = positionsVacantes(g8);
		int[] posJoue = IA_Util.playDifficile(posVides, g8);
		assertTrue(posJoue[0] == 2 && posJoue[1] == 2);
		// if (!(posJoue[0] == 2 && posJoue[1] == 2))
		// fail();
		posVides = positionsVacantes(g9);
		posJoue = IA_Util.playDifficile(posVides, g9);
		assertTrue(posJoue[0] == 1 && posJoue[1] == 2);
		// if (!(posJoue[0] == 1 && posJoue[1] == 2))
		// fail();
		posVides = positionsVacantes(g10);
		posJoue = IA_Util.playDifficile(posVides, g10);
		assertTrue(posJoue[0] == 1 && posJoue[1] == 2);
		// if (!(posJoue[0] == 1 && posJoue[1] == 2))
		// fail();
	}

	@Test
	public void testPlacementInit()
	{
		for (int nbrFois = 0; nbrFois < 250; nbrFois++)
		{
			int[] a = IA_Util.placementInit(g1, positionsVacantes(g1));
			assertTrue(a[0] == 1 && a[1] == 1);

			a = IA_Util.placementInit(g5, positionsVacantes(g5));
			boolean reussi = false;
			for (int i = 0; i < 4 && !reussi; i++)
			{
				switch (i)
				{
					case 0:
						reussi = a[0] == 0 && a[1] == 0;
						break;
					case 1:
						reussi = a[0] == 0 && a[1] == 2;
						break;
					case 2:
						reussi = a[0] == 2 && a[1] == 0;
						break;
					case 3:
						reussi = a[0] == 2 && a[1] == 2;
						break;
				}
			}
			assertTrue(reussi);

			a = IA_Util.placementInit(g6, positionsVacantes(g6));
			assertTrue(a[0] == 1 && a[1] == 1);
		}
	}

	private ArrayList<int[]> positionsVacantes(char[][] grille)
	{
		ArrayList<int[]> retour = new ArrayList<>();
		int[] temp = new int[2];
		for (int i = 0; i < grille.length; i++)
		{
			for (int j = 0; j < grille[0].length; j++)
			{
				if (grille[i][j] == TTT_Grille.cars[2])
				{
					temp[0] = i;
					temp[1] = j;
					retour.add(temp.clone());
				}
			}
		}
		return retour;
	}
}
