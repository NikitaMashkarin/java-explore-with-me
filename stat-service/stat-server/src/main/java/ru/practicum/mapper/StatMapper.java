package ru.practicum.mapper;

import ru.practicum.model.Stat;
import ru.practicum.StatDto;

public class StatMapper {

    public static StatDto toStatDto(Stat stat) {
        if (stat == null) {
            return null;
        }

        return new StatDto(
                stat.getApp(),
                stat.getUri(),
                stat.getHits()
        );
    }

    public static Stat toStatEntity(StatDto statDto) {
        if (statDto == null) {
            return null;
        }

        return new Stat(
                statDto.getApp(),
                statDto.getUri(),
                statDto.getHits()
        );
    }
}
