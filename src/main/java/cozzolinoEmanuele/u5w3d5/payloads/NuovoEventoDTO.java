package cozzolinoEmanuele.u5w3d5.payloads;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NuovoEventoDTO(
        @NotBlank(message = "Il titolo è un campo obbligatorio")
        String titolo,
        @NotBlank(message = "La descrizione è un campo obbligatorio")
        String descrizione,
        @NotNull(message = "La data è un campo obbligatorio")
        @Future(message = "La data dell'evento deve essere nel futuro")
        LocalDate data,
        @NotBlank(message = "Il luogo è un campo obbligatorio")
        String luogo,
        @Min(value = 1, message = "Il numero di posti deve essere almeno 1")
        int postiTotali) {
}
