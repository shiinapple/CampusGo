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
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("id")
    private String userId;
    private String displayName;
    private String avatarUrl;
    private String phone;
    private String wechatId;
    private boolean verified;
}
