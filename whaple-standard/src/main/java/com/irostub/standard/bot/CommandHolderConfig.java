package com.irostub.standard.bot;

import com.irostub.standard.bot.checkio.CheckIoCommand;
import com.irostub.standard.bot.fridaylunch.FridayLunchCommand;
import com.irostub.standard.bot.gpt.GptCommand;
import com.irostub.standard.bot.hangang.HangangCommand;
import com.irostub.standard.bot.music.MusicCommand;
import com.irostub.standard.bot.rps.RpsCommand;
import com.irostub.standard.bot.vs.VsCommand;
import com.irostub.standard.bot.weather_alert.WeatherAlertCommand;
import com.irostub.standard.bot.ping.PingCommand;
import com.irostub.standard.bot.restaurant.RestaurantCommand;
import com.irostub.standard.bot.weather.WeatherCommand;
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
    private final CheckIoCommand checkIoCommand;
    private final WeatherAlertCommand weatherAlertCommand;
    private final RpsCommand rpsCommand;
    private final VsCommand vsCommand;
    private final GptCommand gptCommand;
    private final MusicCommand musicCommand;
    @Bean
    public CommandHolder commandHolder(){
        CommandHolder commandHolder = new CommandHolder();
        commandHolder.register(pingCommand);
        commandHolder.register(hangangCommand);
        commandHolder.register(weatherCommand);
        commandHolder.register(helpCommand);
        commandHolder.register(restaurantCommand);
        commandHolder.register(fridayLunchCommand);
        commandHolder.register(checkIoCommand);
        commandHolder.register(weatherAlertCommand);
        commandHolder.register(rpsCommand);
        commandHolder.register(vsCommand);
        commandHolder.register(gptCommand);
        commandHolder.register(musicCommand);
        return commandHolder;
    }
}
