package site.shiinapple.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private String token;
    private UserDTO user;
}
