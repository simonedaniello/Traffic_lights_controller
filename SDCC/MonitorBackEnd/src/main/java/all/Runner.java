package all;

import all.control.MonitorController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Runner {

	public static void main(String[] args) {
		new MonitorController();
		SpringApplication.run(Runner.class, args);
	}
}