package main;

import graphic.PlayerStatus;
import gui.CollectionScreen;
import gui.ConfigurableSettings;
import gui.GameScreen;
import gui.StartScreen;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import logic.GameLogic;
import logic.highscore.HighScoreUtility;

public class Main extends Application {

	public static Main instance;
	private Stage primaryStage;

	private CollectionScreen collectionScreen;
	private GameScreen gameScreen;
	private StartScreen startScreen;
	private ConfigurableSettings configurableSettings;

	private GameLogic gameLogic;
	private Thread gameThread;
	private AnimationTimer drawingAnimation;

	Scene collectionScene, gameScene, startScene, configScene;

	@Override
	public void start(Stage primaryStage) throws Exception {
		instance = this;

		this.gameLogic = new GameLogic();

		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Fruit Samurai");
		this.primaryStage.setResizable(false);
		this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				System.exit(0);
			}
		});

		this.gameScreen = new GameScreen();
		this.collectionScreen = new CollectionScreen();
		this.startScreen = new StartScreen();
		this.configurableSettings = new ConfigurableSettings();

		this.gameScene = new Scene(gameScreen, ConfigurableSettings.screenWidth, ConfigurableSettings.screenHeight);
		this.collectionScene = new Scene(collectionScreen, ConfigurableSettings.screenWidth,
				ConfigurableSettings.screenHeight);
		this.configScene = new Scene(configurableSettings, ConfigurableSettings.screenWidth, ConfigurableSettings.screenHeight);
		this.startScene = new Scene(startScreen, ConfigurableSettings.screenWidth, ConfigurableSettings.screenHeight);

		this.primaryStage.setScene(this.startScene);
		this.primaryStage.show();

		this.gameThread = new Thread(() -> {
			while (true) {
				// TODO Add GameOver transition
				if (PlayerStatus.instance.isGameOver()) {
					Platform.runLater(() -> {
						HighScoreUtility.recordHighScore(PlayerStatus.instance.getScore());
					});
					PlayerStatus.instance.setGameOver(false);
					return;
				}
				try {
					gameLogic.updateLogic();
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
			}
		});

		this.drawingAnimation = new AnimationTimer() {
			@Override
			public void handle(long now) {
				drawGameScreen();
			}
		};

	}

	public GameLogic getGameLogic() {
		return this.gameLogic;
	}

	public CollectionScreen getCollectionScreen() {
		return collectionScreen;
	}

	public StartScreen getStartScreen() {
		return startScreen;
	}

	public AnimationTimer getDrawingAnimation() {
		return drawingAnimation;
	}

	public GameScreen getGameScreen() {
		return this.gameScreen;
	}

	public void changeToStartScreen() {
		this.primaryStage.setScene(startScene);
		this.gameThread.interrupt();
		this.drawingAnimation.stop();
	}

	public void changeToGameScreen() {
		this.primaryStage.setScene(gameScene);
		this.gameThread.start();
		drawingAnimation.start();
	}
	
	public void changeToCongfigurationSetting() {
		configurableSettings.transIn();
		this.primaryStage.setScene(configScene);
		this.gameThread.interrupt();
		this.drawingAnimation.stop();
	}

	public void changeToCollectionScreen() {
		this.primaryStage.setScene(collectionScene);
		this.gameThread.interrupt();
		this.drawingAnimation.stop();
	}

	public void closeScreen() {
		this.primaryStage.close();
		this.gameThread.interrupt();
		this.drawingAnimation.stop();
	}

	public void drawGameScreen() {
		this.gameScreen.paintComponents();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
