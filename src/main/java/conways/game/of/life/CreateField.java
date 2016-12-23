package conways.game.of.life;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CreateField implements JavaDelegate {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		int rows = get("rows", 40, execution);
		int cols = get("cols", 40, execution);
		double density = get("density", 0.5, execution);
		boolean[][] preparedField = execution.hasVariable("preparedField") ? (boolean[][]) execution.getVariable("preparedField") : null;
		boolean[][] field = preparedField != null ? preparedField : new boolean[rows][];
		List<Integer> cells = new ArrayList<Integer>();

		if (preparedField != null) {
			for (int i = 0; i < rows * cols; i++) {
				cells.add(i);
			}
		}
		else {
			Random random = new Random();
			for (int i = 0; i < rows; i++) {
				field[i] = new boolean[cols];
				for (int j = 0; j < cols; j++) {
					field[i][j] = random.nextDouble() < density;
					cells.add(i);
				}
			}
		}
		
		execution.setVariable("field", field);
		execution.setVariable("cells", cells);

		if (!execution.hasVariable("duration")) {
			execution.setVariable("duration", "PT1S");
		}
		
		LOG.info("rows, cols: {} {} density: {}", rows, cols, density);
	}

	@SuppressWarnings("unchecked")
	private <T> T get(String variableName, T defaultValue, DelegateExecution execution) {
		if (execution.hasVariable(variableName)) {
			return (T) execution.getVariable(variableName);
		}
		else {
			execution.setVariable(variableName, defaultValue);
			return defaultValue;
		}
	}
	
}
