package com.github.milomarten.fracktail.core.twitch;

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "twitch", name = "secret")
public class TwitchBootstrap {
    @Bean
    public TwitchClient twitchClient(
            @Value("${twitch.clientId}") String clientId,
            @Value("${twitch.secret}") String clientSecret) {
        return TwitchClientBuilder.builder()
                .withClientId(clientId)
                .withClientSecret(clientSecret)
                .withEnableEventSocket(true)
                .withEnableHelix(true)
                .build();
    }
}
