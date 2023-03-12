package com.irostub.whaple.bot.weather_alert;

import com.irostub.whaple.bot.account.Account;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeatherAlert {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime scheduledTime;

    private String location;

    @ManyToOne
    private Account account;

    public static WeatherAlert create(LocalTime scheduledTime, String location, Account account) {
        WeatherAlert weatherAlert = new WeatherAlert();
        weatherAlert.scheduledTime = scheduledTime;
        weatherAlert.location = location;
        weatherAlert.account = account;
        return weatherAlert;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeatherAlert)) return false;
        WeatherAlert that = (WeatherAlert) o;
        return Objects.equals(id, that.id) && Objects.equals(scheduledTime, that.scheduledTime) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, scheduledTime, location);
    }
}
