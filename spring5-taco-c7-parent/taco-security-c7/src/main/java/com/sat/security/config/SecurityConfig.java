package com.sat.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	/**
	 * 配置用户认证
	 */
	//自定义用户认证-用户详情服务类
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		//基于内存的用户存储
//		auth.inMemoryAuthentication()
//				.passwordEncoder(new BCryptPasswordEncoder())
//				.withUser("root")
//					.password(new BCryptPasswordEncoder().encode("123456"))
//					.authorities("ROLE_USER")
//				.and()
//				.withUser("sat")
//				.password(new BCryptPasswordEncoder().encode("123456"))
//				.authorities("ROLE_USER");
		
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(encoder());
	}
	
	//引入一个密码转码器
	//对于encoder()的任何调用都会被拦截，并且会返回应用上下文中的bean实例。
	@Bean
	public PasswordEncoder encoder() {
		//deprecated
//		return new StandardPasswordEncoder("53cr3t");
		return new BCryptPasswordEncoder();
	}

	/**
	 * 配置 保护web请求
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		//对/design和/order需要ROLE_USER权限访问，其他没有限制
		//顺序很重要，前面的具有更高的优先级，所以越具体的约束需要在最前面
		http.authorizeRequests()
				.antMatchers("/design","/order")
				//一样的效果
	//			.access("hasRole('ROLE_USER')")
	//			.antMatchers("/","/**")
	//			.access("permitAll");
				.hasRole("USER")//在spring5中不需要ROLE前缀了，会自动加入
				.antMatchers("/","/**")
				.permitAll()
			//使用了security后如果要使用h2除了路径允许访问还要加下面这两个
			.and().csrf().ignoringAntMatchers("/h2-console/**","/api/**") // 禁用 H2 控制台的 CSRF 防护,后增加api调用的
	        .and().headers().frameOptions().sameOrigin() // 允许来自同一来源的 H2 控制台的请求
			//自定义登录路径，所以还需要一个配置一个视图控制器，将该路径请求映射到视图上和一个登录页面
			.and()
				.formLogin()
				.loginPage("/login")
				//设置security监听的登录请求地址，默认是/login，就是登录页面表单提交的地址
//				.loginProcessingUrl("/authenticate")
				//设置登录请求地址提交的预期用户名和密码的参数名称，默认是username和password
//				.usernameParameter("user")
//				.passwordParameter("pwd")
				//设置登录成功后统一访问那个路径
				.defaultSuccessUrl("/design", true)
			//设置退出，会自动拦截/logout的请求，斌且重定向到指定页面
			.and()
				.logout()
				//.logoutUrl("/logout")//出发推出的url，默认就是这个
				.logoutSuccessUrl("/");
			//禁用跨域请求伪造，但是不建议使用，默认是开启的
			//但页面使用thymeleaf的时候，甚至都不需要加任何东西，只要表单中某处使用了th前缀即可
//			.and()
//				.csrf()
//				.disable();
	}
	
	
}
