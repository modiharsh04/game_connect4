package assignment;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Board {
	char[][] board = new char[6][7];
	protected final ReentrantLock lock = new ReentrantLock();
	protected final Condition canWrite = lock.newCondition();
	protected int ref_check = 0;
	int colIndex, rowNo;
	protected int check = 0;
	protected int is_board_full = 0;

	public Board() {
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				this.board[i][j] = '-';
			}
		}
	}
	
	protected void print_board() {
		int i, j;
		System.out.printf("\n");
		for (i = 0; i < 6; i++) {
			for (j = 0; j < 7; j++) {
				System.out.printf(" %c |", board[i][j]);
			}
			System.out.printf("\n");
		}
		System.out.printf("\n -------------------------- \n");
		for (i = 1; i < 8; i++) {
			System.out.printf(" %d |", i);
		}
		System.out.printf("\n -------------------------- \n");
	}

	int check_board() {
		int i, j;

		for (i = 0; i < 6; i++) {
			for (j = 0; j < 7; j++) {
				if (board[i][j] == '-') {
					return 8;
				}
			}
		}
		System.out.printf(".....no place for token sir!!!!!!\n");
		return 9;
	}
	
	int check_row(int col) {
		int i = 5;
		int b = 0;
		while (i >=0) {
			if (board[i][col] == '-') {
				return i;
			}
			i--;
		}
		b = check_board();
		return b;
	}

	int check_horizontal() {
		int i, j, countR, countY;
		for (i = 0; i < 6; i++) {
			countY = 0;
			countR = 0;
			for (j = 0; j < 7; j++) {
				if (board[i][j] == 'R') {
					countY = 0;
					countR++;
					if (countR == 4) {
						System.out
								.printf("\n R wins horizontal...congratulations!!!! \n");
						return 2;
					}
				} else if (board[i][j] == 'Y') {
					countR = 0;
					countY++;
					if (countY == 4) {
						System.out
								.printf("\n Y wins horizontal...congratulations!!!! \n");
						return 2;
					}
				} else {
					countR = 0;
					countY = 0;
				}
			}
		}
		return (0);
	}

	int check_diagonal() {
		int j = 0;
		if(this.check_horizontal()==2){
			return 2;
		}
		for (int i = 5; i > 2; i--) {
			for (j = 0; j < 4; j++) {
				if ((board[i][j] == 'R' && board[i - 2][j + 2] == 'R'
						&& board[i - 3][j + 3] == 'R' && board[i - 1][j + 1] == 'R')) {
					System.out
							.printf("\n R wins diagonally...congratulations!!!! \n");
					return 2;
				}
				if ((board[i][j] == 'Y' && board[i - 2][j + 2] == 'Y'
						&& board[i - 3][j + 3] == 'Y' && board[i - 1][j + 1] == 'Y')) {
					System.out
							.printf("\n Y wins diagonally...congratulations!!!! \n");
					return 2;
				}
			}
		}
		for (int i = 5; i > 2; i--) {
			for (j = 6; j > 3; j--) {
				if ((board[i][j] == 'R' && board[i - 2][j - 2] == 'R'
						&& board[i - 3][j - 3] == 'R' && board[i - 1][j - 1] == 'R')) {
					System.out
							.printf("\n R wins diagonally...congratulations!!!! \n");
					return 2;
				}
				if ((board[i][j] == 'Y' && board[i - 2][j - 2] == 'Y'
						&& board[i - 3][j - 3] == 'Y' && board[i - 1][j - 1] == 'Y')) {
					System.out
							.printf("\n Y wins diagonally...congratulations!!!! \n");
					return 2;
				}
			}
		}
		return 0;
	}

	public void refreeDo() {
		for (;;) {
			try {
				Thread.sleep(1000);
				lock.lock();
				while (ref_check == 0) {
					canWrite.await();
				}
				System.out.printf("\n.....refree replies....\n");
				if (check_board() == 8) {
					check = this.check_diagonal();
					if (check == 2) {
						System.out.printf("\n....Winner Found.... \n");
						is_board_full = 1;
						ref_check = 0;
						System.out.printf(" \n ....Game Over... \n");
						canWrite.signalAll();
						lock.unlock();
						return;
					}
					System.out.printf("\n.....no win found...go players :)\n");
				} else if (check_board() == 9) {
					ref_check = 0;
					is_board_full = 1;
					System.out
							.printf("....The Game is Over as tie...sorry :) \n");
					canWrite.signalAll();
					lock.unlock();
					return;
				}
				ref_check = 0;
				canWrite.signalAll();
				lock.unlock();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
