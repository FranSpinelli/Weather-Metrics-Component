package ar.edu.unq.weather_metrics_component.infrastructure.web.in;

import ar.edu.unq.weather_metrics_component.domain.model.PeriodOfTimeTemperatureReport;
import ar.edu.unq.weather_metrics_component.domain.model.TemperatureReport;
import ar.edu.unq.weather_metrics_component.domain.port.in.TemperatureMetricsUseCasePort;
import ar.edu.unq.weather_metrics_component.infrastructure.web.in.dto.CurrentTemperatureResponseDto;
import ar.edu.unq.weather_metrics_component.infrastructure.web.in.dto.PeriodOfTimeTemperatureReportResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
public class WeatherMetricsController {

    private final TemperatureMetricsUseCasePort temperatureMetricsUseCasePort;

    public WeatherMetricsController(TemperatureMetricsUseCasePort temperatureMetricsUseCasePort) {
        this.temperatureMetricsUseCasePort = temperatureMetricsUseCasePort;
    }

    @GetMapping("/current/temperature")
    public ResponseEntity<CurrentTemperatureResponseDto> getCurrentTemperature() {
        TemperatureReport currentTemperatureReport = temperatureMetricsUseCasePort.getCurrentTemperatureReport();
        return ResponseEntity.ok(generateCurrentTemperatureResponseDtoFrom(currentTemperatureReport));
    }

    @GetMapping("/last-day/temperature/average")
    public ResponseEntity<PeriodOfTimeTemperatureReportResponseDto> getLastDayAverageTemperature() {
        PeriodOfTimeTemperatureReport currentTemperatureReport = temperatureMetricsUseCasePort.getLastDayAverageTemperatureReport();
        return ResponseEntity.ok(generatePeriodOfTimeTemperatureResponseDtoFrom(currentTemperatureReport));
    }

    @GetMapping("/last-week/temperature/average")
    public ResponseEntity<PeriodOfTimeTemperatureReportResponseDto> getLastWeekAverageTemperature() {
        PeriodOfTimeTemperatureReport currentTemperatureReport = temperatureMetricsUseCasePort.getLastWeekAverageTemperatureReport();
        return ResponseEntity.ok(generatePeriodOfTimeTemperatureResponseDtoFrom(currentTemperatureReport));
    }

    private CurrentTemperatureResponseDto generateCurrentTemperatureResponseDtoFrom(TemperatureReport currentTemperatureReport) {
        return new CurrentTemperatureResponseDto(
                currentTemperatureReport.getTemperature(),
                currentTemperatureReport.getCity(),
                currentTemperatureReport.getTimestamp()
        );
    }

    private PeriodOfTimeTemperatureReportResponseDto generatePeriodOfTimeTemperatureResponseDtoFrom(PeriodOfTimeTemperatureReport currentTemperatureReport) {
        return new PeriodOfTimeTemperatureReportResponseDto(
                deleteExtraDecimalsFromDouble(currentTemperatureReport.getAverageTemperature()),
                currentTemperatureReport.getCity(),
                currentTemperatureReport.getFrom(),
                currentTemperatureReport.getTo()
        );
    }

    private Double deleteExtraDecimalsFromDouble(Double averageTemperature) {
        return BigDecimal.valueOf(averageTemperature)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
