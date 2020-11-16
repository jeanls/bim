package br.com.bim.controllers;

import br.com.bim.services.HistoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping("/history")
@RestController
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping(path = "/{code}")
    public ResponseEntity<Object> getHistory(@PathVariable("code") final String code) {
        return ResponseEntity.ok(historyService.getHistory(code));
    }
}
