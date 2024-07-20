package me.Dimashi.Exchange_Rate.services;

import lombok.AllArgsConstructor;
import me.Dimashi.Exchange_Rate.model.ExchangeRecord;
import me.Dimashi.Exchange_Rate.repositories.ExchangeRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class ExchangeRateService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_URL = "https://v6.exchangerate-api.com/v6/9234df9394fd5ab8d3883dc0/latest/USD";

    @Autowired
    private ExchangeRecordRepository exchangeRecordRepository;

    public Map<String, Double> getExchangeRates() {
        Map<String, Object> response = restTemplate.getForObject(API_URL, Map.class);
        if (response != null && response.containsKey("conversion_rates")) {
            Map<String, Object> rawRates = (Map<String, Object>) response.get("conversion_rates");
            Map<String, Double> rates = new LinkedHashMap<>();
            rates.put("USD", 1.0); // Ensure USD is the first entry

            for (Map.Entry<String, Object> entry : rawRates.entrySet()) {
                if (!entry.getKey().equals("USD")) {
                    rates.put(entry.getKey(), ((Number) entry.getValue()).doubleValue());
                }
            }
            return rates;
        } else {
            throw new RuntimeException("Failed to fetch exchange rates from API");
        }
    }

    public double convert(double amount, String from, String to) {
        Map<String, Double> rates = getExchangeRates();
        if (!rates.containsKey(from) || !rates.containsKey(to)) {
            throw new IllegalArgumentException("Invalid currency code: " + from + " or " + to);
        }
        double rateFrom = rates.get(from);
        double rateTo = rates.get(to);
        double convertedAmount = amount * (rateTo / rateFrom);

        //лолгирование для откладки и результат на консоль
        System.out.println("Conversion details:");
        System.out.println("Amount: " + amount);
        System.out.println("From: " + from + " (" + rateFrom + ")");
        System.out.println("To: " + to + " (" + rateTo + ")");
        System.out.println("Converted amount: " + convertedAmount);

        if (convertedAmount == 0.0) {
            throw new RuntimeException("Converted amount is zero. Check rate calculations.");
        }

        saveExchangeRecord(amount, from, to, convertedAmount);
        return convertedAmount;
    }

    public ExchangeRecord saveExchangeRecord(double amount, String from, String to, double convertedAmount) {
        ExchangeRecord record = new ExchangeRecord();
        record.setAmount(amount);
        record.setFrom(from);
        record.setTo(to);
        record.setConvertedAmount(convertedAmount); //добавление конвертирование суммы
        record.setDate(LocalDateTime.now());

        //сохранение записи в хранилище
        return exchangeRecordRepository.save(record);
    }
}
