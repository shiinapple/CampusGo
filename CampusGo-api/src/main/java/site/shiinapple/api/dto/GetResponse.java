package site.shiinapple.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 获取用户信息响应 DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserDTO userDTO;

}
