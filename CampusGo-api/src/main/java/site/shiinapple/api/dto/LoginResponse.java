package site.shiinapple.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    
    @JsonProperty("token")
    private String token;
    
    @JsonProperty("user")
    private UserDTO user;
}
