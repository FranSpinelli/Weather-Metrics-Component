package ar.edu.unq.weather_metrics_component.application;

import ar.edu.unq.weather_metrics_component.domain.model.PeriodOfTimeTemperatureReport;
import ar.edu.unq.weather_metrics_component.domain.model.TemperatureReport;
import ar.edu.unq.weather_metrics_component.domain.port.in.TemperatureMetricsUseCasePort;
import ar.edu.unq.weather_metrics_component.domain.port.out.WeatherRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TemperatureMetricsUseCaseAdapter implements TemperatureMetricsUseCasePort {

    private final WeatherRepositoryPort weatherRepositoryPort;

    public TemperatureMetricsUseCaseAdapter(WeatherRepositoryPort weatherRepositoryPort) {
        this.weatherRepositoryPort = weatherRepositoryPort;
    }

    @Override
    public TemperatureReport getCurrentTemperatureReport() {
        return weatherRepositoryPort.getCurrentTemperatureReport();
    }

    @Override
    public PeriodOfTimeTemperatureReport getLastDayAverageTemperatureReport() {
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        LocalDateTime localDateTimeYesterday = localDateTimeNow.minusDays(1);

        List<TemperatureReport> temperatureReports = weatherRepositoryPort.getTemperatureReportsFromPeriod(localDateTimeYesterday, localDateTimeNow);

        Double averageTemperature = temperatureReports.stream()
                .mapToDouble(TemperatureReport::getTemperature)
                .average()
                .orElseThrow();

        return new PeriodOfTimeTemperatureReport(averageTemperature, temperatureReports.get(0).getCity(), localDateTimeYesterday, localDateTimeNow);
    }

    @Override
    public PeriodOfTimeTemperatureReport getLastWeekAverageTemperatureReport() {
        LocalDateTime localDateTimeNow = LocalDateTime.now();
        LocalDateTime localDateTimeLastWeek = localDateTimeNow.minusWeeks(1);

        List<TemperatureReport> temperatureReports = weatherRepositoryPort.getTemperatureReportsFromPeriod(localDateTimeLastWeek, localDateTimeNow);

        Double averageTemperature = temperatureReports.stream()
                .mapToDouble(TemperatureReport::getTemperature)
                .average()
                .orElseThrow();

        return new PeriodOfTimeTemperatureReport(averageTemperature, temperatureReports.get(0).getCity(), localDateTimeLastWeek, localDateTimeNow);
    }
}
