package com.sat.api.hateoas.assembler;

import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import com.sat.api.hateoas.TacoRestController;
import com.sat.api.hateoas.model.IngredientModel;
import com.sat.tacos.Ingredient;

public class IngredientModelAssembler 
				extends RepresentationModelAssemblerSupport<Ingredient,IngredientModel>{

	public IngredientModelAssembler() {
		super(TacoRestController.class, IngredientModel.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IngredientModel toModel(Ingredient ingredient) {
		// TODO Auto-generated method stub
		return createModelWithId(ingredient.getId(), ingredient);
	}

	@Override
	protected IngredientModel instantiateModel(Ingredient ingredient) {
		// TODO Auto-generated method stub
		return new IngredientModel(ingredient);
	}

}
