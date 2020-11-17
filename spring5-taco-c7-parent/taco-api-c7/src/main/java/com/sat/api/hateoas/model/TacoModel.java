package com.sat.api.hateoas.model;

import java.util.Date;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.sat.api.hateoas.assembler.IngredientModelAssembler;
import com.sat.tacos.Taco;

import lombok.Getter;

/**
 * 将taco转为tacoModel工具类，区别在于能携带链接
 * 并没有包含Taco的id属性。这是因为没有必要在API中暴露数据库相关的ID。从API客户端的角度来看，资源的self链接将会作为该资源的标识符。
 * 没有包含id的结果就是在查询的时候绑定的类中也没有id属性，在delete等unsafe动作的时候可能会报错
 * 继承RepresentationModel 从而继承了一个Link对象的列表和管理链接列表的方法。
 * @author V_QS4SCV
 *
 */
//指定SpringHATEOAS该如何命名结果JSON中的字段名,对应单个和多个的时候
//如下，请求recent路径的时候
//"_embedded": {
//    "tacos": [
//        {
//            "name": "taco3", 
//            "createdAt": "2020-06-18T10:46:12.518+00:00", 
//            "_links": {
//                "self": {
//                    "href": "http://localhost:8080/hateoas/api/design/3"
//                }
//            }
//        }, 
@Relation(value = "taco", collectionRelation = "tacos")
public class TacoModel extends RepresentationModel{
	
	//使tacoModel携带IngredientModel而不是Ingredient
	private static final IngredientModelAssembler
				ingredientModelAssembler = new IngredientModelAssembler();
	
	
	@Getter
	private final String name;
	
	@Getter
	private final Date createdAt;
	
	//使tacoModel携带IngredientModel而不是Ingredient
//	private final List<Ingredient> ingredients; 
	private final CollectionModel<IngredientModel> ingredients; 
	
	public TacoModel(Taco taco) {
		this.name = taco.getName();
		this.createdAt = taco.getCreatedAt();
		
		//使tacoModel携带IngredientModel而不是Ingredient
//		this.ingredients = taco.getIngredients();
		this.ingredients = ingredientModelAssembler.toCollectionModel(taco.getIngredients());
	}
}
