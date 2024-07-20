package me.Dimashi.Exchange_Rate.repositories;

import me.Dimashi.Exchange_Rate.model.ExchangeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRecordRepository extends JpaRepository<ExchangeRecord, Long> {
}

