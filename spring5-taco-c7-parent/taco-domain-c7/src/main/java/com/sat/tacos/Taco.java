package com.sat.tacos;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.rest.core.annotation.RestResource;

import lombok.Data;

//墨西哥卷
@Data
@Entity//jpa注解
//该注解用于spring data rest将Resource接口自动配置为rest,为其设置路径和关系名，和采用spring hateoas中给model的注解@Relation有些类似
@RestResource(rel = "tacos",path = "tacos")
public class Taco {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)//主键策略
	private Long id;
	
  @NotNull
  @Size(min=5, message="Name must be at least 5 characters long")
  private String name;
  
  @ManyToMany(targetEntity = Ingredient.class)//多对多
  @NotNull(message="You must choose at least 1 ingredient")//在校验的时候必须加上这个，不然null的时候下面的size校验不会生效
  @Size(min=1, message="You must choose at least 1 ingredient")
  private List<Ingredient> ingredients;
  
  private Date createdAt;
  
  @PrePersist//持久化之前设置createAt的值
  void createdAt() {
	  this.createdAt = new Date();
  }
	
	

}
