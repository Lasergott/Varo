package net.howtobedwars.varo.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class Config {

    @Getter
    private final String fileName;
}
