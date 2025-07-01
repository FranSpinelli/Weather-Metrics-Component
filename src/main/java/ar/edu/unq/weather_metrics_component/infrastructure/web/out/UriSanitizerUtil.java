package ar.edu.unq.weather_metrics_component.infrastructure.web.out;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class UriSanitizerUtil {

    public static String sanitize(URI uri){
        if (uri == null || uri.getQuery() == null) return uri != null ? uri.toString() : null;

        String sanitizedQuery = Arrays.stream(uri.getQuery().split("&"))
                .map(param -> {
                    String[] parts = param.split("=", 2);
                    String key = parts[0];
                    String value = parts.length > 1 ? parts[1] : "";
                    if (key.equalsIgnoreCase("apikey") || key.equalsIgnoreCase("appid") || key.equalsIgnoreCase("token")) {
                        return key + "=****";
                    }
                    return key + "=" + value;
                })
                .collect(Collectors.joining("&"));

        try {
            return new URI(
                    uri.getScheme(),
                    uri.getAuthority(),
                    uri.getPath(),
                    sanitizedQuery,
                    uri.getFragment()
            ).toString();
        } catch (URISyntaxException e) {
            return uri.toString(); // fallback
        }
    }
}
