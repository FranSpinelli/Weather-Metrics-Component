package ar.edu.unq.weather_metrics_component.infrastructure.web.out.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class PeriodOfTimeTemperatureReportDto {

    @JsonProperty("temperature_reports")
    private List<TemperatureReportDto> temperatureReports;
}
