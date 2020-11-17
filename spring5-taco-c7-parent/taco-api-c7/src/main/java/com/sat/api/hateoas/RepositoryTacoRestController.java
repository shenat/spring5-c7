package com.sat.api.hateoas;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.sat.api.hateoas.assembler.TacoModelAssembler;
import com.sat.api.hateoas.model.TacoModel;
import com.sat.data.tacos.TacoRepository;
import com.sat.tacos.Taco;

/**
 * 与spring data rest结合使用，其他的用repository自动生成的rest,taco的还包含用这个controller生成的rest
 * @author V_QS4SCV
 *
 */
//在使用@RepositoryRestController注解的控制器中，所有映射将会具有和spring.data.rest.base-path属性值一样的前缀
//尽管@RepositoryRestController的名称和@RestController非常相似，
//但是它并没有并没有和@RestController相同的语义。
//具体来讲，它并不能保证处理器方法返回的值会自动写入响应体中。
//所以，我们要么为方法添加@ResponseBody注解，要么返回包装响应数据的ResponseEntity。这里我们选择的方案是返回ResponseEntity。
//与此相关注解还有个@RestResource实在实体类上的指定json中数据名称以及路径，但是路径和controller无关，只有使用repository自动生成的rest才有效
//配置文件中spring.data.rest.base-path也于此有关
@RepositoryRestController
public class RepositoryTacoRestController {
	private TacoRepository tacoRepo;

	public RepositoryTacoRestController(TacoRepository tacoRepo) {
		this.tacoRepo = tacoRepo;
	}
	
	//这里的hal+json就是值包含链接的json
	@GetMapping(path="/tacos/recent", produces="application/hal+json")
	public ResponseEntity<CollectionModel<TacoModel>> recentTacos() {
		 PageRequest page = PageRequest.of(
	                          0, 12, Sort.by("createdAt").descending());
		 List<Taco> tacos = tacoRepo.findAll(page).getContent();
	
		//toCollectionModel方法会循环所有的Taco对象，调用我们在TacoModelAssembler中重写的toModel()方法来创建TacoModel对象的列表。
		CollectionModel<TacoModel> tacoCollectionModel = 
					new TacoModelAssembler().toCollectionModel(tacos);
		
		//这里写这个就是一个完整的hateoas响应了，里面有recents的一个自身链接
		tacoCollectionModel.add(
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RepositoryTacoRestController.class).recentTacos())
				 	.withRel("recents"));
		
		 return new ResponseEntity<>(tacoCollectionModel, HttpStatus.OK);
	 }
}
