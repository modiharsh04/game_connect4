package assignment;

import java.security.SecureRandom;

public class Player implements Runnable {

	private char name;
	private SecureRandom random = new SecureRandom();
	private boolean flag = false;
	private Board b;

	public Player(Board o, char s) {
		this.name = s;
		this.b = o;
	}

	@Override
	public void run() {
		for (;;) {
			try {
				Thread.sleep(1000);
				b.lock.lock();
				while (b.ref_check == 1) {
					b.canWrite.await();
				}
				b.ref_check = 1;
				if (b.is_board_full == 1) {
					b.ref_check=0;
					b.canWrite.signalAll();
					b.lock.unlock();
					return;
				}
				System.out.printf("\n..........%c's turn........\n",name);
				do {
					
					b.colIndex = random_col();
					b.rowNo = b.check_row(b.colIndex);
					if (b.rowNo < 7) {
						b.board[b.rowNo][b.colIndex] = name;
						flag = true;
					}
				} while (flag == false);
				b.print_board();
				b.canWrite.signalAll();
				b.lock.unlock();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	int random_col() {
		int b;
		b = random.nextInt(7);
		return b;
	}

}
