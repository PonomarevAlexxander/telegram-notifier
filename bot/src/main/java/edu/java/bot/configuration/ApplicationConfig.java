package edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import edu.java.bot.command.Command;
import edu.java.bot.command.HelpCmd;
import edu.java.bot.command.ListCmd;
import edu.java.bot.command.StartCmd;
import edu.java.bot.command.TrackCmd;
import edu.java.bot.command.UntrackCmd;
import jakarta.validation.constraints.NotEmpty;
import java.util.LinkedList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
    String telegramToken
) {
    @Bean
    @Scope("singleton")
    TelegramBot telegramBot() {
        return new TelegramBot(telegramToken);
    }

    @Bean
    List<Command> commands() {
        List<Command> commands = new LinkedList<>();
        commands.add(new StartCmd(null));
        commands.add(new HelpCmd(commands));
        commands.add(new TrackCmd(null));
        commands.add(new UntrackCmd(null));
        commands.add(new ListCmd(null));
        return commands;
    }
}
