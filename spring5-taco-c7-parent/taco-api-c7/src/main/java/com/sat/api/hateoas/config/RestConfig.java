package com.sat.api.hateoas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.RepresentationModelProcessor;

import com.sat.tacos.Taco;


/**
 * 为自定义端点添加超链接
 * 使用controller中的方法有一个问题就是上一层链接中不会指出该方法所在的链接，所以需要RepresentationModelProcessor的帮助
 * 如果控制器返回PagedModel < EntityModel <Taco>>，就会包含一个最近创建的taco链接
 *
 */
@Configuration
public class RestConfig {
	
	//如果有多个的话，添加多个不同的RepresentationModelProcessor接口实现即可，方法名不一样，没有强制命名要求
	@Bean
	public RepresentationModelProcessor<PagedModel<EntityModel<Taco>>> tacoProcessor(EntityLinks links){
		return new RepresentationModelProcessor<PagedModel<EntityModel<Taco>>> () {

			@Override
			public PagedModel<EntityModel<Taco>> process(PagedModel<EntityModel<Taco>> model) {
				// TODO Auto-generated method stub
				model.add(
					links.linkFor(Taco.class)
						 .slash("recent")
						 .withRel("recents"));
				return model;
			}
			
		};
	}
}
