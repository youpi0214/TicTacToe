package celebration;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import vue.TTT_Vue;

public class Visuel implements Runnable
{

	final double VITESSEGO = 3, VITESSESTOP = 0;
	boolean move;
	DoubleProperty x, y;
	TTT_Vue vue;
	Timeline loop;

	public Visuel(TTT_Vue pVue)
	{
		if (pVue != null)
		{
			vue = pVue;
			x = new SimpleDoubleProperty(0);
			y = new SimpleDoubleProperty(0);
			move = false;
		}
	}

	public Timeline getTime()
	{
		return loop;
	}

	public DoubleProperty getX()
	{
		return x;
	}

	public DoubleProperty getY()
	{
		return y;
	}

	public void flipMove()
	{
		move = !move;
	}

	public boolean getMove()
	{
		return move;
	}

	@Override
	public void run()
	{

		loop = new Timeline(new KeyFrame(Duration.millis(10),
				new EventHandler<ActionEvent>()
				{

					double deltaX = 1;
					double deltaY = 1;

					@Override
					public void handle(final ActionEvent t)
					{

						boolean atRightBorder = ((ImageView) vue
								.getVisageVictoire()).getTranslateX()
								+ (((ImageView) vue.getVisageVictoire())
										.getFitWidth()) > vue.getGridPane()
												.getWidth() - 70;
						boolean atLeftBorder = ((ImageView) vue
								.getVisageVictoire()).getTranslateX() < 0;
						boolean atBottomBorder = ((ImageView) vue
								.getVisageVictoire()).getTranslateY()
								+ (((ImageView) vue.getVisageVictoire())
										.getFitHeight()) > vue.getGridPane()
												.getHeight() + 20;
						boolean atTopBorder = ((ImageView) vue
								.getVisageVictoire()).getTranslateY() < 0;

						if (atRightBorder || atLeftBorder)
						{
							deltaX *= -1;
						}
						if (atBottomBorder || atTopBorder)
						{
							deltaY *= -1;
						}
						x.set(x.getValue()
								+ (move ? VITESSEGO : VITESSESTOP) * deltaX);
						y.set(y.getValue()
								+ (move ? VITESSEGO : VITESSESTOP) * deltaY);
					}
				}));

		loop.setCycleCount(Timeline.INDEFINITE);
		loop.play();

	}

	public void stop()
	{
		loop.stop();
		x.set(0);
		y.set(0);
	}

	public void start()
	{
		loop.play();
	}

}
