package com.sat.restClient.traverson;

import java.util.Collection;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sat.tacos.Ingredient;
import com.sat.tacos.Taco;

/**
 * 
 * @ClassName: TacoClientTarverson
 * @Description: traverson来调用HATEOAS风格的rest,只能查询，其他操作需要借助RestTemplate
 * 				 需要Traverson的bean或者new 一个
 * @author: sat
 * @date: 2020年6月30日 下午1:26:31
 * @param:
 */
@Service
public class TacoClientTraverson {
	
	private RestTemplate rest;
	private Traverson traverson;
	
//	private static final String URL = "http://localhost:8001/api/ingredients/";
	
	
	public TacoClientTraverson(RestTemplate rest, Traverson traverson) {
		this.rest = rest;
		this.traverson = traverson;
	}
	
	public Iterable<Ingredient> getAllIngredientsWithTraverson(){
		//spring框架实现的从在运行是捕获泛型,返回为集合的时候需要用到这个,注意大括号
		ParameterizedTypeReference<CollectionModel<Ingredient>> ingredientType = 
				new ParameterizedTypeReference<CollectionModel<Ingredient>>() {};
		//follow就是hareoas rest中的rel名称
		CollectionModel<Ingredient> ingredientModels = 
				traverson.follow("ingredients").toObject(ingredientType);
		Collection<Ingredient> ingredients = ingredientModels.getContent();
		return ingredients;
	}
	
	//tarverson和restTemplate结合使用
	public Ingredient addIngredient(Ingredient ingredient) {
		//利用tarverson获取rel为ingredients的链接
		String ingredientsUrl = traverson.follow("ingredients").asLink().getHref();
		//使用restTemplate新增，这边返回的应该是没有主键的
		return rest.postForObject(ingredientsUrl, ingredient, Ingredient.class);
	}
	
	//tarverson访问多层链接
	public Iterable<Taco> getRecentTacosWithTraverson(){
		ParameterizedTypeReference<CollectionModel<Taco>> tacoType = 
				new ParameterizedTypeReference<CollectionModel<Taco>>() {};
		CollectionModel<Taco> tacoModels = traverson.follow("tacos","recents").toObject(tacoType);
		return tacoModels.getContent();
	}
	
	
	
}
