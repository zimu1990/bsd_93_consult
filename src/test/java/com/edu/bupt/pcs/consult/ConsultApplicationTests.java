package com.edu.bupt.pcs.consult;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConsultApplicationTests {

    @Test
    public void contextLoads() {
        try {
            String path = ResourceUtils.getURL("classpath:").getPath();

            String property = System.getProperty("user.dir")+"";
            System.out.println(property);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
