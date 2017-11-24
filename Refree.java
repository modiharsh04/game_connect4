package assignment;

public class Refree implements Runnable {
	private Board b;

	public Refree(Board o) {
		this.b = o;
	}

	@Override
	public void run() {
		b.refreeDo();
	}
}
