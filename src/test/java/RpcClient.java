import com.learn.rpc.client.RpcProxy;
import com.learn.rpc.test.HelloService;
import com.learn.rpc.test.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-client.xml")
public class RpcClient {

    private final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    @Autowired
    private RpcProxy rpcProxy;


    @Test
    public void helloTest() {

        User user = new User();
        user.setAge(10);
        user.setId(UUID.randomUUID().toString());
        user.setName("张三");
        user.setAddress("南京市玄武区");
        HelloService helloService = rpcProxy.create(HelloService.class);
        String result = helloService.hello("World");
        logger.info("result = {}", result);

        User user1 = helloService.saveAndReturUser(user);
        logger.info("result user1 = {}", user1);

        HelloService helloService2 = rpcProxy.create(HelloService.class, "helloServiceI2mpl");
        String result2 = helloService2.hello("tom");
        logger.info("result = {}", result2);


        User user2 = new User();
        user2.setAge(20);
        user2.setId(UUID.randomUUID().toString());
        user2.setName("李四");
        user2.setAddress("南京市鼓楼区");
        User user3 = helloService2.saveAndReturUser(user2);
        logger.info("result user3 = {}", user3);
    }
}