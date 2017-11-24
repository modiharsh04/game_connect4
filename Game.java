package assignment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Game {
	public static void main(String[] args) {

		Board b = new Board();

		ExecutorService exec = Executors.newCachedThreadPool();

		exec.execute(new Player(b, 'Y'));
		exec.execute(new Player(b, 'R'));
		exec.execute(new Refree(b));
		
		exec.shutdown();
	}
}
