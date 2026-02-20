package cozzolinoEmanuele.u5w3d5.controllers;

import cozzolinoEmanuele.u5w3d5.entities.Utente;
import cozzolinoEmanuele.u5w3d5.exceptions.ValidationException;
import cozzolinoEmanuele.u5w3d5.payloads.LoginDTO;
import cozzolinoEmanuele.u5w3d5.payloads.LoginResponseDTO;
import cozzolinoEmanuele.u5w3d5.payloads.NuovoUtenteDTO;
import cozzolinoEmanuele.u5w3d5.services.AuthService;
import cozzolinoEmanuele.u5w3d5.services.UtentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UtentiService utentiService;

    @Autowired
    public AuthController(AuthService authService, UtentiService utentiService) {
        this.authService = authService;
        this.utentiService = utentiService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginDTO body) {
        return new LoginResponseDTO(this.authService.checkCredentialsAndGenerateToken(body));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Utente register(@RequestBody @Validated NuovoUtenteDTO payload, BindingResult validationResult) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
        return this.utentiService.save(payload);
    }
}
