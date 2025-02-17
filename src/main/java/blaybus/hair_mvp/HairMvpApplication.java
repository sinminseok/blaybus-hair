package blaybus.hair_mvp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HairMvpApplication {
	public static void main(String[] args) {
		SpringApplication.run(HairMvpApplication.class, args);
	}

}
