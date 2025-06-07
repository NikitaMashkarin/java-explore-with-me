package ru.practicum.compilations.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.dto.UpdateCompilationRequestDto;
import ru.practicum.compilations.service.CompilationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CompilationsController {
    private CompilationService service;

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET /compilations pinned {}, from {}, size {}", pinned, from, size);
        return service.getCompilations(pinned, from, size);
    }

    @GetMapping("/compilations/{comId}")
    public CompilationDto getCompilationById(@PathVariable @Positive Long comId) {
        log.info("GET /compilations/{}", comId);
        return service.getCompilationById(comId);
    }

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("POST /admin/compilations");
        return service.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @Positive Long comId) {
        log.info("DELETE /admin/compilations/{}", comId);
        service.deleteCompilation(comId);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable @Positive Long comId,
                                            @RequestBody UpdateCompilationRequestDto updateCompilationRequestDto) {
        log.info("PATCH /admin/compilations/{}", comId);
        return service.updateCompilation(comId, updateCompilationRequestDto);
    }
}
