package modele;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Classe utilitaire pour la gestion de l'AI, permet à lIA de jouer selon
 * plusieurs niveaux de difficulté (aléatoire, facile et difficile)
 * 
 * @author Nicolas Boulet & Yannick André Ouamba
 *
 */
public class IA_Util
{
	/**
	 * Profondeur maximale de récursion
	 */
	private static int MAXPROF = 10;
	/**
	 * 
	 */
	private static final int MAXCOMP = -10000;
	/**
	 * 
	 */
	private static final int MINCOMP = 10000;

	/**
	 * Mélange la liste de de position vide puis choisi la premiere du lot
	 * 
	 * @param Liste de position vides
	 * @return position choisie au hasard
	 */
	public static int[] playRandom(ArrayList<int[]> temp)
	{

		if (temp != null && !temp.isEmpty())
			Collections.shuffle(temp);

		return (temp == null || temp.isEmpty()) ? null : temp.get(0);
	}

	/**
	 * l'AI choisi au hasard une position autour de la dernière jouée par son
	 * adversaire
	 * 
	 * @param dernierPosHumain
	 * @param posVides
	 * @return
	 */
	public static int[] playFacile(int[] dernierPosHumain,
			ArrayList<int[]> posVides)
	{

		ArrayList<int[]> temp = positionsFaciles(dernierPosHumain, posVides);
		// si aucune position proche du dernier choisi par l'utilisateur n'est
		// vide, l'ia choisi une position vide random
		temp = (temp.isEmpty()) ? posVides : temp;

		return playRandom(temp);
	}

	/**
	 * L'AI choisi une position optimale pour gagner ou empêcher l'adversaire de
	 * gagner
	 *
	 * @return une position optimale
	 */
	public static int[] playDifficile(ArrayList<int[]> posVides,
			char[][] pGrille)
	{
		int[] pos = null;

		if (posVides.size() == Math.pow(TTT_Grille.GROSSEUR_GRILLE, 2) - 1)
			pos = placementInit(pGrille, posVides);

		else
			pos = miniMaxLaunch(pGrille, posVides, TTT_Grille.cars[1]).getPos();

		return pos;
	}

	/**
	 * Produit la copie de la grille de caractère reçue en paramètre puis la
	 * retourne
	 * 
	 * @param pGrille grille de jeu
	 * @return une copie de la grille de jeu
	 */
	private static char[][] copyGrille(char[][] pGrille)
	{
		char[][] copy = new char[pGrille.length][pGrille[0].length];
		for (int i = 0; i < pGrille.length; i++)
			for (int j = 0; j < pGrille[0].length; j++)
				copy[i][j] = pGrille[i][j];

		return copy;
	}

	/**
	 * Retourne le meilleur scénario de jeu possible pour l'IA
	 * 
	 * @param pGrille grille de jeu
	 * @param posVides liste des positions vides
	 * @param player l'IA (le caractère 'O')
	 * @return un scénario où l'IA gagne ou empêche l'adversaire de gagner
	 */
	public static Scenario miniMaxLaunch(char[][] pGrille,
			ArrayList<int[]> posVides, char player)
	{
		// set la profondeur maximale
		MAXPROF = posVides.size();

		// retourne le Scenario qui a le pointage le plus élevé étant donné que
		// mon player est O
		return miniMaxCalcul(pGrille, posVides, 0, player, null);

	}

	/**
	 * Cherche par récursion le meilleur meilleur scénario possible dans lequel
	 * l'IA gagne ou empêche l'adversaire de gagner
	 * 
	 * @param pGrille grille de jeu
	 * @param posVides liste des positions vides
	 * @param profondeur niveau de récursion atteint
	 * @param player joueur courant (X ou O)
	 * @param dernierePos dernière position jouée par l'adversaire
	 * @return scénario optimale pour l'IA
	 */
	public static Scenario miniMaxCalcul(char[][] pGrille,
			ArrayList<int[]> posVides, int profondeur, char player,
			int[] dernierePos)
	{
		// Une copie des positions vides
		ArrayList<int[]> posCopy;
		// Le meilleur move, instantié à une valeur bidon
		Scenario meilleurMove = new Scenario(0, null);
		// Si on est au bout d'une branche, que ce soit par manque d'espace pour
		// placer
		// une pièce, par victoire ou par défaite, on retourne le score du noeud
		// actuel
		if (profondeur == MAXPROF
				|| partieFinie(pGrille, posVides.size()) != null)
		{
			return new Scenario(score(player, pGrille, posVides, profondeur),
					dernierePos);
		}
		// Coeur de minimax, vérifie si on est le joueur qui maximise(O) ou
		// celui qui
		// minimise(X)
		if (player == TTT_Grille.cars[1])
		{
			// set le score de base de la comparaison à une valeur démesurée,
			// soit -10000
			int meilleurScore = MINCOMP;
			// Pour toutes les positions dans les positions vides
			for (int[] pos : posVides)
			{
				// Copier la liste de positions vides
				posCopy = (ArrayList<int[]>) posVides.clone();
				// Copier la matrice dans laquelle on veut jouer
				char[][] nouvGrille = copyGrille(pGrille);
				// Jouer dans la grille copiée
				TTT_Grille.placer(nouvGrille, pos[0], pos[1], player);
				// Retirer la position jouée dans la copie des positions vides
				posCopy.remove(pos);
				// Appelle par récursivité miniMaxCalcul avec la matrice copiée,
				// les positions
				// vides copiés et dont la position jouée est retirée, la
				// profondeur
				// incrémentée, le joueur inversé, et la dernière position jouée
				// prise en note
				int pointage = miniMaxCalcul(nouvGrille, posCopy,
						profondeur + 1, player == 'X' ? 'O' : 'X', pos)
								.getScore();
				// Trouve le plus grand pointage entre le meilleur score actuel
				// et le pointage
				// et change le valeurs si celui-ci est meilleur
				if (pointage < meilleurScore)
				{
					meilleurScore = pointage;
					meilleurMove = new Scenario(meilleurScore, pos);
				}
			}
			return meilleurMove;
		}
		else
		{
			// set le score de base de la comparaison à une valeur démesurée,
			// soit -10000
			int meilleurScore = MAXCOMP;
			// Pour toutes les positions dans les positions vides
			for (int[] pos : posVides)
			{
				// Copier la liste de positions vides
				posCopy = (ArrayList<int[]>) posVides.clone();
				// Copier la matrice dans laquelle on veut jouer
				char[][] nouvGrille = copyGrille(pGrille);
				// Jouer dans la grille copiée
				TTT_Grille.placer(nouvGrille, pos[0], pos[1], player);
				// Retirer la position jouée dans la copie des positions vides
				posCopy.remove(pos);
				// Appelle par récursivité miniMaxCalcul avec la matrice copiée,
				// les positions vides copiés et dont la position jouée est
				// retirée, la
				// profondeur incrémentée, le joueur inversé, et la dernière
				// position jouée prise en note
				int pointage = miniMaxCalcul(nouvGrille, posCopy,
						profondeur + 1, player == 'X' ? 'O' : 'X', pos)
								.getScore();
				// Trouve le plus petit pointage entre le meilleur score actuel
				// et le pointage
				// et change le valeurs si celui-ci est inférieur
				if (pointage > meilleurScore)
				{
					meilleurScore = pointage;
					meilleurMove = new Scenario(meilleurScore, pos);
				}
			}
			return meilleurMove;
		}
	}

	/**
	 * vérifie si la partie a atteint un état terminal (victoire, défaite, ou
	 * égalité)
	 * 
	 * @param pGrille grille de jeu
	 * @param posVides liste des positions vides
	 * @return retourne un objet Character si la partie est fini et null dans le
	 *         cas contraire
	 */
	public static Character partieFinie(char[][] pGrille, int posVides)
	{
		// Si une victoire de O est relevée, retourne le Character 'O'
		if (JeuTTT.validerGagner(pGrille, TTT_Grille.cars[1]) != null)
		{
			return (Character) 'O';

		}
		else if (JeuTTT.validerGagner(pGrille, TTT_Grille.cars[0]) != null)
		{
			return (Character) 'O';
		}
		// Si une partie nulle est relevée, retourne le Character ' '
		else if (posVides == 0)
		{
			return (Character) ' ';
		}
		// Si une partie n'est pas encore terminée, retourne null
		return null;
	}

	/**
	 * determine le noeud final d'une branche selon sa profondeur puis le
	 * retourne.
	 * 
	 * @param player joueur courant (X ou O)
	 * @param pGrille grille de jeu
	 * @param posVides liste des positions vides
	 * @param profondeur niveau de récursion atteint
	 * @return le score du noeud final
	 */
	public static int score(char player, char[][] pGrille,
			ArrayList<int[]> posVides, int profondeur)
	{
		// Trouve le char qui représente l'adversaire
		char opp = player == TTT_Grille.cars[0] ? TTT_Grille.cars[1]
				: TTT_Grille.cars[0];
		// Trouve le résultat de la partie finale, 'O', 'X', ' ' ou null
		Character resultat = partieFinie(pGrille, posVides.size());
		// Si le résultat de la partie finale est que le joueur actuel gagne,
		// retourner
		// 10 - profondeur
		if (resultat != null && resultat.charValue() == player)
		{
			return 10 - profondeur;
			// Si le résultat de la partie finale est que le joueur actuel
			// gagne, retourner
			// profondeur - 10, c'est à dire une valeur entre 0 et -10
		}
		else if (resultat != null && resultat.charValue() == opp)
		{
			return profondeur - 10;
			// Si le résultat de la partie est une partie nulle, retourner 0
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Lors de son premier jeu, l'IA joue en s'adaptant au premier jeu de
	 * l'adersaire en minimisant les chances de l'adversaire d'obtenir un
	 * avantage (simule une prise de stratégie)
	 * 
	 * @param pGrille grille de jeu
	 * @param posVides liste des positions vides
	 * @return une position vide
	 */
	public static int[] placementInit(char[][] pGrille,
			ArrayList<int[]> posVides)
	{
		int[] pos = new int[2];
		ArrayList<int[]> coins = coinsLibre(pGrille);
		if (pGrille[1][1] == TTT_Grille.cars[0])
		{
			pos = playRandom(coins);

		}
		else
		{
			pos[0] = 1;
			pos[1] = 1;
		}
		return pos;
	}

	/**
	 * Établie une liste des coins de la grille de jeu qui ne sont occupés par
	 * aucun joueur puis la retourne
	 * 
	 * @param pGrille grille de jeu
	 * @return la liste des coins libres
	 */
	private static ArrayList<int[]> coinsLibre(char[][] pGrille)
	{
		ArrayList<int[]> retour = new ArrayList<>();
		int[] temp = new int[2];
		for (int x = 0; x < pGrille.length; x++)
		{
			for (int y = 0; y < pGrille.length; y++)
			{
				if ((x == 0 || x == pGrille.length - 1)
						&& (y == 0 || y == pGrille.length - 1)
						&& (pGrille[x][y] == ' '))
				{
					temp[0] = x;
					temp[1] = y;
					retour.add(temp.clone());
				}
			}
		}
		return retour;
	}

	/**
	 * Établie une liste de positions directement autour de la dernière position
	 * jouée par l'adversaire
	 * 
	 * @param dernierPosHumain dernière position jouée par l'adversaire
	 * @param posVides listes de positions vides
	 * @return une liste de position adjacentes à la dernière position jouée par
	 *         l'adversaire
	 */
	private static ArrayList<int[]> positionsFaciles(int[] dernierPosHumain,
			ArrayList<int[]> posVides)
	{
		ArrayList<int[]> retour = new ArrayList<>();
		int[] temp = new int[2];
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				if (i == 0 && j == 0)
					continue;
				temp[0] = dernierPosHumain[0] + i;
				temp[1] = dernierPosHumain[1] + j;
				for (int[] pos : posVides)
				{
					if (pos[0] == temp[0] && pos[1] == temp[1])
					{
						retour.add(temp.clone());
						break;
					}
				}
			}
		}
		return retour;
	}

}