package modele;

/**
 * Classe servant à emmagasiner une position et son score d'optimisation
 * (utilisée pour déterminer la meilleur position possible à jouer pour l'IA)
 * 
 * @author Nicolas Boulet & Yannick André Ouamba
 *
 */
public class Scenario
{

	/**
	 * score d'optimisation
	 */
	private int score;
	/**
	 * position vide d'une grille
	 */
	private int[] position;

	/**
	 * Instancie un scénario en lui assignant une position et un score
	 * 
	 * @param pScore
	 * @param pPos
	 */
	public Scenario(int pScore, int[] pPos)
	{
		score = pScore;
		position = pPos;
	}

	/**
	 * retourne la position du scénario
	 * 
	 * @return la position emmagasinée
	 */
	public int[] getPos()
	{
		return position;
	}

	/**
	 * retourne le score emmagasiné
	 * 
	 * @return le score emmagasiné
	 */
	public int getScore()
	{
		return score;
	}

	/**
	 * assigne un score
	 * 
	 * @param pScore score d'optimisation obtenu
	 */
	public void setScore(int pScore)
	{
		score = pScore;
	}

	/**
	 * assigne une position
	 * 
	 * @param pPos position obtenue
	 */
	public void setPos(int[] pPos)
	{
		position = pPos;
	}
}
