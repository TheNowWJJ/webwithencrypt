package cn.veryjava.webwithencrypt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 描述: TODO:
 * 包名: cn.veryjava.webwithencrypt.
 * 作者: barton.
 * 日期: 16-9-28.
 * 项目名称: webwithencrypt
 * 版本: 1.0
 * JDK: since 1.8
 */
@Configuration
public class RestTemplateConfig {
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
