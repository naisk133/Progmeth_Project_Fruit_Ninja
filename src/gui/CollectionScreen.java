package gui;

<<<<<<< HEAD
import Utility.DrawingUtility;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
=======
>>>>>>> origin/master
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
<<<<<<< HEAD
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import main.Main;
=======
>>>>>>> origin/master

public class CollectionScreen extends BorderPane {
	private highScorePane highScorePane;
	private bladeSelectionPane bladeSelectionPane;
	private StackPane backPane;
	
	public CollectionScreen() {
		String image = ClassLoader.getSystemResource("image/menu.jpg").toString();
		this.setStyle("-fx-background-image: url('" + image + "'); "
					 + "-fx-background-position: center center;"
		    	     + "-fx-background-repeat: stretch; -fx-background-size:" 
		    	     + ScreenProperties.screenWidth + " " + ScreenProperties.screenHeight
		    	     + ";");
		
		highScorePane = new highScorePane();
		bladeSelectionPane = new bladeSelectionPane();
		
		backPane = new StackPane();
		this.setPadding(new Insets(50));
		Button backButton = new Button("Back");
		backButton.setStyle("-fx-background-color:deepskyblue ; -fx-background-radius: 0,0,0,0; "
				+ "-fx-padding: 5 30 5 30; -fx-background-size:50;" + "-fx-text-fill: black; -fx-font-size: 40px;"
				+ "-fx-font-weight: bold; -fx-font-family: \"Arial\"; "
				+ "-fx-border-color: black; -fx-border-width: 5;");
		backButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				screenTransitionOut();
				PauseTransition pause = new PauseTransition(Duration.millis(700));
				pause.setOnFinished(event -> {
					Main.instance.getStartScreen().screenTransitionIn();
					Main.instance.changeToStartScreen();
				});
				pause.play();

			}

		});
		backPane.getChildren().add(backButton);
		
//		setLeft(highScorePane);
		setRight(bladeSelectionPane);
		setBottom(backPane);
		
	}
	
	public void screenTransitionIn() {
//		TranslateTransition highScoreTrans = new TranslateTransition(Duration.millis(700), highScorePane);
//		highScoreTrans.setFromX(ScreenProperties.screenWidth);
//		highScoreTrans.setByX(-ScreenProperties.screenWidth);
		TranslateTransition selectionTrans = new TranslateTransition(Duration.millis(700), bladeSelectionPane);
		selectionTrans.setFromX(ScreenProperties.screenWidth);
		selectionTrans.setByX(-ScreenProperties.screenWidth);
		TranslateTransition backTrans = new TranslateTransition(Duration.millis(700), backPane);
		backTrans.setFromX(ScreenProperties.screenWidth);
		backTrans.setByX(-ScreenProperties.screenWidth);
//		highScoreTrans.play();
		selectionTrans.play();
		backTrans.play();
	}
	
	public void screenTransitionOut() {
//		TranslateTransition highScoreTrans = new TranslateTransition(Duration.millis(700), highScorePane);
//		highScoreTrans.setByX(ScreenProperties.screenWidth);
		TranslateTransition selectionTrans = new TranslateTransition(Duration.millis(700), bladeSelectionPane);
		selectionTrans.setByX(ScreenProperties.screenWidth);
		TranslateTransition backTrans = new TranslateTransition(Duration.millis(700), backPane);
		backTrans.setByX(ScreenProperties.screenWidth);
//		highScoreTrans.play();
		selectionTrans.play();
		backTrans.play();
	}
	
	private class highScorePane {
		
	}
	
	private class bladeSelectionPane extends GridPane {
		private Button purpleBtn, blueBtn, greenBtn, redBtn, rainbowBtn;
		private Label selectColor;
		
		public  bladeSelectionPane() {
			setAlignment(Pos.CENTER);
			setPrefSize(ScreenProperties.screenWidth/2, ScreenProperties.screenHeight);
			setVgap(10);
			setHgap(10);
			setStyle("-fx-background-color: transparent; -fx-padding: 30");
			
			selectColor = new Label("Blade Color");
			selectColor.setPrefWidth(210);
			setLabel(selectColor, "mediumorchid");
			
			purpleBtn = new Button();
			blueBtn = new Button();
			greenBtn = new Button();
			redBtn = new Button();
			rainbowBtn = new Button();
			
			setBtn(purpleBtn, "mediumorchid");
			setBtn(blueBtn, "deepskyblue");
			setBtn(greenBtn, "limegreen");
			setBtn(redBtn, "red");
			setBtn(rainbowBtn, "linear-gradient(from 0% 50% to 100% 50%, red 3%, orange 16%, yellow 30%, limegreen 44%, lightblue 64%, blue 77%, purple 92%)");
			
			add(selectColor, 0, 0, 2, 1);
			add(purpleBtn, 0, 1);
			add(blueBtn, 1, 1);
			add(greenBtn, 0, 2);
			add(redBtn, 1, 2);
			add(rainbowBtn, 0, 3);
			
			addListener();
		}
		
		public void addListener() {
			purpleBtn.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					setLabel(selectColor, "mediumorchid");
				}
			});
			
			blueBtn.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					setLabel(selectColor, "deepskyblue");
				}
			});
			
			greenBtn.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					setLabel(selectColor, "limegreen");
				}
			});

			redBtn.setOnAction(new EventHandler<ActionEvent>() {
	
				@Override
				public void handle(ActionEvent event) {
					setLabel(selectColor, "red");
				}
			});
			
			rainbowBtn.setOnAction(new EventHandler<ActionEvent>() {
				
				@Override
				public void handle(ActionEvent event) {
					setLabel(selectColor, "linear-gradient(from 0% 50% to 100% 50%, red 3%, orange 16%, yellow 30%, limegreen 44%, lightblue 64%, blue 77%, purple 92%)");
				}
			});
			
		}
		
		public void setLabel(Label label, String color) {
			selectColor.setStyle("-fx-font-size: 30px; -fx-font-family:\"Arial Black\"; -fx-text-fill: " + color + ";"
					+ "-fx-text-alignment:center; -fx-background-color: white; -fx-padding: 0 5 0 5;"
					+ "-fx-border-color: black; -fx-border-width: 3;");
		}
		
		public void setBtn(Button Btn, String color) {
			Btn.setPrefSize(100, 100);
			Btn.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 0,0,0,0; "
					+ "-fx-background-size:50; -fx-border-color: black; -fx-border-width: 3;");
		}
	}
}
