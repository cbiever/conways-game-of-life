package conways.game.of.life;

import org.camunda.bpm.dmn.engine.DmnDecisionResultEntries;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionResultImpl;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UpdateField implements JavaDelegate {

	private Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		DmnDecisionResultImpl decisions = (DmnDecisionResultImpl) execution.getVariable("decisionResult");
		boolean alive = false;
		if (decisions.size() > 1) {
			LOG.error("number of decisions > 1");
			throw new Exception("More than 1 DMN decision");
		}
		if (decisions.size() == 1) {
			DmnDecisionResultEntries decision = decisions.get(0);
			alive = decision.getSingleEntry();
		}

		int loopCounter = (Integer) execution.getVariable("loopCounter");
		int rows = (Integer) execution.getVariable("rows");
		int cols = (Integer) execution.getVariable("cols");

		if (loopCounter == 0) {
			boolean[][] field = createField(rows, cols);
			field[0][0] = alive;
			execution.setVariable("nextField", field);
		}
		else if (loopCounter == (rows * cols) - 1) {
			boolean[][] field = (boolean[][]) execution.getVariable("nextField");
			field[rows - 1][cols - 1] = alive;
			execution.setVariable("field", field);
		}
		else {
			boolean[][] field = (boolean[][]) execution.getVariable("nextField");
			int row = loopCounter / cols;
			int col = loopCounter % cols;
			field[row][col] = alive;
		}	
	}

	private boolean[][] createField(int rows, int cols) {
		boolean[][] field = new boolean[rows][];
		for (int i = 0; i < rows; i++) {
			field[i] = new boolean[cols];
		}
		return field;
	}
	
}
