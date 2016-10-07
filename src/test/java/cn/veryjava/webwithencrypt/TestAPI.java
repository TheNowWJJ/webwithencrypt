package cn.veryjava.webwithencrypt;

import cn.veryjava.encrypt.DigestUtil;
import cn.veryjava.encrypt.OrderByWordUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

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

  private String BASE_URL = "http://localhost:8080/api/gf_client";

  @Test
  public void testGetOrder() throws Exception {

    // 初始化参数
    String orderNo = "123";
    // 这种情况下会抛出400异常
    //String getOrderUrl = BASE_URL + "/getOrder";
    // 这种情况下返回值result_code=3 result_type订单号为空
    String getOrderUrl = BASE_URL + "/order/" + orderNo;

    // 发起请求并接收返回值
    Map response = restTemplate.getForObject(getOrderUrl, Map.class);

    // result_code = 1 为成功
    if (response.get("result_code").equals("1")) {

      String hmac = (String) response.get("hmac");

      response.remove("hmac");

      // 验证hmac
      if (verifyHmac(response, hmac)) {
        System.out.println("验证成功");
        System.out.println(response.get("orderNo"));
      } else {
        System.out.println("验证失败");
      }
    } else {
      System.out.println(response.get("result_type"));
    }
  }

  @Test
  public void testNotify() {
    // 初始化参数
    String orderNo = "20160929190002665";
    String amount = "2048.90";
    String payNo = "12345567890";
    String bankCardNo = "62222222222";
    String bankCardName = "广发银行";

    Map<String, String> request = new HashMap<>();
    request.put("orderNo", orderNo);
    request.put("amount", amount);
    request.put("payNo", payNo);
    request.put("bankCardNo", bankCardNo);
    request.put("bankCardName", bankCardName);
    String hmac = getHmac(request);
    request.put("hmac", hmac);

    String notifyUrl = BASE_URL + "/notify";
    // 发起请求并接收返回值
    Map response = restTemplate.postForObject(notifyUrl, request, Map.class);

    // result_code = 1 为成功
    if (!response.get("result_code").equals("1")) {
      System.out.println(response.get("result_type"));
    } else {
      String requestHmac = (String) response.get("hmac");

      response.remove("hmac");

      // 验证hmac
      if (verifyHmac(response, requestHmac)) {
        System.out.println("验证成功");
        System.out.println(response.get("payNo"));
      } else {
        System.out.println("验证失败");
      }
    }
  }

  public boolean verifyHmac(Map<String, String> verify, String hmac) {
    return getHmac(verify).equals(hmac);
  }

  public String getHmac(Map<String, String> map) {
    // 按照字母序排列
    String orderString = OrderByWordUtil.order(map);
    // hmac
    String hmac = DigestUtil.hmacSHA1Base64(orderString);
    return hmac;
  }

  @Test
  public void testMapClear() {
    Map<String, String> map = new HashMap<>();
    map.put("f", "f");

    System.out.println(map);

    // clear
    map.clear();
    System.out.println(map);
  }
}
