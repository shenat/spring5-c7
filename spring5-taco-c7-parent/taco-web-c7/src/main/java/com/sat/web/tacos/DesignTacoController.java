package com.sat.web.tacos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.sat.data.tacos.IngredientRepository;
import com.sat.data.tacos.TacoRepository;
import com.sat.tacos.Ingredient;
import com.sat.tacos.Ingredient.Type;
import com.sat.tacos.Order;
import com.sat.tacos.Taco;
import com.sat.tacos.User;

import lombok.extern.slf4j.Slf4j;

@SessionAttributes("order")//该注解的作用是将model中的order设置为session方位的，以便跨请求访问
@Slf4j//Lombok提供的日志注解会自动生成logger
@Controller
@RequestMapping("/design")
public class DesignTacoController {
//	private static final Logger log = LoggerFactory.getLogger(DesignTacoController.class);
	
	//注意这里的final
	private final IngredientRepository ingredientRepo;
	private TacoRepository designRepo;
	
	
	//@Autowired
	public DesignTacoController(
	        IngredientRepository ingredientRepo, 
	        TacoRepository designRepo) {
	    this.ingredientRepo = ingredientRepo;
	    this.designRepo = designRepo;
	}
	
	//在所有处理方法之前执行，在model中增加数据
	@ModelAttribute
	public void addIngredientsToModel(Model model) {
		List<Ingredient> ingredients = new ArrayList<>();
		ingredientRepo.findAll().forEach(i -> ingredients.add(i));
		
		Type[] types = Ingredient.Type.values();
		for (Type type : types) {
		  model.addAttribute(type.toString().toLowerCase(),
		      filterByType(ingredients, type));
		}
	}
	
	@ModelAttribute(name = "order")
	public Order order() {
		return new Order();
	}
	
	@ModelAttribute(name = "design")
	public Taco taco() {
		return new Taco();
	}
	
	
	@GetMapping//spring4.3引入的,之前用requestMapping代替
	public String showDesignForm(Model model) {
//		model.addAttribute("design",new Taco());
		return "design";
	}
	
	//@Valid和Errors执行校验
	//为什么加了@ModelAttribute("design")校验错误再返回的时候就不报错了呢
	//因为ModelAttribute修饰方法参数的作用就是取出前台传给后台的值，放到model中再传给前台，必须有name不然key会为taco
	@PostMapping
	public String processDesign(@Valid @ModelAttribute("design") Taco design,
			Errors errors,
			@ModelAttribute Order order,
			//这个参数和注解可以从security认证中获取到认证用户
			@AuthenticationPrincipal User user) {
		
		if(errors.hasErrors()) {
			return "design";
		}
		designRepo.save(design);
		order.addDesign(design);//由于order是跨请求的，所以多次创建taco都会在order中，页面使用order.tacos会都出来
		order.setUser(user);//将认证用户信息加进去
		return "redirect:/orders/current";
	}

	private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
		// TODO Auto-generated method stub
		//流式编程，将list集合中对象的Type属性等于type的筛选出来重选转为集合输出
		return ingredients.stream().filter(x -> x.getType().equals(type))
				.collect(Collectors.toList());
	}
}
