package ar.edu.unq.weather_metrics_component.domain.port.out;

import ar.edu.unq.weather_metrics_component.domain.model.TemperatureReport;

import java.time.LocalDateTime;
import java.util.List;

public interface WeatherRepositoryPort {

    TemperatureReport getCurrentTemperatureReport();

    List<TemperatureReport> getTemperatureReportsFromPeriod(LocalDateTime localDateTimeYesterday, LocalDateTime localDateTimeNow);
}
