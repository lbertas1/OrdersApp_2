package bartos.lukasz.ui.controller;

import bartos.lukasz.persistence.model.ExchangeRate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ReadExchangeRate {

    private static final String NBP_PATH = "http://api.nbp.pl/api/exchangerates/rates/c/usd/2016-04-04/?format=json";

    private HttpRequest createHttpRequest() {
        try {
            return HttpRequest.newBuilder()
                    .uri(new URI(NBP_PATH))
                    .version(HttpClient.Version.HTTP_2)
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    private CompletableFuture<HttpResponse<String>> invokeAsyncGet() {
        return HttpClient
                .newBuilder()
                .proxy(ProxySelector.getDefault())
                .build()
                .sendAsync(createHttpRequest(), HttpResponse.BodyHandlers.ofString());
    }

    public ExchangeRate getLastReadingExchangeRate(ExchangeRate exchangeRate) {
        if (exchangeRate.getCurrency() == null) {
            exchangeRate = getExchangeRateFromNbp();
            setNextInvokeOfHttpRequest();
        } else {
            setNextInvokeOfHttpRequest();
        }
        return exchangeRate;
    }

    private ExchangeRate getExchangeRateFromNbp() {
        CompletableFuture<HttpResponse<String>> httpResponseCompletableFuture = invokeAsyncGet();
        AtomicReference<ExchangeRate> exchangeRateAtomicReference = new AtomicReference<>();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        httpResponseCompletableFuture.thenAccept(
                stringHttpResponse -> exchangeRateAtomicReference.set(gson.fromJson(stringHttpResponse.body(), ExchangeRate.class)));

        return exchangeRateAtomicReference.get();
    }

    public void setNextInvokeOfHttpRequest() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleWithFixedDelay(this::getExchangeRateFromNbp, 0, calculateRemainingTime(), TimeUnit.SECONDS);
    }

    private long calculateRemainingTime() {
        LocalDateTime nextHttpRequest = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(8, 0));
        return ChronoUnit.SECONDS.between(LocalDateTime.now(), nextHttpRequest);
    }
}
