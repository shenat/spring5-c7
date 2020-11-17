package com.sat.web.converter;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.sat.data.tacos.IngredientRepository;
import com.sat.tacos.Ingredient;

//自定义类型转换器，有了这个页面上的Ingredient的String[]才能转为List
@Component//特别注意要加
public class IngredientByIdConverter implements Converter<String, Ingredient> {

  private IngredientRepository ingredientRepo;

  @Autowired
  public IngredientByIdConverter(IngredientRepository ingredientRepo) {
    this.ingredientRepo = ingredientRepo;
  }
  
  @Override
  public Ingredient convert(String id) {
    Optional<Ingredient> optionalIngredient = ingredientRepo.findById(id);
	return optionalIngredient.isPresent() ?
		   optionalIngredient.get() : null;
  }

}
