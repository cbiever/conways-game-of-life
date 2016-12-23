package conways.game.of.life;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class CalculateNumberOfLivingNeighboors implements JavaDelegate {
	
	@Override
	public void execute(DelegateExecution execution) {
		boolean[][] field = (boolean[][]) execution.getVariable("field");
		int rows = (Integer) execution.getVariable("rows");
		int cols = (Integer) execution.getVariable("cols");
		int loopCounter =  (Integer) execution.getVariable("loopCounter");

		int row = loopCounter / cols;
		int col = loopCounter % cols;
		
		int numberOfLivingNeighboors = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if ((i != 0 || j != 0) && alive(row + i, col + j, rows, cols, field)) {
					numberOfLivingNeighboors++;
				}
			}
		}
		
		execution.setVariableLocal("numberOfLivingNeighboors", numberOfLivingNeighboors);
		execution.setVariableLocal("alive", field[row][col]);
	}

	private boolean alive(int row, int col, int rows, int cols, boolean[][] field) {
		int r = (row + rows) % rows;
		int c = (col + cols) % cols;
		return field[r][c];
	}
	
}
