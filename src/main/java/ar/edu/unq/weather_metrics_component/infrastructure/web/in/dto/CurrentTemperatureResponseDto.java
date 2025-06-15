package ar.edu.unq.weather_metrics_component.infrastructure.web.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CurrentTemperatureResponseDto {

    @JsonProperty("current_temperature")
    private Double currentTemperature;

    private String city;

    private LocalDateTime timestamp;
}
