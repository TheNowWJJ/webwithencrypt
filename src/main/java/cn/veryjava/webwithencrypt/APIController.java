package cn.veryjava.webwithencrypt;

import cn.veryjava.encrypt.DigestUtil;
import cn.veryjava.encrypt.OrderByWordUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
@Controller
@RequestMapping("/api/gf_client")
public class APIController {

  @RequestMapping(value = "/verify",method = RequestMethod.POST)
  @ResponseBody
  public Map<String, String> verify(@RequestBody Map<String, String> requestParams) {
    String hmac = requestParams.get("hmac");
    System.out.println("request hmac : " + hmac);

    // 验证
    requestParams.remove("hmac");
    String orderString = OrderByWordUtil.order(requestParams);
    String verifyString = DigestUtil.hmacSHA1Base64(orderString);
    System.out.println("verifyString : " + verifyString);
    System.out.println(verifyString.equals(hmac));

    // 返回值
    Map<String, String> response = new HashMap<>();
    response.put("orderId", requestParams.get("orderId"));
    response.put("account", requestParams.get("account"));
    response.put("result_code", "1");
    response.put("result_type", "成功");
    response.put("hmac", hmac); // 懒

    return response;
  }
}
