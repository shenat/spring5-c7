//tag::all[]
//tag::allButValidation[]
package com.sat.tacos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.CreditCardNumber;

import lombok.Data;

@Data
@Entity//jpa
@Table(name="Taco_Order")//jpa
public class Order implements Serializable{

	private static final long serialVersionUID = -3664518918580354820L;
	
	 @Id//jpa
	 @GeneratedValue(strategy = GenerationType.AUTO)//jpa
	 private Long id;
	 
	 //end::allButValidation[]
	 @NotBlank(message="Name is required")
	 //tag::allButValidation[]
	 private String name;
	 //end::allButValidation[]
	
	  @NotBlank(message="Street is required")
	  //tag::allButValidation[]
	  private String street;
	  //end::allButValidation[]
	
	  @NotBlank(message="City is required")
	  //tag::allButValidation[]
	  private String city;
	  //end::allButValidation[]
	
	  @NotBlank(message="State is required")
	  //tag::allButValidation[]
	  private String state;
	  //end::allButValidation[]
	
	  @NotBlank(message="Zip code is required")
	  //tag::allButValidation[]
	  private String zip;
	  //end::allButValidation[]
	
	  @CreditCardNumber(message="Not a valid credit card number")
	  //tag::allButValidation[]
	  private String ccNumber;
	  //end::allButValidation[]
	
	  @Pattern(regexp="^(0[1-9]|1[0-2])([\\/])([1-9][0-9])$",
	           message="Must be formatted MM/YY")
	  //tag::allButValidation[]
	  private String ccExpiration;
	  //end::allButValidation[]
	  
	  //digits中的integer表示最多几个数字，fraction表示小数最多几个
	  @Digits(integer=3, fraction=0, message="Invalid CVV")
	  //tag::allButValidation[]
	  private String ccCVV;
	  
	  private Date placedAt;
	  
	  @ManyToMany(targetEntity = Taco.class)//jpa
	  private List<Taco> tacos = new ArrayList<>();
	  
	  @ManyToOne//jpa
	  private User user;
	  
	  public void addDesign(Taco design) {
	    this.tacos.add(design);
	  }
	  
	  @PrePersist//jpa
	  void placeAt() {
		  this.placedAt = new Date();
	  }


}
//end::allButValidation[]
//end::all[]
