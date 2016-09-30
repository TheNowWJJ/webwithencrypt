package cn.veryjava.webwithencrypt;

import cn.veryjava.encrypt.Base64Util;
import cn.veryjava.encrypt.CertificateUtil;
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
 * 日期: 16-9-28.
 * 项目名称: webwithencrypt
 * 版本: 1.0
 * JDK: since 1.8
 */
@Controller
@RequestMapping("/server/v1")
public class ServerController {

  /**
   * 数字证书
   */
  @RequestMapping(value = "/test", method = RequestMethod.POST)
  @ResponseBody
  public String get(@RequestBody HashMap<String, String> param) throws Exception {
    byte[] sign = Base64Util.decodeBase64(param.get("sign"));

    // 接收参数
    System.out.println("sign:" + param.get("sign"));
    System.out.println("name:" + param.get("name"));
    System.out.println("accountId:" + param.get("accountId"));
    System.out.println("remark:" + param.get("remark"));

    // 移除sign
    param.remove("sign");

    final String string = OrderByWordUtil.order(param);
    System.out.println("server queryString:" + string);

    // 使用公玥进行验签
    boolean verify = CertificateUtil.verifySign("/home/barton/testcer.cer", string.getBytes(),
     sign);
    System.out.println("verify:" + verify);

    if (!verify) {
      throw new RuntimeException("error");
    } else {
      Map<String, String> response = new HashMap<>();
      response.put("what", "the fuck");
      response.put("nani", "goupi");

      String responseString = OrderByWordUtil.order(response);

      System.out.println("server response String : " + responseString);
    }

    return "ok";
  }
}
