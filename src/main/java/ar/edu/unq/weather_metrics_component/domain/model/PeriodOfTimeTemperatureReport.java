package ar.edu.unq.weather_metrics_component.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PeriodOfTimeTemperatureReport {

    private Double averageTemperature;

    private String city;

    private LocalDateTime from;

    private LocalDateTime to;
}
