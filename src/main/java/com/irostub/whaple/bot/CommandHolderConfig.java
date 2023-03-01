package com.irostub.whaple.bot;

import com.irostub.whaple.bot.fridaylunch.FridayLunchCommand;
import com.irostub.whaple.bot.hangang.HangangCommand;
import com.irostub.whaple.bot.ping.PingCommand;
import com.irostub.whaple.bot.restaurant.RestaurantCommand;
import com.irostub.whaple.bot.weather.WeatherCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class CommandHolderConfig {
    private final PingCommand pingCommand;
    private final HangangCommand hangangCommand;
    private final WeatherCommand weatherCommand;
    private final HelpCommand helpCommand;
    private final RestaurantCommand restaurantCommand;
    private final FridayLunchCommand fridayLunchCommand;

    @Bean
    public CommandHolder commandHolder(){
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.register(pingCommand);
        commandHolder.register(hangangCommand);
        commandHolder.register(weatherCommand);
        commandHolder.register(helpCommand);
        commandHolder.register(restaurantCommand);
        commandHolder.register(fridayLunchCommand);
        return commandHolder;
    }
}
