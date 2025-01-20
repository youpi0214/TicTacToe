package vue;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import celebration.AudioPlayer;
import celebration.Sonorisation;
import celebration.Visuel;
import ctrl.TTT_Ctrl;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import modele.TTT_Grille;
import modele.nivSelect;
import javafx.animation.*;

public class TTT_Vue
{

	/**
	 * Temps en nanosecondes
	 */
	private int temps;
	/**
	 * chronomètre
	 */
	private Timer clockyBoii;
	/**
	 * Contrôleur, permet d'accéder au modèle
	 */
	private TTT_Ctrl ctrl;
	/**
	 * 
	 */
	private Scene scene;
	/**
	 * 
	 */
	private GridPane root;
	/**
	 * Symbole des joueurs
	 */
	private Image xI, oI;
	/**
	 * 
	 */
	private SimpleStringProperty chrono;
	/**
	 * grille du Tic Tac Toe
	 */
	private Button[][] boutons;
	/**
	 * niveau de difficulté
	 */
	private ToggleGroup niveau;
	/**
	 * animation
	 */
	private ParallelTransition transition;
	/**
	 * niveau sélectionné
	 */
	private nivSelect niveauActuel;
	/**
	 * effet de victoire
	 */
	boolean celebrationEnMarche;
	/**
	 * musique de victoire
	 */
	private AudioPlayer sonDeLaVictoire;
	/**
	 * 
	 */
	private ImageView visageDeLaVictoire;
	/**
	 * 
	 */
	private Visuel deplacement;

	/**
	 * afficheur du niveau actuel
	 */
	@FXML
	Label niveau_LB;

	/**
	 * afficheur du chronomètre
	 */
	@FXML
	Label horloge_LB;

	/**
	 * niveau aléatoire
	 */
	@FXML
	RadioMenuItem alea;
	/**
	 * niveau facile
	 */
	@FXML
	RadioMenuItem fafa;
	/**
	 * niveau difficile
	 */
	@FXML
	RadioMenuItem tof;
	/**
	 * mode 2 joueurs humain
	 */
	@FXML
	RadioMenuItem player2;

	public TTT_Vue(TTT_Ctrl pCtrl) throws IOException
	{
		temps = 0;
		FXMLLoader loader = new FXMLLoader(
				this.getClass().getResource("/vue/TicTacToe.fxml"));
		loader.setController(this);
		root = loader.load();
		ctrl = pCtrl;
		scene = new Scene(root);
		scene.getStylesheets()
				.add(getClass().getResource("/style/TicTacToe.css").toString());

		xI = new Image(getClass().getResource("/images/TTTX.png").toString());
		oI = new Image(getClass().getResource("/images/TTTO.png").toString());

		// animation de la celebration
		sonDeLaVictoire = new AudioPlayer(Sonorisation.MOTTO);
		visageDeLaVictoire = new ImageView(
				this.getClass().getResource("/images/Motto.png").toString());
		visageDeLaVictoire.setPreserveRatio(true);
		visageDeLaVictoire.setFitHeight(120);
		deplacement = new Visuel(this);
		visageDeLaVictoire.translateXProperty().bind(deplacement.getX());
		visageDeLaVictoire.translateYProperty().bind(deplacement.getY());
		celebrationEnMarche = false;

		genererBoutons();
		appliquerHoverEffet();
		chrono = new SimpleStringProperty();
		clock();
		horloge_LB.textProperty().bind(chrono);

		setToggleGroup();

	}

	/**
	 * Met en place la liste de niveau
	 */
	private void setToggleGroup()
	{
		niveau = new ToggleGroup();
		alea.setText("Aléatoire");
		fafa.setText("Facile");
		tof.setText("IA");
		player2.setText("2 Joueurs");
		niveau.getToggles().addAll(alea, fafa, tof, player2);
		niveau.selectToggle(player2);

		// set niveau 2 joueurs comme niveau par defaut (lorsqu'on ouvre
		// l'application)
		niveau_LB.setText(
				((RadioMenuItem) niveau.getSelectedToggle()).getText());
		niveauActuel = nivSelect.JOUEUR;

		niveau.selectedToggleProperty().addListener(new ChangeListener<Toggle>()
		{

			@Override
			public void changed(ObservableValue<? extends Toggle> observable,
					Toggle oldValue, Toggle newValue)
			{
				setNiveau((RadioMenuItem) newValue);
				handleRecommencer();
				niveau_LB.setText(((RadioMenuItem) newValue).getText());
			}
		});
	}

	/**
	 * change le niveau aussitôt que l'utilisateur sélectionne un niveau
	 * différent
	 * 
	 * @param nivSelection niveau sélectionné
	 */
	private void setNiveau(RadioMenuItem nivSelection)
	{

		if (nivSelection.equals(alea))
			niveauActuel = nivSelect.ALEATOIRE;
		else if (nivSelection.equals(fafa))
			niveauActuel = nivSelect.FACILE;
		else if (nivSelection.equals(tof))
			niveauActuel = nivSelect.DIFFICILE;
		else if (nivSelection.equals(player2))
			niveauActuel = nivSelect.JOUEUR;
	}

	/**
	 * instancie le chronomètre
	 */
	public void clock()
	{
		TimerTask timer = new TimerTask()
		{

			@Override
			public void run()
			{
				Platform.runLater(() -> {
					chrono.set(formaterHMS(temps++));

				});

			}
		};
		clockyBoii = new Timer();
		clockyBoii.schedule(timer, 0, 1000l);
	}

	/**
	 * Retourne le chronomètre
	 * 
	 * @return le chronomètre
	 */
	public Timer getClock()
	{
		return clockyBoii;
	}

	/**
	 * Détermine le format d'affichage du temps
	 * 
	 * @param pTemps temps du jeu
	 * @return le temps selon un format défini (HH:MM:SS)
	 */
	private String formaterHMS(int pTemps)
	{
		return String.format("%02d:%02d:%02d", pTemps / 3600,
				(pTemps / 60) % 60, pTemps % 60);
	}

	/**
	 * Remet le chronomètre à zéro
	 */
	public void clearClock()
	{
		temps = 0;
	}

	/**
	 * Retourne la scene
	 * 
	 * @return la scene
	 */
	public Scene getScene()
	{
		return scene;
	}

	/**
	 * génère les boutons puis les place dans la grille de jeu
	 */
	public void genererBoutons()
	{
		boutons = new Button[TTT_Grille.GROSSEUR_GRILLE][TTT_Grille.GROSSEUR_GRILLE];
		for (int i = 0; i < boutons.length; i++)
		{
			for (int j = 0; j < boutons[0].length; j++)
			{
				boutons[i][j] = new Button();
				boutons[i][j].getStyleClass().add("case");
				root.add(boutons[i][j], i + 1, j + 2);
				boutons[i][j].getStyleClass().add("X");
				int[] pos =
				{ i, j };
				boutons[i][j].setOnAction(e -> {
					handlePlacement(pos);
				});
			}
		}
	}

	/**
	 * donne un effet à tout les boutons
	 */
	public void appliquerHoverEffet()
	{
		for (Button[] b : boutons)
		{
			for (Button b2 : b)
			{
				if (b2.getGraphic() == null)
				{
					appliquerHover(b2);
				}
			}
		}
	}

	/**
	 * enlève l'effet donné au bouton
	 * 
	 * @param b bouton sur lequel l'effet sera enlevé
	 */
	private void eneleverHover(Button b)
	{
		b.setOnMouseEntered(null);
		b.setOnMouseExited(null);

	}

	/**
	 * affiche sur le bouton quelle joueur joue présentement lorsque le pointeur
	 * de la souris le surplombe
	 * 
	 * @param b bouton sur lequel l'effet sera enlevé
	 */
	private void appliquerHover(Button b)
	{
		b.setOnMouseEntered(e -> {
			ImageView img = new ImageView(ctrl.getTour() ? xI : oI);
			img.setPreserveRatio(true);
			img.setFitHeight(70);
			img.setOpacity(0.3);
			((Button) (e.getSource())).setGraphic(img);
		});
		b.setOnMouseExited(e -> {
			((Button) (e.getSource())).setGraphic(null);
		});

	}

	/**
	 * désactive tout les boutons sauf ce qui sont victorieux, arrête le temps
	 * puis enclenche l'animation de célébration
	 * 
	 * @param victoire
	 */
	public void gagner(int[][] victoire)
	{
		int[] pos = new int[2];
		int index = 0;
		boolean victorieux;
		Button[] anims = new Button[3];
		for (Button[] b1 : boutons)
		{
			for (Button b : b1)
			{
				victorieux = false;
				pos[0] = GridPane.getRowIndex(b) - 2;
				pos[1] = GridPane.getColumnIndex(b) - 1;
				for (int i = 0; i < victoire.length; i++)
				{
					if (index < 3 && pos[0] == victoire[i][0]
							&& pos[1] == victoire[i][1])
					{
						victorieux = true;
					}
				}
				if (victorieux)
				{
					anims[index] = b;
					index++;
				}
				else
					b.setDisable(true);
			}
		}
		horloge_LB.textProperty().unbind();
		appliquerAnim(anims);
		commencerCelebration();
	}

	/**
	 * désactive tout les boutons lorsque la partie fini sans vainceur et arrête
	 * le temps
	 */
	public void draw()
	{
		for (Button[] b1 : boutons)
		{
			for (Button b : b1)
			{
				b.setDisable(true);
			}
		}

		horloge_LB.textProperty().unbind();
	}

	/**
	 * retourne l'image affiché lors de la victoire
	 * 
	 * @return une image
	 */
	public ImageView getVisageVictoire()
	{
		return visageDeLaVictoire;
	}

	/**
	 * Part la musique et le déplacement de l'image sur l'écran
	 */
	private void commencerCelebration()
	{
		sonDeLaVictoire.startPlaying();
		root.getChildren().add(visageDeLaVictoire);
		deplacement.flipMove();
		if (deplacement.getMove())
		{
			if (deplacement.getTime() == null)
				deplacement.run();
			else
				deplacement.start();
		}
		celebrationEnMarche = true;
	}

	/**
	 * arrête la musique et enlève l'image de l'écran
	 */
	private void arreterCelebration()
	{
		sonDeLaVictoire.stopPlaying();
		root.getChildren().remove(visageDeLaVictoire);
		deplacement.flipMove();
		if (!deplacement.getMove())
			deplacement.stop();
		celebrationEnMarche = false;
	}

	/**
	 * Retourne la grille de boutons
	 * 
	 * @return grille de boutons
	 */
	public Button[][] getBoutons()
	{
		return boutons;
	}

	/**
	 * Lorsqu'un bouton est cliqué (utilisateur qui joue), si le jeu effectué
	 * par l'utilisateur est valide et que la partie n'a pas atteint une phase
	 * terminal, l'AI joue son tour tout de suite
	 * 
	 * @param b
	 */
	public void handlePlacement(int[] b)
	{

		if (ctrl.jouer(b[1], b[0])
				&& !(ctrl.getNiveau().equals(nivSelect.JOUEUR))
				&& ctrl.getPosVides() && !ctrl.unVictorieux())
			ctrl.jouerIA();
		else if (!ctrl.getPosVides() && !ctrl.unVictorieux())
		{
			draw();
		}

	}

	/**
	 * applique l'animation sur le bouton ( qui fait partie des boutons
	 * gagnants) pour signifie visuellement le vainceur de la partie
	 * 
	 * @param b bouton gagnant
	 */
	public void appliquerAnim(Button[] b)
	{

		for (int i = 0, mil = 0; i < b.length; i++, mil += 250)
		{
			ScaleTransition scaleTransition = new ScaleTransition(
					Duration.millis(250), b[i].getGraphic());
			scaleTransition.setFromX(1);
			scaleTransition.setFromY(1);
			scaleTransition.setToX(2);
			scaleTransition.setToY(2);
			scaleTransition.setCycleCount(1);
			scaleTransition.setAutoReverse(true);
			RotateTransition rotate = new RotateTransition(Duration.millis(250),
					b[i].getGraphic());
			rotate.setByAngle(180);
			rotate.setCycleCount(1);
			rotate.setAutoReverse(true);
			transition = new ParallelTransition();
			transition.getChildren().addAll(scaleTransition, rotate);
			transition.setCycleCount(Integer.MAX_VALUE);
			transition.setAutoReverse(true);
			transition.setDelay(Duration.millis(mil));
			transition.play();
		}

	}

	/**
	 * place le symbole du joueur sur le bouton choisi selon le tour
	 * 
	 * @param i numéro de ligne de la grille de boutons
	 * @param j numéro de colonne de la grille de boutons
	 * @param estTourHumain
	 */
	public void placerImage(int i, int j, boolean estTourHumain)
	{
		ImageView img = new ImageView(estTourHumain ? xI : oI);
		img.setPreserveRatio(true);
		img.setFitHeight(70);
		getBoutons()[j][i].setGraphic(img);
		eneleverHover(getBoutons()[j][i]);

	}

	/**
	 * réactive tout les boutons, arrète tout célébration et animation puis
	 * repart le chronomètre à 0
	 */
	public void handleRecommencer()
	{
		for (Button[] b1 : boutons)
		{
			for (Button b : b1)
			{
				if (b.getGraphic() != null)
					b.setGraphic(null);
				b.setDisable(false);
			}
		}
		temps = 0;
		chrono.set(formaterHMS(temps));
		horloge_LB.textProperty().bind(chrono);
		appliquerHoverEffet();
		ctrl.recommencer();
		if (celebrationEnMarche)
			arreterCelebration();
	}

	/**
	 * Retourne le niveau sélectionné
	 * 
	 * @return un niveau de difficulté
	 */
	public nivSelect getNiveau()
	{
		return niveauActuel;
	}

	/**
	 * affiche une fenêtre d'alerte lorsque l'utilisateur essaie de quitter et
	 * réagit selon la réponse de celui-ci (ferme l'application si la réponse
	 * est oui et ne fait rien dans le cas contraire)
	 */
	public void handleQuitter()
	{
		ctrl.alerte(AlertType.CONFIRMATION, "Quitter",
				"Voulez-vous vraiment quitter?",
				"Vous perdrez toute progression!");
	}

	/**
	 * Retourne la grille de boutons
	 * 
	 * @return grille de boutons
	 */
	public GridPane getGridPane()
	{
		return root;
	}
}
