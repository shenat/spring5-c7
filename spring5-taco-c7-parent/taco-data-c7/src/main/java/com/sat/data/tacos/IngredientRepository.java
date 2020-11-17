package com.sat.data.tacos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.sat.tacos.Ingredient;

//rest给其他人用需要避免同源问题
@CrossOrigin(origins = "*")
public interface IngredientRepository extends CrudRepository<Ingredient, String> {
	
}
