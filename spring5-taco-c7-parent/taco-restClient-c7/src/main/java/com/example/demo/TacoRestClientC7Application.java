package com.example.demo;

import java.net.URI;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.web.client.RestTemplate;

import com.sat.restClient.normal.TacoClient;
import com.sat.restClient.traverson.TacoClientTraverson;
import com.sat.tacos.Ingredient;
import com.sat.tacos.Taco;

import lombok.extern.slf4j.Slf4j;

@ComponentScan(basePackages = {"com.sat"})
//@EnableJpaRepositories(basePackages = "com.sat")
//@EntityScan(basePackages = "com.sat")
@SpringBootApplication
@Slf4j
public class TacoRestClientC7Application {

	public static void main(String[] args) {
		SpringApplication.run(TacoRestClientC7Application.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	//单独使用RestTemplate测试
	/**
	@Bean
	public CommandLineRunner fetchIngredients(TacoClient client) {
		return args -> {
			 log.info("----------------------- GET -------------------------");
			 log.info("GETTING INGREDIENT BY IDE");
			 log.info("Ingredient:  " + client.getIngredientById("CHED"));
			 //这个暂时不测，因为使用的data-rest在返回list集合的时候json中会有一个_embedded属性，所以绑定数据出错
//			 log.info("GETTING ALL INGREDIENTS");
//			 List<Ingredient> ingredients = client.getAllIngredients();
//			 log.info("All ingredients:");
//			 for(Ingredient ingredient : ingredients) {
//				 log.info("   - " + ingredient);
//			 }
		};
	}
	
	//这个也不好测,因为Ingredient的属性都是final的没有set方法，所以不能更新操作，虽然访问profile路径的时候提示可以更新
	//put方法经测试，有主键相同得更新，没有则新增，如果实体类中哪怕只有一个final属性，就put不了,此时需要手动重新rest服务端得update方法
	@Bean
	  public CommandLineRunner putAnIngredient(TacoClient tacoCloudClient) {
	    return args -> {
	      log.info("----------------------- PUT -------------------------");
	      Ingredient before = tacoCloudClient.getIngredientById("LETC");
	      log.info("BEFORE:  " + before);
	      tacoCloudClient.updateIngredient(new Ingredient("LETC2", "Shredded Lettuce2", Ingredient.Type.VEGGIES));
	      Ingredient after = tacoCloudClient.getIngredientById("LETC2");
	      log.info("AFTER:  " + after);
	    };
	  }
	  
//	  @Bean
	  public CommandLineRunner addAnIngredient(TacoClient tacoCloudClient) {
	    return args -> {
	      log.info("----------------------- POST -------------------------");
	      Ingredient chix = new Ingredient("CHIX", "Shredded Chicken", Ingredient.Type.PROTEIN);
	      Ingredient chixAfter = tacoCloudClient.createIngredient(chix);
	      log.info("AFTER=1:  " + chixAfter);
//	      Ingredient beefFajita = new Ingredient("BFFJ", "Beef Fajita", Ingredient.Type.PROTEIN);
//	      URI uri = tacoCloudClient.createIngredient(beefFajita);
//	      log.info("AFTER-2:  " + uri);      
//	      Ingredient shrimp = new Ingredient("SHMP", "Shrimp", Ingredient.Type.PROTEIN);
//	      Ingredient shrimpAfter = tacoCloudClient.createIngredient(shrimp);
//	      log.info("AFTER-3:  " + shrimpAfter);      
	    };
	  }

	  //测试时发现before中没有id，而ingredient中的id时final的不好设置，所以也不好测试
//	  @Bean
	  public CommandLineRunner deleteAnIngredient(TacoClient tacoCloudClient) {
		return args -> {
		  log.info("----------------------- DELETE -------------------------");
		  // start by adding a few ingredients so that we can delete them later...
		  Ingredient beefFajita = new Ingredient("BFFJ", "Beef Fajita", Ingredient.Type.PROTEIN);
		  tacoCloudClient.createIngredient(beefFajita);
		  Ingredient shrimp = new Ingredient("SHMP", "Shrimp", Ingredient.Type.PROTEIN);
		  tacoCloudClient.createIngredient(shrimp);
		
		  
		  Ingredient before = tacoCloudClient.getIngredientById("CHIX");
		  //测试时发现before中没有id，而ingredient中的id时final的不好设置，所以也不好测试
		  log.info("BEFORE:  " + before);
		  tacoCloudClient.deleteIngredient(before);
		  Ingredient after = tacoCloudClient.getIngredientById("CHIX");
		  log.info("AFTER:  " + after);
		  before = tacoCloudClient.getIngredientById("BFFJ");
		  log.info("BEFORE:  " + before);
		  tacoCloudClient.deleteIngredient(before);
		  after = tacoCloudClient.getIngredientById("BFFJ");
		  log.info("AFTER:  " + after);
		  before = tacoCloudClient.getIngredientById("SHMP");
		  log.info("BEFORE:  " + before);
		  tacoCloudClient.deleteIngredient(before);
		  after = tacoCloudClient.getIngredientById("SHMP");
		  log.info("AFTER:  " + after);
		};
	  }
	**/
	
	//使用Traverson和RestTemplate
	@Bean
	public Traverson traverson() {
		Traverson traverson = new Traverson(
				URI.create("http://localhost:8001/api/"),MediaTypes.HAL_JSON);
		return traverson;
	}
	
	/**
	 * 
	 * @Title: traversonGetIngredients
	 * @Description: 测试tarverson查询
	 * @param: traversonClient
	 * @return: CommandLineRunner 
	 * @throws
	 */
	@Bean
	public CommandLineRunner traversonGetIngredients(TacoClientTraverson traversonClient) {
		return args ->{
			Iterable<Ingredient> ingredients = traversonClient.getAllIngredientsWithTraverson();
			log.info("----------------------- GET INGREDIENTS WITH TRAVERSON -------------------------");
			for(Ingredient ingredient : ingredients) {
				log.info("  -  " + ingredient);
			}
		};
		
	}
	
	@Bean
	public CommandLineRunner traversonSaveIngredient(TacoClientTraverson traversonClient,TacoClient tacoCloudClient) {
		return args ->{
			Ingredient pico = traversonClient.addIngredient(
					new Ingredient("PICO", "Pico de Gallo", Ingredient.Type.SAUCE));
			Iterable<Ingredient> ingredients = traversonClient.getAllIngredientsWithTraverson();
			log.info("----------------------- ALL INGREDIENTS AFTER SAVING PICO -------------------------");
			for(Ingredient ingredient : ingredients) {
				log.info("  -  " + ingredient);
			}
			tacoCloudClient.deleteIngredient(pico);
		};
	}
	
	@Bean
	public CommandLineRunner traversonRecentTacos(TacoClientTraverson traversonClient) {
		return args -> {
			Iterable<Taco> recentTacos = traversonClient.getRecentTacosWithTraverson();
			log.info("----------------------- GET RECENT TACOS WITH TRAVERSON -------------------------");
			for (Taco taco : recentTacos) {
				log.info("   -  " + taco);
			}
	    };
	}
	
}
