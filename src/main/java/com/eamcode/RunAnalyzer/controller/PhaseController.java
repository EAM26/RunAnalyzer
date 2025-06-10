package com.eamcode.RunAnalyzer.controller;

import com.eamcode.RunAnalyzer.dto.PhaseRequest;
import com.eamcode.RunAnalyzer.model.Phase;
import com.eamcode.RunAnalyzer.service.PhaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/phases")
public class PhaseController {

    private final PhaseService phaseService;

    @PostMapping
    public ResponseEntity<Phase> createPhase(@RequestBody PhaseRequest request) {
        return ResponseEntity.ok(phaseService.createPhase(request));
    }

}
