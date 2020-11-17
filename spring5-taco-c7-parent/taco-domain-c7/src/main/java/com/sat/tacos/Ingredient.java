package com.sat.tacos;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.data.rest.core.annotation.RestResource;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data//用的lombok的自动生成缺少方法
//如果得写一个有参构造器，就一定要有一个无参构造器，不然向上回溯会发生错误
//如果不需要有参构造器，那就不必那么麻烦，可以什么构造器都不写
//@RequiredArgsConstructor//用的lombok的自动生成参数构造器（参数只正对final并且非null得）//c7 修改
@AllArgsConstructor// c7 增加
@NoArgsConstructor(access = AccessLevel.PRIVATE,force = true)//jpa必须要有一个无参构造器，但是这里还设置为private和初始化所有final为空的
@Entity//jpa注解
public class Ingredient {
	
	/**
	 * 使用final修饰会报：The blank final field id may not have been  initialized
	 * 但是如果构造器中对其初始化了，则不会报这个错，且不能有无参构造器了
	 * 因为无参构造器会初始化的时候不将final变量初始化
	 */
	@RestResource(exported = true)
	@Id//jpa注解，主键，没有加主键策略因为这边值都是手动插入的，主键也是手动指定的
//	private final String id;
	private String id;//c7 修改 final不能put
//	private final String name;
	private String name;//c7 修改
//	private final Type type;
	private Type type;//c7 修改
  
 	public static enum Type {
 		WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
 	}

 	

 	
 	
  
  
}
