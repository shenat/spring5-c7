package com.sat.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

//用于配置security login的自定义错误提示消息，需要放在不继承任何类的配置类中
@Configuration
public class SecurityPropertiesConfig {
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
		source.setBasename("classpath:security/loginError");
		return source;
	}
}
