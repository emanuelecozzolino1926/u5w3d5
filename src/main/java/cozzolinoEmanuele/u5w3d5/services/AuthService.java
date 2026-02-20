package cozzolinoEmanuele.u5w3d5.services;

import cozzolinoEmanuele.u5w3d5.entities.Utente;
import cozzolinoEmanuele.u5w3d5.exceptions.UnauthorizedException;
import cozzolinoEmanuele.u5w3d5.payloads.LoginDTO;
import cozzolinoEmanuele.u5w3d5.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UtentiService utentiService;
    private final JWTTools jwtTools;
    private final PasswordEncoder bcrypt;

    @Autowired
    public AuthService(UtentiService utentiService, JWTTools jwtTools, PasswordEncoder bcrypt) {
        this.utentiService = utentiService;
        this.jwtTools = jwtTools;
        this.bcrypt = bcrypt;
    }

    public String checkCredentialsAndGenerateToken(LoginDTO body) {
        Utente found = this.utentiService.findByEmail(body.email());

        if (bcrypt.matches(body.password(), found.getPassword())) {
            return jwtTools.generateToken(found);
        } else {
            throw new UnauthorizedException("Credenziali errate!");
        }
    }
}
