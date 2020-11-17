package com.sat.api.hateoas.model;

import org.springframework.hateoas.RepresentationModel;

import com.sat.tacos.Ingredient;
import com.sat.tacos.Ingredient.Type;

import lombok.Getter;

public class IngredientModel extends RepresentationModel{
	
	@Getter
	private String name;
	
	@Getter
	private Type type;
	
	public IngredientModel(Ingredient ingredient) {
		this.name = ingredient.getName();
		this.type = ingredient.getType();
	}
	
	
}
