package org.airo.asmp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

import org.airo.asmp.model.entity.Alumni;

@SpringBootTest
class AlumniServiceManagementPlatformApplicationTests {

    @Test
    void testAlumniApi() throws IOException {
        // 定义 JSON 文件路径
        File jsonFile = new File("Backend/../TestData/creatorId-test-data.json");
        ObjectMapper mapper = new ObjectMapper();
        Alumni alumni = mapper.readValue(jsonFile, Alumni.class);
        System.out.println(alumni);
    }

    @Test
    void contextLoads() {
    }

}