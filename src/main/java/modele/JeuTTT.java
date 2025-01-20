package modele;

import java.util.ArrayList;

/**
 * Classe gérant les actions du joueur humain et de l'IA, représente la
 * également la partie en cours
 * 
 * @author Nicolas Boulet & Yannick André Ouamba
 *
 */
public class JeuTTT
{
	private boolean estTourHumain;
	private ArrayList<int[]> posVacantes;
	private int[] dernierPosJouer;
	private nivSelect niveau;
	private char[][] grilleDeJeu;

	/**
	 * Commence une nouvelle partie selon le niveau choisi par l'utilisateur
	 * 
	 * @param pNiveau niveau de difficulté de l'IA
	 */
	public JeuTTT(nivSelect pNiveau)
	{
		nouvPartie(pNiveau);
	}

	/**
	 * Retourne la grille de jeu de la partie de Tic Tac Toe présent
	 * 
	 * @return la grille de jeu
	 */
	public char[][] getGrille()
	{
		return grilleDeJeu;
	}

	/**
	 * Créer la grille, place le niveau selon le choix de l'utilisateur, remplit
	 * la liste de position vacantes(non jouées) et donne le tour à
	 * l'utilisateur
	 * 
	 * @param pNiveau niveau de difficulté de l'IA
	 */
	public void nouvPartie(nivSelect pNiveau)
	{
		creerNouveauJeu();
		niveau = pNiveau;
		positionsVacantes();
		estTourHumain = true;
	}

	/**
	 * Génère une nouvelle grille de jeu vide
	 */
	private void creerNouveauJeu()
	{
		grilleDeJeu = new char[TTT_Grille.GROSSEUR_GRILLE][TTT_Grille.GROSSEUR_GRILLE];
		for (int i = 0; i < TTT_Grille.GROSSEUR_GRILLE; i++)
		{
			for (int j = 0; j < TTT_Grille.GROSSEUR_GRILLE; j++)
			{
				grilleDeJeu[i][j] = TTT_Grille.cars[2];
			}
		}
	}

	/**
	 * Reçoit les coordonnées de la case choisi par l'utilisateur puis essaie de
	 * joue à cette position. Si cela réussit, le nombre de case vide est réduit
	 * 
	 * @param i numéro de ligne de la grille
	 * @param j numéro de colonne de la grille
	 * @return vrai si le placement à réussi et faux dans le cas contraire
	 */
	public boolean jouerHumain(int i, int j)
	{
		boolean retour = false;

		// Utilisé pour le mode 2 joueurs. En mode vs ai, il n'a aucun impact
		// et est constamment égale a cars[0], soit le joueur X qui est
		// l'utilisateur
		char player = (getTour()) ? TTT_Grille.cars[0] : TTT_Grille.cars[1];

		// réduit le nombre
		if ((retour = TTT_Grille.placer(getGrille(), i, j, player)))
			reduirePosVide(i, j);

		return retour;
	}

	/**
	 * Lorsque l'ia ou l'utilisateur font un jeu valide, la case jouée est
	 * enlevée de la liste des cases vides
	 * 
	 * @param i numéro de ligne de la grille
	 * @param j numéro de colonne de la grille
	 */
	private void reduirePosVide(int i, int j)
	{
		for (int[] positionVide : posVacantes)
		{
			if (positionVide[0] == i && positionVide[1] == j)
			{
				dernierPosJouer = positionVide;
				posVacantes.remove(positionVide);
				break;
			}
		}

	}

	/**
	 * Détermine où l'ia joue selon le niveau de difficulté actuelle puis y
	 * place son "pions" (caractère 'O') (la difficulté actuelle est déterminé
	 * par l'utilisateur avant le début de la partie)
	 * 
	 * @param niveau niveau de difficulté de l'IA
	 * @return La position jouée par l'IA
	 */
	public int[] jouerIA()
	{
		int[] posJouer = null;
		if (posVacantes != null && posVacantes.size() > 0)
			switch (niveau)
			{

				case ALEATOIRE:
					posJouer = IA_Util.playRandom(posVacantes);
					reduirePosVide(posJouer[0], posJouer[1]);
					break;
				case FACILE:
					posJouer = IA_Util.playFacile(dernierPosJouer, posVacantes);
					reduirePosVide(posJouer[0], posJouer[1]);
					break;
				case DIFFICILE:
					posJouer = IA_Util.playDifficile(
							(ArrayList) posVacantes.clone(),
							this.getGrille().clone());
					break;
			}

		// enlève la position jouée de la liste de position libre
		if (posJouer != null)
		{
			TTT_Grille.placer(getGrille(), posJouer[0], posJouer[1],
					TTT_Grille.cars[1]);
			reduirePosVide(posJouer[0], posJouer[1]);
		}

		return posJouer;
	}

	/**
	 * Permet de changer de tour entre X et O (vrai pour le X et faux pour le O)
	 */
	public void changerTour()
	{
		estTourHumain = !estTourHumain;
	}

	/**
	 * Retourne quel joueur joue présentement
	 * 
	 * @return vrai pour le X et faux pour le O
	 */
	public boolean getTour()
	{
		return estTourHumain;
	}

	/**
	 * Vérifie si la liste de positions vides contient encore au moins une
	 * position
	 * 
	 * @return faux si la liste est vide et vrai si elle contient encore des
	 *         positions libres
	 */
	public boolean getPosVides()
	{
		return posVacantes.size() > 0;
	}

	/**
	 * Retourne le niveau de difficulté auquel joue l'utilisateur
	 * 
	 * @return niveau de difficulté
	 */
	public nivSelect getNiveau()
	{
		return niveau;
	}

	/**
	 * Valide si le joueur au tour actuelle a gagné ou non
	 * 
	 * @param pGrille grille de jeu
	 * @param joueur joueur actuelle (X ou O selon leur tour)
	 * @return retourne une matrice représentant les coordonnées des cases
	 *         gagnantes
	 */
	public static int[][] validerGagner(char[][] pGrille, char joueur)
	{
		int[] gagner = null;
		gagner = TTT_Grille.validerVictoire(pGrille, TTT_Grille.condH, joueur);
		if (gagner == null)
			gagner = TTT_Grille.validerVictoire(pGrille, TTT_Grille.condV,
					joueur);
		if (gagner == null)
			gagner = TTT_Grille.validerVictoire(pGrille, TTT_Grille.condD1,
					joueur);
		if (gagner == null)
			gagner = TTT_Grille.validerVictoire(pGrille, TTT_Grille.condD2,
					joueur);
		return TTT_Grille.transfertVecAMatPos(gagner);
	}

	/**
	 * Parcourir la grille au complet pour trouver toute les positions non
	 * jouées actuelle et les ajoute dans une liste
	 */
	protected void positionsVacantes()
	{
		posVacantes = new ArrayList<>();
		char[][] tempGrille = getGrille();

		int[] temp = new int[3];
		for (int i = 0; i < tempGrille.length; i++)
		{
			for (int j = 0; j < tempGrille[0].length; j++)
			{
				if (tempGrille[i][j] == TTT_Grille.cars[2])
				{
					temp[0] = i;
					temp[1] = j;
					posVacantes.add(temp.clone());
				}
			}
		}
	}

}
