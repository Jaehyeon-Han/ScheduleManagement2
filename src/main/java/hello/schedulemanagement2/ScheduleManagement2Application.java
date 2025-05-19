package hello.schedulemanagement2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ScheduleManagement2Application {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleManagement2Application.class, args);
    }

}
