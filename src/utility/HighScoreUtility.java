/*
 * Author: 	Wattanai Thangsrirojkul		5831062121 Section 33
 * 			Sivakorn Chanpitayanukulkij 5830535521 Section 33
 */
package utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import exception.ScoreParsingException;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.TextInputDialog;
import main.Main;

public class HighScoreUtility {

	public static class HighScoreRecord implements Comparable<HighScoreRecord> {
		private String name = "";
		private int score = 0;

		private HighScoreRecord(String name, int score) {
			this.name = name;
			this.score = score;
		}

		/*
		 * Parse the given string "record" record format is name:score
		 */
		public HighScoreRecord(String record) throws ScoreParsingException {
			String[] strings = record.split(":");
			if (strings.length != 2)
				throw new ScoreParsingException(1);
			try {
				this.name = strings[0];
				this.score = Integer.parseInt(strings[1]);
			} catch (NumberFormatException e) {
				throw new ScoreParsingException(0);
			}
		}

		public String getRecord() {
			return name.trim() + ":" + score;
		}

		private static String[] defaultRecord() {
			return new String[] { "A:800", "B:350", "C:300", "D:250", "E:200", "F:100", "G:40", "H:30", "I:20",
					"J:10" };
		}

		@Override
		public int compareTo(HighScoreRecord o) {
			if (this.score > o.score) {
				return -1;
			} else if (this.score == o.score) {
				return 0;
			} else {
				return 1;
			}
		}
	}

	private static HighScoreRecord[] highScoreRecord = null;

	private static String readFileName = "highscore";

	/*
	 * Display player's score and record if the player rank is 10 or higher.
	 */
	public static void displayErrorAlert(){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText("Error loading highscore record");
		alert.show();
	}
	
	public static void displayGameOverAlert(int score){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Game over");
		alert.setHeaderText(null);
		alert.setContentText("Game over\n" + "Your score is " + score);

		alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
			@Override
			public void handle(DialogEvent event) {
				Main.instance.getDrawingAnimation().stop();
				InputUtility.setMouseLeftDown(false);
				Main.instance.getStartScreen().screenTransitionIn();
				Main.instance.changeToStartScreen();
			}
		});
		alert.show();
	}
	
	public static void displayRankedAlert(int index,int score){
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("High score");
		dialog.setHeaderText(null);
		dialog.setContentText("Congratulation, you are ranked " + (index + 1) + "\n" + "Please enter your name");

		final int final_index = index;
		dialog.setOnHidden(new EventHandler<DialogEvent>() {

			@Override
			public void handle(DialogEvent event) {
				try {
					String name = ((TextInputDialog) event.getTarget()).getResult();
					if (name != null) {
						if(name.length() > 10) {
							name = name.substring(0, 10);
						}
						highScoreRecord[final_index] = new HighScoreRecord(name, score);
						BufferedWriter out = new BufferedWriter(new FileWriter("highscore"));
						String string = "";
						for (HighScoreRecord record : highScoreRecord) {
							string += record.getRecord() + "\n";
						}
						out.write(getXORed(string));
						out.close();
					}
				} catch (IOException e1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText(null);
					alert.setContentText("Error saving high score record");
					alert.setTitle("Error");
					alert.show();
					highScoreRecord = null;
					return;
				}
				Main.instance.getDrawingAnimation().stop();
				InputUtility.setMouseLeftDown(false);
				Main.instance.getStartScreen().screenTransitionIn();
				Main.instance.changeToStartScreen();
			}
		});
		dialog.show();
	}
	
	public static void recordHighScore(int score) {
		if (!loadHighScore() || highScoreRecord == null) {
			displayErrorAlert();
			return;
		}
		int index = highScoreRecord.length;
		for (int i = 0; i < highScoreRecord.length; i++) {
			if (score > highScoreRecord[i].score) {
				index = i;
				break;
			}
		}
		if (index >= highScoreRecord.length) {
			displayGameOverAlert(score);
		} else {
			for (int i = highScoreRecord.length - 1; i >= index + 1; i--) {
				highScoreRecord[i] = highScoreRecord[i - 1];
			}
			displayRankedAlert(index,score);
		}
	}

	public static HighScoreRecord[] loadTop10() {
		setReadFileName("highscore");
		if (!loadHighScore() || highScoreRecord == null) {
			displayErrorAlert();
			return null;
		}
		return highScoreRecord;
	}

	private static boolean loadHighScore() {
		File f = new File(readFileName);
		// If no high score, create default

		if (!f.exists()) {
			if (!createDefaultScoreFile())
				return false;
		}
		// Read high score -- if fail: delete the file, create default one and
		// read it again
		if (!readAndParseScoreFile(f)) {
			f.delete();
			if (!createDefaultScoreFile())
				return false;
			return readAndParseScoreFile(f);
		}
		return true;
	}

	private static boolean readAndParseScoreFile(File f) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(f));
			highScoreRecord = new HighScoreRecord[10];
			String str = "";
			int c;
			while ((c = in.read()) != -1) {
				str += (char) c;
			}
			in.close();
			String[] records = getXORed(str).split("\n");
			for (int i = 0; i < highScoreRecord.length; i++) {
				try {
					highScoreRecord[i] = new HighScoreRecord(records[i]);
				} catch (ScoreParsingException e) {
					highScoreRecord[i] = new HighScoreRecord("ERROR_RECORD", 0);
				}
			}
			Arrays.sort(highScoreRecord);
			return true;
		} catch (Exception e) {
			highScoreRecord = null;
		}
		return false;
	}

	private static boolean createDefaultScoreFile() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("highscore"));
			String str = "";
			for (String s : HighScoreRecord.defaultRecord()) {
				str += s + "\n";
			}
			str = str.trim();
			out.write(getXORed(str));
			out.close();
			return true;
		} catch (IOException e1) {
			highScoreRecord = null;
			return false;
		}
	}

	private static final byte[] key = "RmAAq2b5d8fjgu9dhher".getBytes();

	// This method does both encryption and decryption
	private static String getXORed(String in) {
		byte[] inData = in.getBytes();
		for (int i = 0; i < inData.length; i++) {
			inData[i] = (byte) (inData[i] ^ key[i % key.length]);
		}
		return new String(inData);
	}

	public static void setReadFileName(String name) {
		readFileName = name;
	}
}
