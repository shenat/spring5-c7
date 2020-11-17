package com.sat.restClient.normal;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sat.tacos.Ingredient;

import lombok.extern.slf4j.Slf4j;

/**
 * rest简单客户端类，使用的restTemplate,只需要引入spring-web即可,需要有restTemplate的bean或者new一个
 * @author V_QS4SCV
 *
 */
@Service//为了在测试类中能componentScan到
@Slf4j
public class TacoClient {
	private RestTemplate rest;
	private String url = "http://localhost:8001/api/ingredients/";
	
	public TacoClient(RestTemplate rest) {
		this.rest = rest;
	}

	//getForObject
	public Ingredient getIngredientById(String ingredientId) {
		return rest.getForObject(url + "{id}", Ingredient.class, ingredientId);
		
	}
	
	public Ingredient getIngredientById_map(String ingredientId) {
		Map<String,String> uriVariables = new HashMap<>();
		uriVariables.put("id", ingredientId);
		return rest.getForObject(url + "{id}", Ingredient.class, uriVariables);
	}
	
	public Ingredient getIngredientById_URI(String ingredientId) {
		Map<String,String> uriVariables = new HashMap<>();
		uriVariables.put("id", ingredientId);
		URI uri = UriComponentsBuilder.fromHttpUrl(url + "{id}").build(uriVariables);
		return rest.getForObject(uri, Ingredient.class);
		
	}
	
	//getForEntity
	public Ingredient getIngredientById_entity(String ingredientId) {
		ResponseEntity<Ingredient> responseEntity = rest.getForEntity(url + "{id}", Ingredient.class, ingredientId);
		log.info("fetched time: " + new Date(responseEntity.getHeaders().getDate()));
		return responseEntity.getBody();
	}
	
	//exchange/excute
	//注意这边的ParameterizedTypeReference是为了获取运行时泛型信息，在返回内容为list的时候可以直接转换
	//如果用{@Link RestTemplate#exchange(URI url, HttpMethod method, @Nullable HttpEntity<?> requestEntity,Class<T> responseType)}
	//是无法使用的，而使用RestTemplate.postForObject是无法知道类型的，最终T会是List<LinkedHashMap>
	//所以用ParameterizedTypeReference可以解决返回时集合的问题
	////这个暂时不测，因为使用的data-rest在返回list集合的时候json中会有一个_embedded属性，所以绑定数据出错
	public List<Ingredient> getAllIngredients(){
//		return rest.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Ingredient>>() {}).getBody();
		return rest.exchange(url,
	            HttpMethod.GET, null, new ParameterizedTypeReference<List<Ingredient>>() {})
	        .getBody();
	}
	
	//put,更新操作,返回为void
	public void updateIngredient(Ingredient ingredient) {
		rest.put(url + "{id}", ingredient, ingredient.getId());
	}
	
	//post,添加操作
	public Ingredient createIngredient(Ingredient ingredient) {
		return rest.postForObject(url, ingredient, Ingredient.class);
	}
	
	public URI createIngredient_URI(Ingredient ingredient) {
	     return rest.postForLocation(url,
	         ingredient, Ingredient.class);
	}
	
	public Ingredient createIngredient_entity(Ingredient ingredient) {
	     ResponseEntity<Ingredient> responseEntity =
	            rest.postForEntity(url,
	                           ingredient,
	                           Ingredient.class);
	     log.info("New resource created at " +
	              responseEntity.getHeaders().getLocation());
	     return responseEntity.getBody();
	}
	
	//delete
	public void deleteIngredient(Ingredient ingredient) {
		log.info("delete id:" + ingredient.getId());
		rest.delete(url + "{id}", ingredient.getId());
	}
	
	
}
