package site.shiinapple.test;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import site.shiinapple.api.dto.LoginRequest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test_login() throws Exception {
        // 1. 获取微信 code
        // 这里是从小程序前端 wx.login() 获取的临时 code。每次有效期 5 分钟且只能使用一次。
        // 如果你还没有真实的小程序环境，可以填入 "mock_user_001" 进行本地测试（后端支持 mock 模式）。
        
        
        String code = "mock_user_001"; 

        log.info("开始模拟调用登录接口, code: {}", code);

        LoginRequest request = LoginRequest.builder()
                .code(code)
                .build();

        // 2. 模拟 POST 请求调用 /v1/auth/login
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/login")
                        .content(JSON.toJSONString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        // 3. 打印结果
        log.info("测试结果：{}", response);
    }
}
