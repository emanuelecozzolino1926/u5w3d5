package cozzolinoEmanuele.u5w3d5.controllers;

import cozzolinoEmanuele.u5w3d5.entities.Evento;
import cozzolinoEmanuele.u5w3d5.entities.Utente;
import cozzolinoEmanuele.u5w3d5.exceptions.ValidationException;
import cozzolinoEmanuele.u5w3d5.payloads.NuovoEventoDTO;
import cozzolinoEmanuele.u5w3d5.services.EventiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/eventi")
public class EventiController {

    private final EventiService eventiService;

    @Autowired
    public EventiController(EventiService eventiService) {
        this.eventiService = eventiService;
    }

    @GetMapping
    public Page<Evento> findAll(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(defaultValue = "data") String orderBy) {
        return this.eventiService.findAll(page, size, orderBy);
    }

    @GetMapping("/{eventoId}")
    public Evento findById(@PathVariable UUID eventoId) {
        return this.eventiService.findById(eventoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Evento save(@RequestBody @Validated NuovoEventoDTO payload,
                       BindingResult validationResult,
                       @AuthenticationPrincipal Utente currentUser) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        return this.eventiService.save(payload, currentUser);
    }

    @PutMapping("/{eventoId}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public Evento findByIdAndUpdate(@PathVariable UUID eventoId,
                                    @RequestBody @Validated NuovoEventoDTO payload,
                                    BindingResult validationResult,
                                    @AuthenticationPrincipal Utente currentUser) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        return this.eventiService.findByIdAndUpdate(eventoId, payload, currentUser);
    }

    @DeleteMapping("/{eventoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public void findByIdAndDelete(@PathVariable UUID eventoId,
                                  @AuthenticationPrincipal Utente currentUser) {
        this.eventiService.findByIdAndDelete(eventoId, currentUser);
    }
}
