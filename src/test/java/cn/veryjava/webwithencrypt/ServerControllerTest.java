package cn.veryjava.webwithencrypt;

import cn.veryjava.encrypt.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述: TODO:
 * 包名: cn.veryjava.webwithencrypt.
 * 作者: barton.
 * 日期: 16-9-28.
 * 项目名称: webwithencrypt
 * 版本: 1.0
 * JDK: since 1.8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WebwithencryptApplication.class)
public class ServerControllerTest {
  @Autowired
  RestTemplate restTemplate;

  private static String BASE_URL = "http://localhost:8080/server/v1/";
  private String VERIFY_URL = "http://localhost:8080/api/gf_client/verify";

  @Test
  public void testTest() throws Exception {

    // 构建参数
    Map<String, String> map = new HashMap<>();
    map.put("name", "barton");
    map.put("accountId", "15");
    map.put("remark", "备注");

    String string = OrderByWordUtil.order(map);
    System.out.println("client queryString:" + string);

    // 使用私钥进行签名,之后将原文内容和签名一并发送给server
    map.put("sign", Base64Util.encodeBase64String(CertificateUtil.sign("/home/barton/testcer" +
     ".keystore", "testcer", "testcer", string.getBytes())));

    String response = restTemplate.postForObject(BASE_URL + "/test", map, String.class);
    System.out.println(response);

  }

}