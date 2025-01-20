package modele;

/**
 * Classe utilitaire servant aux traitements sur la grille de Tic Tac Toe
 * 
 * 
 * @author Nicolas Boulet & Yannick André Ouamba
 *
 */
public class TTT_Grille
{

	/**
	 * Dimension par défaut des colonnes et des lignes, exemple : grosseur 3 =
	 * dimension 3x3
	 */
	public static final int GROSSEUR_GRILLE = 3;

	/**
	 * Condition pour vérifier verticalement. arguments :{case de départ de la
	 * vérification, case limite où s'arrête la vérification, valeur
	 * d'itération,
	 * 
	 * }
	 */
	public static final int[] condV =
	{ 0, GROSSEUR_GRILLE, 1, GROSSEUR_GRILLE, 0,
			(int) Math.pow(GROSSEUR_GRILLE, 2), GROSSEUR_GRILLE, 0, 0 };

	/**
	 * Condition pour vérifier horizontalement. arguments : *voir conDV*
	 */
	public static final int[] condH =
	{ 0, (int) Math.pow(GROSSEUR_GRILLE, 2), GROSSEUR_GRILLE, 1, 1,
			GROSSEUR_GRILLE, 1, 0, 0 };

	/**
	 * Condition pour vérifier la diagonale descendante. arguments : *voir
	 * conDV*
	 */
	public static final int[] condD1 =
	{ 0, 1, 1, GROSSEUR_GRILLE + 1, 0, (int) Math.pow(GROSSEUR_GRILLE, 2),
			GROSSEUR_GRILLE + 1 };

	/**
	 * Condition pour vérifier la diagonale ascendante. arguments : *voir conDV*
	 */
	public static final int[] condD2 =
	{ GROSSEUR_GRILLE - 1, GROSSEUR_GRILLE, 1, GROSSEUR_GRILLE - 1, 0,
			((int) Math.pow(GROSSEUR_GRILLE, 2)) - 1, GROSSEUR_GRILLE - 1 };

	public static char[] cars =
	{ 'X', 'O', ' ' };

	/**
	 * Vérifie s'il y'a une victoire selon la condition de vérification reçue la
	 * méthode retourne La position des cases gagnantes.
	 * 
	 * @param pGrille grille de jeu du Tic Tac Toe
	 * @param condition Le sens dans lequel il faut faire la vérification
	 *            (horizontal, vertical, diagonale inclinée vers le bas et celle
	 *            incliné vers le haut)
	 * @param player Joueur pour qui la vérification est effectuée (X ou O)
	 * @return La position des case gagnantes s'il y'a une victoire sous forme
	 *         vectorielle. Dans le cas contraire, retourne null
	 */
	protected static int[] validerVictoire(char[][] pGrille, int[] condition,
			char player)
	{
		boolean win = false;
		char[] vecCurrent = transfertMatVec(pGrille);
		int[] posVic = new int[3];
		int index = 0;
		for (int i = condition[0]; !win && i < condition[1]; i += condition[2])
		{
			boolean yes = vecCurrent[i] == player;
			win = yes;
			if (yes)
			{
				posVic = new int[GROSSEUR_GRILLE];
				posVic[0] = i;
			}
			index = 1;
			for (int a = condition[3]; yes && win && i + a < condition[5]
					+ (i * condition[4]); a += condition[6])
			{
				win = vecCurrent[i] == vecCurrent[i + a];
				posVic[index] = i + a;
				index++;
			}
		}
		return (win) ? posVic : null;
	}

	/**
	 * Transfère le vecteur des points gagnants en coordonnées de matrice puis
	 * les retourne
	 * 
	 * @param vec Vecteur des positions gagnantes
	 * @return les coordonnées matricielles des positions gagnantes
	 */
	public static int[][] transfertVecAMatPos(int[] vec)
	{
		int index = 0;
		int[][] retour = null;
		if (vec != null)
		{
			retour = new int[GROSSEUR_GRILLE][2];
			for (int i = 0, pos = 0; pos < Math.pow(GROSSEUR_GRILLE, 2); pos++)
			{
				if (pos != 0 && pos % GROSSEUR_GRILLE == 0)
					i++;
				if (index < GROSSEUR_GRILLE && pos == vec[index])
				{
					retour[index][0] = i;
					retour[index][1] = pos % GROSSEUR_GRILLE;
					index++;
				}
			}
		}

		return retour;
	}

	/**
	 * Transfère une matrice de caractère en vecteur de caractère puis le
	 * retourne
	 * 
	 * @param mat matrice représentant la grille de jeu
	 * @return Le vecteur représentant la matrice de caractère
	 */
	public static char[] transfertMatVec(char[][] mat)
	{
		char[] retour = new char[mat.length * mat.length];
		int index = 0;
		for (int i = 0; i < mat.length; i++)
		{
			for (int j = 0; j < mat[0].length; j++)
			{
				retour[index++] = mat[i][j];
			}
		}
		return retour;
	}

	/**
	 * Vérifie si la case à la position (i,j) est libre
	 * 
	 * @param i ligne de la grille
	 * @param j colonnes de la grille
	 * @return vrai si la position est vide et faux dans le cas contraire
	 */
	private static boolean validerPlacement(char[][] pGrille, int i, int j)
	{
		return pGrille[i][j] == cars[2];
	}

	/**
	 * Vérifie si la case à la position choisie (i,j) est libre avant de la
	 * placer le "pions" du joueur (X ou O)
	 * 
	 * @param i ligne de la grille
	 * @param j colonnes de la grille
	 * @return
	 */
	public static boolean placer(char[][] pGrille, int i, int j, char player)
	{
		boolean retour = false;
		if (validerPlacement(pGrille, i, j))
		{
			pGrille[i][j] = player;
			retour = true;
		}
		return retour;
	}

}
