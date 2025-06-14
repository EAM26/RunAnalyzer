package com.eamcode.RunAnalyzer.controller;

import com.eamcode.RunAnalyzer.dto.PhaseRequest;
import com.eamcode.RunAnalyzer.dto.PhaseResponse;
import com.eamcode.RunAnalyzer.model.Phase;
import com.eamcode.RunAnalyzer.model.PhaseCategory;
import com.eamcode.RunAnalyzer.service.PhaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/phases")
public class PhaseController {

    private final PhaseService phaseService;

    @PostMapping
    public ResponseEntity<PhaseResponse> createPhase(@RequestBody PhaseRequest request) throws IOException {
        return ResponseEntity.ok(phaseService.createPhase(request));
    }

    @PostMapping("/multi")
    public ResponseEntity<List<PhaseResponse>> addMultiPhase(@RequestParam int multiplier, @RequestParam Long reportId,
                                                     @RequestParam PhaseCategory category1, @RequestParam PhaseCategory category2,
                                                     @RequestParam String duration1, @RequestParam String duration2) throws IOException {
        return ResponseEntity.ok(phaseService.createMultiPhase(multiplier, reportId, category1, category2, duration1, duration2));
    }
}
