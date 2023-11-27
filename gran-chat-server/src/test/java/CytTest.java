import com.shill.gran.common.GranCustomApplication;
import com.shill.gran.common.user.service.UserService;
import com.shill.gran.common.websocket.service.LoginService;
import javafx.application.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.junit.Assert;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GranCustomApplication.class)
public class CytTest {

    @Autowired
    private UserService userService;

    @Test
    public void test() {
        userService.modifyName(1L, "lllll");
        System.out.println("111");
    }
}