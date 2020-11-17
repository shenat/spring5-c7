package com.sat.api.hateoas.assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import com.sat.api.hateoas.TacoRestController;
import com.sat.api.hateoas.model.TacoModel;
import com.sat.tacos.Taco;

/**
 * 资源装备器，以便将taco转为tacoModel
 * @author V_QS4SCV
 *
 */
public class TacoModelAssembler//注意继承类中的泛型一个是taco一个是tacomodel 
			extends RepresentationModelAssemblerSupport<Taco,TacoModel>{
	
	//因为父类中只有由参构造器，所以这时候系统不会默认生成无参构造器
	//所以这里也必须有个能调用父类有参构造器的构造器(无参有参都可以)，不然不能向上回溯
	public TacoModelAssembler() {
		//调用父类的时候将controller和model相结合了
		//会告诉超类（RepresentationModelAssemblerSupport）在创建TacoModel中的链接时将会使用DesignTacoController来确定所有URL的基础路径。
		super(TacoRestController.class, TacoModel.class);
		// TODO Auto-generated constructor stub
	}

	//这个是强实现方法
	//告诉它要通过Taco创建TacoResource，并且要设置一个self链接，这个链接的URL是根据Taco对象的id属性衍生出来的。
	@Override
	public TacoModel toModel(Taco taco) {
		// TODO Auto-generated method stub
		return createModelWithId(taco.getId(), taco);
	}

	//这个是可选 以便基于给定的Taco实例化TacoResource。
	@Override
	protected TacoModel instantiateModel(Taco taco) {
		// TODO Auto-generated method stub
		return new TacoModel(taco);
	}
	
	
}
