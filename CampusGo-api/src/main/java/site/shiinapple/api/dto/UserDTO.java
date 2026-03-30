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
    
    @JsonProperty("displayName")
    private String displayName;
    
    @JsonProperty("avatarUrl")
    private String avatarUrl;
    
    @JsonProperty("phone")
    private String phone;
    
    @JsonProperty("wechatId")
    private String wechatId;
    
    @JsonProperty("verified")
    private boolean verified;

    @JsonProperty("monthTaken")
    private Integer monthTaken;

    @JsonProperty("totalTaken")
    private Integer totalTaken;

    @JsonProperty("onTimeRate")
    private String onTimeRate;
}
