package com.eamcode.RunAnalyzer.controller;

import com.eamcode.RunAnalyzer.dto.PhaseRequest;
import com.eamcode.RunAnalyzer.model.PhaseCategory;
import com.eamcode.RunAnalyzer.service.PhaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/phases")
public class PhaseController {

    private final PhaseService phaseService;

    @PostMapping
    public ResponseEntity<?> createPhase(@RequestBody PhaseRequest request) throws IOException {
        try {
            return ResponseEntity.ok(phaseService.createPhase(request));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/multi")
    public ResponseEntity<?> addMultiPhase(@RequestParam int multiplier, @RequestParam Long reportId,
                                           @RequestParam PhaseCategory category1, @RequestParam PhaseCategory category2,
                                           @RequestParam String duration1, @RequestParam String duration2) throws IOException {

        try {
            return ResponseEntity.ok(phaseService.createMultiPhase(multiplier, reportId, category1, category2, duration1, duration2));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


}
