package rs.raf.demo.dto.responses;

import lombok.Data;

@Data
public class LoginResponse {
    private String jwt;

    public LoginResponse(String jwt) {
        this.jwt = jwt;
    }
}