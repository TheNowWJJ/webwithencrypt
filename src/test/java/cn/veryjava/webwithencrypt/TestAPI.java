package cn.veryjava.webwithencrypt;

import cn.veryjava.encrypt.DigestUtil;
import cn.veryjava.encrypt.OrderByWordUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * 日期: 16-9-29.
 * 项目名称: webwithencrypt
 * 版本: 1.0
 * JDK: since 1.8
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = WebwithencryptApplication.class)
public class TestAPI {

  @Autowired
  RestTemplate restTemplate;

  private String VERIFY_URL = "http://localhost:8080/api/gf_client/verify";

  @Test
  public void testVerify() throws Exception {

    // 初始化参数
    String orderId = "20160929113347302";
    String time = String.valueOf(new Date().getTime());
    String account = "102400";// 精确到分,倒数第三位是 元

    Map<String, String> requestParams = new HashMap<>();
    requestParams.put("orderId", orderId);
    requestParams.put("time", time);
    requestParams.put("account", account);

    // 对参数进行字母序排列
    String requestSignString = OrderByWordUtil.order(requestParams);

    // 对参数进行HmacSHA1数字摘要
    String hmac = DigestUtil.hmacSHA1Base64(requestSignString);

    // 将数字摘要内容放置到请求参数中
    requestParams.put("hmac", hmac);

    // 发起请求并接收返回值
    Map response = restTemplate.postForObject(VERIFY_URL, requestParams, Map.class);
    System.out.println(new ObjectMapper().writeValueAsString(response));
    System.out.println(response.get("orderId"));
  }
}
