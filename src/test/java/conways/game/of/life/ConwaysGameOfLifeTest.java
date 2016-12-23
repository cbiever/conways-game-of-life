package conways.game.of.life;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.processEngine;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.init;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.logging.LogFactory;
import org.camunda.bpm.consulting.process_test_coverage.ProcessTestCoverage;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test case starting an in-memory database-backed Process Engine.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:camunda.cfg.xml")
public class ConwaysGameOfLifeTest {

	@Rule
	@Autowired
	public ProcessEngineRule rule;

	private static final String PROCESS_DEFINITION_KEY = "conways_game_of_life";

	static {
		LogFactory.useSlf4jLogging(); // MyBatis
	}

	@Before
	public void setup() {
		init(rule.getProcessEngine());
	}

	/**
	 * Just tests if the process definition is deployable.
	 */
	@Test
	@Deployment(resources = { "ConwaysGameOfLife.bpmn", "ConwaysGameOfLife.dmn" })
	public void testParsingAndDeployment() {
		// nothing is done here, as we just want to check for exceptions during
		// deployment
	}

	@Test
	@Deployment(resources = { "ConwaysGameOfLife.bpmn", "ConwaysGameOfLife.dmn" })
	public void testConwaysGameOfLife() {
		int fieldSize = 20;
		boolean[][] preparedField = new boolean[fieldSize][];
		
		for (int i = 0; i < fieldSize; i++) {
			preparedField[i] = new boolean[fieldSize];
		}
		preparedField[0][1] = true;
		preparedField[1][2] = true;
		preparedField[2][0] = true;
		preparedField[2][1] = true;
		preparedField[2][2] = true;

		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("rows", fieldSize);
		variables.put("cols", fieldSize);
//		variables.put("preparedField", preparedField);
		ProcessInstance processInstance = processEngine().getRuntimeService()
				.startProcessInstanceByKey(PROCESS_DEFINITION_KEY, variables);

		for (int i = 0; i < fieldSize; i++) {
			assertThat(processInstance).isStarted().isWaitingAt("Timer");

			Job job = rule.getManagementService().createJobQuery().singleResult();
			rule.getManagementService().executeJob(job.getId());
		}

//		ProcessTestCoverage.calculate(processInstance, rule.getProcessEngine());
	}

	@After
	public void calculateCoverageForAllTests() throws Exception {
		ProcessTestCoverage.calculate(rule.getProcessEngine());
	}

}
