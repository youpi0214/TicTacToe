package ctrl;

import java.io.IOException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import modele.JeuTTT;
import modele.TTT_Grille;
import modele.nivSelect;
import vue.TTT_Vue;

/**
 * classe messagère entre la vue et le modèle, sert à transférer l'information
 * entre les deux
 * 
 * @author Nicolas Boulet & Yannick André Ouamba
 *
 */
public class TTT_Ctrl
{

	/**
	 * interface visuel du jeu
	 */
	private TTT_Vue vue;
	/**
	 * classe de traitement du jeu
	 */
	JeuTTT jeu;

	/**
	 * état de la partie actuelle
	 */
	boolean gameGagne;

	/**
	 * créer l'interface visuel et se passe en paramètre à celui-ci puis
	 * commence une nouvelle partie
	 * 
	 * @throws IOException
	 */
	public TTT_Ctrl() throws IOException
	{
		vue = new TTT_Vue(this);
		jeu = new JeuTTT(vue.getNiveau());
		gameGagne = false;
	}

	/**
	 * Permet de recommencer une partie selon le niveau sélectionné dans
	 * l'interface visuel
	 */
	public void recommencer()
	{
		jeu.nouvPartie(vue.getNiveau());
		gameGagne = false;
	}

	/**
	 * retourne quel joueur appartient le tour actuel
	 * 
	 * @return vrai pour X et faux pour O
	 */
	public boolean getTour()
	{
		return jeu.getTour();
	}

	/**
	 * Fenêtre d'alerte lorsque l'utilisateur essaie de quitter le jeu
	 * 
	 * @param type type d'alerte à afficher
	 * @param titre titre de l'alerte
	 * @param message1 premier message affiché
	 * @param message2 deuxième message affiché
	 */
	public void alerte(AlertType type, String titre, String message1,
			String message2)
	{
		Alert a = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES,
				ButtonType.NO);
		a.setTitle(titre);
		a.setHeaderText(message1);
		a.getDialogPane().getStylesheets().add(
				this.getClass().getResource("/style/alert.css").toString());
		a.setContentText(message2);
		Optional<ButtonType> reponse = a.showAndWait();
		if (reponse.get().equals(ButtonType.YES))
		{
			vue.getClock().cancel();
			Platform.exit();
		}

	}

	/**
	 * lorsque l'utilisateur clique sur un bouton, si c'est une case valide, il
	 * joue. Vérifie après chaque son jeu s'il obtient une victoire ou non puis
	 * passe sont tour pour laisser l'ia jouer
	 *
	 * @param i numéro de la ligne
	 * @param j numéro de la colonne
	 * @return vrai si l'utilisateur a joué à une position valide
	 */
	public boolean jouer(int i, int j)
	{
		boolean succes = false;
		int[][] win = null;

		if (jeu.jouerHumain(i, j))
		{
			succes = true;
			vue.placerImage(i, j, jeu.getTour());
			victoireOuNon(win);
		}
		return succes;
	}

	/**
	 * Vérifie s'il y'a une victoire ou non puis change de tour Dans le cas
	 * d'une victoire, le jeu est interrompu et une le contrôleur donne à
	 * l'interface visuel les coordonnées des case gagnantes
	 * 
	 * @param win coordonnées des positions gagnantes
	 */
	public void victoireOuNon(int[][] win)
	{
		char player = (getTour()) ? TTT_Grille.cars[0] : TTT_Grille.cars[1];

		if ((win = JeuTTT.validerGagner(jeu.getGrille(), player)) != null)
		{
			gameGagne = true;
			vue.gagner(win);
		}
		jeu.changerTour();
	}

	/**
	 * Fait jouer l'IA et vérifie s'il obtient une victoire pour ensuite changer
	 * de tour
	 */
	public void jouerIA()
	{

		int[][] win = null;
		int[] pos = jeu.jouerIA();

		if (pos != null)
			vue.placerImage(pos[0], pos[1], jeu.getTour());

		victoireOuNon(win);
	}

	/**
	 * Retourne vrai ou faux selon la quantité de positions vides restantes
	 * 
	 * @return vrai s'il reste encore des positions vides
	 */
	public boolean getPosVides()
	{
		return jeu.getPosVides();
	}

	/**
	 * retourne l'interface visuel
	 * 
	 * @return l'interface visuel
	 */
	public TTT_Vue getVue()
	{
		return vue;
	}

	/**
	 * retourne vrai ou faux selon l'état du jeu
	 * 
	 * @return vrai s'il y'eu un victorieux
	 */
	public boolean unVictorieux()
	{
		return gameGagne;
	}

	/**
	 * retourne le niveau sélectionné dans l'interface visuel
	 * 
	 * @return le niveau sélectionné
	 */
	public nivSelect getNiveau()
	{
		return jeu.getNiveau();
	}

}