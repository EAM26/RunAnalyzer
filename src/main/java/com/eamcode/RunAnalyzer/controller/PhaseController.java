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

    @PostMapping("/multi")
    public ResponseEntity<Void> addMultiPhase(@RequestParam int multiplier, @RequestParam Long reportId,
                                                @RequestParam String name1, @RequestParam String name2,
                                                @RequestParam String duration1, @RequestParam String duration2) {
        phaseService.createMultiPhase(multiplier, reportId, name1, name2, duration1, duration2);
        return ResponseEntity.ok().build();
    }


}
