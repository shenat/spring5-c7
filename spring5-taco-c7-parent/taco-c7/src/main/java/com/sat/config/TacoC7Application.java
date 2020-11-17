package com.sat.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//之所以要加scan是因为默认只扫描配置类所在的包，如果要扫描其他包的组件需要加@ComponentScan，或者直接将这个类放到类路径下也行
//ComponentScan是扫描指定包及子包下的@Component注解以及使用@Component注解的注解（{@code @Repository}, {@code @Service}, or {@code @Controller} ）
@ComponentScan(basePackages = {"com.sat"})
//扫描 jpa repository
@EnableJpaRepositories(basePackages = "com.sat")
//扫描 jpa entity 注释
@EntityScan(basePackages = "com.sat")
//一般推荐打包为jar包，jar包需要一个主类，这个就是
@SpringBootApplication//表明这是个spring应用，并且具有启动自动扫描，自动配置，还有类似@Configuration的功能

//该配置类非常非常重要，这个出问题就起不来
public class TacoC7Application {

	public static void main(String[] args) {
		//这会创建应用上下文，第一个参数为配置类，第二个参数为命令行参数
		SpringApplication.run(TacoC7Application.class, args);
	}
	
}
