package ar.edu.unq.weather_metrics_component;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class WeatherMetricsComponentApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherMetricsComponentApplication.class, args);
    }

}
