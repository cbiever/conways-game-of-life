package conways.game.of.life;

import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DisplayService {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	private Socket socket;
	private PrintWriter ledMatrix;

	public void send(String command) throws Exception {
		if (socket == null) {
			socket = new Socket("localhost", 8081);
			ledMatrix = new PrintWriter(socket.getOutputStream(), true);
			LOG.info("socket connected");
		}
		ledMatrix.println(command);
		ledMatrix.flush();
	}
	
}
