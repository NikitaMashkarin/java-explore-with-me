package ru.practicum.service;

import ru.practicum.HitDto;
import ru.practicum.StatDto;

import java.util.List;

public interface StatService {
    void addHit(HitDto hitDto);

    List<StatDto> getStatistics(String start, String end, String[] uris, boolean unique);
}
