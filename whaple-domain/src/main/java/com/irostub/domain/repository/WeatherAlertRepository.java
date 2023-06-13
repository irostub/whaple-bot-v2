package com.irostub.domain.repository;

import com.irostub.domain.entity.Account;
import com.irostub.domain.entity.WeatherAlert;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface WeatherAlertRepository extends JpaRepository<WeatherAlert, Long> {
    @EntityGraph(attributePaths = "account")
    List<WeatherAlert> findByAccount(Account account);

    Optional<WeatherAlert> findByIdAndAccount(Long id, Account account);

    @EntityGraph(attributePaths = "account")
    List<WeatherAlert> findByScheduledTimeEquals(LocalTime localTime);
}
