package ar.edu.unq.weather_metrics_component.domain.port.in;

import ar.edu.unq.weather_metrics_component.domain.model.PeriodOfTimeTemperatureReport;
import ar.edu.unq.weather_metrics_component.domain.model.TemperatureReport;

public interface TemperatureMetricsUseCasePort {

    TemperatureReport getCurrentTemperatureReport();

    PeriodOfTimeTemperatureReport getLastDayAverageTemperatureReport();

    PeriodOfTimeTemperatureReport getLastWeekAverageTemperatureReport();
}
