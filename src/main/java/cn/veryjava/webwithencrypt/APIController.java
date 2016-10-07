package cn.veryjava.webwithencrypt;

import cn.veryjava.encrypt.DigestUtil;
import cn.veryjava.encrypt.OrderByWordUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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

  @RequestMapping(value = "/order/{orderNo}", method = RequestMethod.GET)
  @ResponseBody
  public Map<String, String> getOrder(@PathVariable String orderNo) {
    // 返回值
    Map<String, String> response = new HashMap<>();
    try {
      if (StringUtils.isEmpty(orderNo)) {
        response.put("result_code", "3");
        response.put("result_type", "订单号为空");
        return response;
      }

      System.out.println("request orderNo : " + orderNo);

      // 根据订单号查询到的金额
      String amount = "1024.00";

      response.put("orderNo", orderNo);
      response.put("amount", amount);
      response.put("result_code", "1");
      response.put("result_type", "成功");
      // hmac
      String responseHmac = getHmac(response);
      response.put("hmac", responseHmac);
    } catch (Exception e) {
      response.clear();
      response.put("result_code", "0");
      response.put("result_type", "未知错误,请联系三际技术部");
      return response;
    }
    return response;
  }

  @RequestMapping(value = "/notify", method = RequestMethod.POST)
  @ResponseBody
  public Map<String, String> fy_notify(@RequestBody Map<String, String> requestParams) {
    Map<String, String> verifyParams = verifyParams(requestParams);
    Map<String, String> response = new HashMap<>();
    try {
      if (null != verifyParams) {
        return verifyParams;
      } else {
        String hmac = requestParams.get("hmac");
        System.out.println("request hmac:" + hmac);

        requestParams.remove("hmac");
        // 验证hmac
        if (verifyHmac(requestParams, hmac)) {
          System.out.println("验证成功");
          System.out.println(requestParams.get("orderNo"));

          // 其他操作
          // ...

          response.put("orderNo", requestParams.get("orderNo"));
          response.put("result_code", "1");
          response.put("result_type", "成功");
          response.put("payNo", requestParams.get("payNo"));
          // hmac
          String responseHmac = getHmac(response);
          response.put("hmac", responseHmac);
          return response;
        } else {
          System.out.println("验证失败");
        }
      }
    } catch (Exception e) {
      response.clear();
      response.put("result_code", "0");
      response.put("result_type", "未知错误,请联系三际技术部");
      return response;
    }
    return null;
  }

  public Map<String, String> verifyParams(Map<String, String> verify) {
    Map<String, String> response = new HashMap<>();
    if (StringUtils.isEmpty(verify.get("orderNo"))) {
      response.put("result_code", "3");
      response.put("result_type", "订单号为空");
      return response;
    }

    if (StringUtils.isEmpty(verify.get("amount"))) {
      response.put("result_code", "4");
      response.put("result_type", "金额为空");
      return response;
    }

    if (StringUtils.isEmpty(verify.get("hmac"))) {
      response.put("result_code", "5");
      response.put("result_type", "数字摘要为空");
      return response;
    }

    if (StringUtils.isEmpty(verify.get("payNo"))) {
      response.put("result_code", "8");
      response.put("result_type", "支付流水号为空");
      return response;
    }

    if (StringUtils.isEmpty(verify.get("bankCardNo"))) {
      response.put("result_code", "9");
      response.put("result_type", "支付卡号为空");
      return response;
    }

    return null;
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
}
