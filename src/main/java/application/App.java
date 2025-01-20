package application;

import ctrl.TTT_Ctrl;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application
{

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		TTT_Ctrl ctrl = new TTT_Ctrl();
		Image icon = new Image("/images/icon.png");
		primaryStage.setScene(ctrl.getVue().getScene());
		primaryStage.getIcons().add(icon);
		primaryStage.show();
		primaryStage.setOnCloseRequest(e -> {
			ctrl.getVue().handleQuitter();
			e.consume();
		});

	}

	public static void main(String[] args)
	{
		Application.launch();
	}
}
