package cozzolinoEmanuele.u5w3d5.payloads;

import cozzolinoEmanuele.u5w3d5.entities.Ruolo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NuovoUtenteDTO(
        @NotBlank(message = "Il nome è un campo obbligatorio")
        @Size(min = 2, max = 30, message = "Il nome deve essere tra i 2 e i 30 caratteri")
        String nome,
        @NotBlank(message = "Il cognome è un campo obbligatorio")
        @Size(min = 2, max = 30, message = "Il cognome deve essere tra i 2 e i 30 caratteri")
        String cognome,
        @NotBlank(message = "L'email è obbligatoria")
        @Email(message = "L'indirizzo email inserito non è nel formato corretto!")
        String email,
        @NotBlank(message = "La password è obbligatoria")
        @Size(min = 4, message = "La password deve avere almeno 4 caratteri")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{4,}$", message = "La password deve contenere una maiuscola, una minuscola ed un numero")
        String password,
        Ruolo ruolo) {
}
