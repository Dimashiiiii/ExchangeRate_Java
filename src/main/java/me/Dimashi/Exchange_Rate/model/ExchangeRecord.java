package me.Dimashi.Exchange_Rate.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exchange_record")
public class ExchangeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    @Column(name = "currency_from")
    private String from;

    @Column(name = "currency_to")
    private String to;

    private double convertedAmount;

    private LocalDateTime date;
}
