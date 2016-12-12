package logic.generator;

import java.util.Random;

import gui.ScreenProperties;
import logic.GameLogic;
import logic.entity.Bomb;

public class BombGenerator extends Generator{
	private GameLogic gameLogic;

	public BombGenerator(GameLogic gameLogic, long generateInterval) {
		super(generateInterval);
		this.gameLogic = gameLogic;
	}

	@Override
	public Runnable initRunnable() {
		return () -> {
			while (true) {
				try {
					Random random = new Random();
					int x = random.nextInt((int) ScreenProperties.screenWidth);
					int y = (int) ScreenProperties.screenHeight;
					int speedX = (50 + random.nextInt(200)) * (random.nextInt(2) == 0 ? -1 : 1);
					int speedY = 300 + random.nextInt(200);
					gameLogic.addEntity(new Bomb(x, y, speedX, speedY));
					Thread.sleep(generateInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.out.println("Generator interrupted");
					return;
				}
			}
		};
	}
}