package conways.game.of.life;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Display implements JavaDelegate {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private DisplayService displayService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		LOG.info("");
		boolean[][] field = (boolean[][]) execution.getVariable("field");
		int rows = (Integer) execution.getVariable("rows");
		int cols = (Integer) execution.getVariable("cols");
		String command = "";
		for (int i = 0; i < rows; i++) {
			String line = "";
			for (int j = 0; j < cols; j++) {
				line += (field[i][j] ? "*" : " ");
				if (i < 8 && j < 8) {
					command += (field[i][j] ? "*" : " ");
				}
			}
			LOG.info(line);
		}
		if (displayService == null) {
			LOG.info("Didn't get Spring working as intended");
			displayService = new DisplayService();
		}
		displayService.send(command);
	}

}
