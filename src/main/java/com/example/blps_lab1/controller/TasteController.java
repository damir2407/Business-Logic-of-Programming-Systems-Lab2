package com.example.blps_lab1.controller;

import com.example.blps_lab1.dto.request.AddTasteRequest;
import com.example.blps_lab1.dto.request.UpdateTasteRequest;
import com.example.blps_lab1.model.basic.Tastes;
import com.example.blps_lab1.service.TastesService;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taste")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TasteController {
    private final TastesService tastesService;

    public TasteController(TastesService tastesService) {
        this.tastesService = tastesService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Tastes addTaste(@Valid @RequestBody AddTasteRequest addTasteRequest) {
        return tastesService.saveTaste(addTasteRequest);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTaste(@RequestParam("tasteId") Long tasteId) {
        tastesService.deleteTaste(tasteId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping()
    public Tastes updateTaste(@RequestParam("tasteId") Long tasteId,
                              @Valid @RequestBody UpdateTasteRequest updateTasteRequest) {

        return tastesService.updateTaste(tasteId, updateTasteRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping()
    public List<Tastes> getAllTastes(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "size", defaultValue = "10") int size) {
        return tastesService.getAllTaste(page, size).getContent();
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("{tasteId}")
    public Tastes getTaste(@PathVariable Long tasteId) {
        return tastesService.getTaste(tasteId);
    }
}
