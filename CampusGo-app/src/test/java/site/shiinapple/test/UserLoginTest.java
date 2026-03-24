package site.shiinapple.test;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import site.shiinapple.domain.user.model.valobj.UserVO;
import site.shiinapple.domain.user.service.IUserService;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserLoginTest {

    @Autowired
    private IUserService userService;


}
