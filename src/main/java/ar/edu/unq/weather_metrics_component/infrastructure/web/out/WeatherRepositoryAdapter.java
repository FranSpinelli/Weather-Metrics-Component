package ar.edu.unq.weather_metrics_component.infrastructure.web.out;

import ar.edu.unq.weather_metrics_component.domain.model.TemperatureReport;
import ar.edu.unq.weather_metrics_component.domain.port.out.WeatherRepositoryPort;
import ar.edu.unq.weather_metrics_component.infrastructure.web.out.dto.PeriodOfTimeTemperatureReportDto;
import ar.edu.unq.weather_metrics_component.infrastructure.web.out.dto.TemperatureReportDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class WeatherRepositoryAdapter implements WeatherRepositoryPort {

    private static final String WEATHER_LOADER_COMPONENT_SCHEME = "http";
    private static final String WEATHER_LOADER_COMPONENT_HOST = "localhost";
    private static final Integer WEATHER_LOADER_COMPONENT_PORT = 8081;
    private static final String WEATHER_LOADER_COMPONENT_BASE_PATH = "weather-loader-component/temperature/report";
    private static final String WEATHER_LOADER_COMPONENT_CURRENT_TEMPERATURE_REPORT_PATH = "current";
    private static final String WEATHER_LOADER_COMPONENT_FILTER_BY_PATH = "filter-by";

    private final RestClient restClient;

    public WeatherRepositoryAdapter(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    @Cacheable(value = "currentTemperatureReportsCache", key = "T(java.time.LocalDateTime).now().truncatedTo(T(java.time.temporal.ChronoUnit).MINUTES).toString()")
    public TemperatureReport getCurrentTemperatureReport() {

        URI weatherLoaderComponentUri = UriComponentsBuilder.newInstance()
                .scheme(WEATHER_LOADER_COMPONENT_SCHEME)
                .host(WEATHER_LOADER_COMPONENT_HOST)
                .port(WEATHER_LOADER_COMPONENT_PORT)
                .pathSegment(WEATHER_LOADER_COMPONENT_BASE_PATH)
                .pathSegment(WEATHER_LOADER_COMPONENT_CURRENT_TEMPERATURE_REPORT_PATH)
                .build()
                .toUri();

        log.info("Weather Loader Component URI: {}.", UriSanitizerUtil.sanitize(weatherLoaderComponentUri));

        TemperatureReportDto temperatureReportDto =
                Objects.requireNonNull(restClient.get().uri(weatherLoaderComponentUri).retrieve().body(TemperatureReportDto.class));

        return generateTemperatureReportFrom(temperatureReportDto);
    }

    @Override
    @Cacheable(
            value = "PeriodTemperatureReportsCache",
            key = "#localDateTimeYesterday.truncatedTo(T(java.time.temporal.ChronoUnit).MINUTES).toString()" +
                    "+ '-' +" +
                    "#localDateTimeNow.truncatedTo(T(java.time.temporal.ChronoUnit).MINUTES).toString()"
    )
    public List<TemperatureReport> getTemperatureReportsFromPeriod(LocalDateTime localDateTimeYesterday, LocalDateTime localDateTimeNow) {

        URI weatherLoaderComponentUri = UriComponentsBuilder.newInstance()
                .scheme(WEATHER_LOADER_COMPONENT_SCHEME)
                .host(WEATHER_LOADER_COMPONENT_HOST)
                .port(WEATHER_LOADER_COMPONENT_PORT)
                .pathSegment(WEATHER_LOADER_COMPONENT_BASE_PATH)
                .pathSegment(WEATHER_LOADER_COMPONENT_FILTER_BY_PATH)
                .queryParam("from", localDateTimeYesterday)
                .queryParam("to", localDateTimeNow)
                .build()
                .toUri();

        log.info("Weather Loader Component URI: {}.", UriSanitizerUtil.sanitize(weatherLoaderComponentUri));

        PeriodOfTimeTemperatureReportDto periodOfTimeTemperatureReportDto =
                Objects.requireNonNull(restClient.get().uri(weatherLoaderComponentUri).retrieve().body(PeriodOfTimeTemperatureReportDto.class));

        return periodOfTimeTemperatureReportDto.getTemperatureReports().stream()
                .map(this::generateTemperatureReportFrom)
                .toList();
    }

    private TemperatureReport generateTemperatureReportFrom(TemperatureReportDto temperatureReportDto) {
        return new TemperatureReport(
                temperatureReportDto.getTemperature(),
                temperatureReportDto.getCity(),
                temperatureReportDto.getTimestamp()
        );
    }
}
