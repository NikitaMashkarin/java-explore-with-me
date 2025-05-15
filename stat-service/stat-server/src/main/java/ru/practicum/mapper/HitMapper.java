package ru.practicum.mapper;

import ru.practicum.HitDto;
import ru.practicum.model.Hit;

import java.sql.Timestamp;

public class HitMapper {

    public static HitDto toHitDto(Hit hit) {
        if (hit == null) {
            return null;
        }
        return HitDto.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp().toString())
                .build();
    }

    public static Hit toHitEntity(HitDto hitDto) {
        if (hitDto == null) {
            return null;
        }
        Hit hit = new Hit();
        hit.setApp(hitDto.getApp());
        hit.setUri(hitDto.getUri());
        hit.setIp(hitDto.getIp());

        try {
            hit.setTimestamp(Timestamp.valueOf(hitDto.getTimestamp()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid timestamp format: " + hitDto.getTimestamp());
        }

        return hit;
    }
}
