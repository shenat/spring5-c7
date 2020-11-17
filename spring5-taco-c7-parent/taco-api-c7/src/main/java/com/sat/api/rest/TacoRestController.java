package com.sat.api.rest;
//package com.sat.web.rest;
//
//import java.util.Optional;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.sat.bean.Order;
//import com.sat.bean.Taco;
//import com.sat.data.interfaces.TacoRepository;
//
//@RestController
//@RequestMapping(path="/design", 
//				produces="application/json") //只处理accept是json的请求，多个值隔开
//@CrossOrigin(origins="*")//允许那些地址可以跨域请求这个服务，这里是任何地址都可以
//public class TacoRestController {
//	private TacoRepository tacoRepo;
//	
//	
////	EntityLinks entityLinkl;
//	
//	public TacoRestController(TacoRepository tacoRepo) {
//		this.tacoRepo = tacoRepo;
//	}
//	
//	/**
//	 * 查找资源
//	 */
//	
//	@GetMapping("/recent")
//	public Iterable<Taco> recentTacos(){
//		PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
//		return tacoRepo.findAll(page).getContent();
//	}
//	
//	@GetMapping("/{id}")
//	public ResponseEntity<Taco> tacoById(@PathVariable("id") Long id){
//		Optional<Taco> optTaco = tacoRepo.findById(id);
//		if(optTaco.isPresent()) {
//			return new ResponseEntity<Taco>(optTaco.get(), HttpStatus.OK);
//		}
//		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//	}
//	
//	/**
//	 * 创建资源
//	 */
//	
//	@PostMapping(consumes="application/json")//多个值逗号隔开
//	@ResponseStatus(HttpStatus.CREATED)//这个注解不同于查询方法中的ResponseEntity改变响应状态码，这里是改变正常的响应状态码，正常不添加该注解返回的是200
//	public Taco postTaco(@RequestBody Taco taco) {//@RequestBody不可少，是将请求体中json对象绑定到该对象
//		return tacoRepo.save(taco);
//	}
//	
//	/**
//	 * 更新资源
//	 */
//	
//	//完全更新,不可有遗漏信息
//	@PutMapping(path="/{id}",consumes="application/json")
//	public Taco putOrder(@RequestBody Taco taco) {
//		return tacoRepo.save(taco);
//		
//	}
//	
//	//局部更新
//	@PatchMapping(path="/{id}",consumes="application/json")
//	public Taco patchOrder(@PathVariable("id") Long id,
//						@RequestBody Taco patch) {
//		Optional<Taco> option = tacoRepo.findById(id);
//		Taco taco;
//		if(option.isPresent()) {
//			taco = option.get();
//			if(patch.getIngredients() != null) {
//				taco.setIngredients(patch.getIngredients());
//			}
//			if(patch.getCreatedAt() != null) {
//				taco.setCreatedAt(patch.getCreatedAt());
//			}
//			if(StringUtils.isNotBlank(patch.getName())) {
//				taco.setName(patch.getName());
//			}
//		}else {
//			taco = patch;
//		}
//		return tacoRepo.save(taco);//这个save方法的实现包括merge
//	}
//	
//	
//	/**
//	 * 删除资源
//	 */
//	
//	@DeleteMapping("/{id}")
//	@ResponseStatus(code=HttpStatus.NO_CONTENT)
//	public void deleteOrder(@PathVariable("id") Long id) {
//		try {
//			tacoRepo.deleteById(id);
//		} catch(EmptyResultDataAccessException e) {
//			//不做任何动作
//		}
//	}
//}
