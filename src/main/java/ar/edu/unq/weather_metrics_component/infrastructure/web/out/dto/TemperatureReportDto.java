package ar.edu.unq.weather_metrics_component.infrastructure.web.out.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TemperatureReportDto {

    private Double temperature;

    @JsonProperty("city_name")
    private String city;

    private LocalDateTime timestamp;
}
