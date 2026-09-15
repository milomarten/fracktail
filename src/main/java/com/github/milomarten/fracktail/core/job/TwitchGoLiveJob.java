package com.github.milomarten.fracktail.core.job;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.events.ChannelGoLiveEvent;
import discord4j.common.util.Snowflake;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@ConditionalOnProperty(value = "discord.twitch-go-live.enabled", havingValue = "true")
public class TwitchGoLiveJob extends AbstractAnnouncementJob {
    private final TwitchClient twitchClient;
    private final String ownerId;

    protected TwitchGoLiveJob(
            TwitchClient twitchClient,
            @Value("${discord.twitch-go-live.announcementChannelId}") Snowflake announcementChannelId,
            @Value("${twitch.ownerId}") String ownerId
    ) {
        super(announcementChannelId);
        this.twitchClient = twitchClient;
        this.ownerId = ownerId;
    }

    @PostConstruct
    public void setup() {
        twitchClient.getClientHelper()
                .enableStreamEventListener(ownerId);

        var handler = twitchClient.getEventManager()
                .getEventHandler(SimpleEventHandler.class);
        handler.onEvent(ChannelGoLiveEvent.class, this::handleEvent);
    }

    private void handleEvent(ChannelGoLiveEvent event) {
        var formatted = """
                %s %s is now live at https://www.twitch.tv/%s !
                `%s`
                PLAYING: %s
                """
                .formatted(
                        "<@&535277349340708864>",
                        event.getStream().getUserName(),
                        event.getStream().getUserLogin(),
                        event.getStream().getTitle(),
                        event.getStream().getGameName()
                );
        sendAnnouncement(formatted);
    }
}
