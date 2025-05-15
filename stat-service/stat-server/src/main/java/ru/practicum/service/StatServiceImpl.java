package ru.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.StatDto;
import ru.practicum.mapper.StatMapper;
import ru.practicum.repository.StatRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mapper.HitMapper.toHitEntity;

@Slf4j
@Service
public class StatServiceImpl implements StatService{
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private StatRepository statRepository;

    @Override
    public void addHit(HitDto hitDto){
        statRepository.save(toHitEntity(hitDto));
    }

    @Override
    public List<StatDto> getStatistics(String start, String end, String[] uris, boolean unique){
        LocalDateTime startDataTime = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime endDataTime = LocalDateTime.parse(end, FORMATTER);

        if(uris.length == 0){
            if(unique) return statRepository.findAllUrisUnique(startDataTime, endDataTime).stream()
                    .map(StatMapper::toStatDto)
                    .collect(Collectors.toList());
            else return statRepository.findAllUris(startDataTime, endDataTime).stream()
                    .map(StatMapper::toStatDto)
                    .collect(Collectors.toList());
        } else {
            if(unique) return statRepository.findByUrisUnique(List.of(uris), startDataTime, endDataTime).stream()
                    .map(StatMapper::toStatDto)
                    .collect(Collectors.toList());
            else return statRepository.findByUris(List.of(uris), startDataTime, endDataTime).stream()
                    .map(StatMapper::toStatDto)
                    .collect(Collectors.toList());
        }
    }
}
