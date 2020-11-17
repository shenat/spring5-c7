package com.sat.props;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@Component//不可少
@ConfigurationProperties(prefix="taco.orders")//会将taco.orders.pageSize资源注入成员属性中
@Data
@Validated//这个其实相当于在controller的方法参数上加上该注解，但是controller方法参数上没有这个类，所以夹着这边
public class OrderProps {
	
	@Min(value=1,message="must be between 1 and 3")
	@Max(value=3,message="must be between 1 and 3")
	private int pageSize = 2;
}
