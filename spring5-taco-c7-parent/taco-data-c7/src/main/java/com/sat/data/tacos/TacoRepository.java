package com.sat.data.tacos;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.sat.tacos.Taco;

@CrossOrigin(origins = "*")
public interface TacoRepository extends PagingAndSortingRepository<Taco, Long>{
	//该注解用在repository方法上可以改变链接的rel和链接最后一个单词名称
//	http://localhost:8080/api/tacos/search：
//	{
//		  "_links" : {
//		    "searchByName" : {
//		      "href" : "http://localhost:8080/api/tacos/search/name{?name}",
//		      "templated" : true
//		    },
//		    "self" : {
//		      "href" : "http://localhost:8080/api/tacos/search"
//		    }
//		  }
//		}
	@RestResource(rel = "searchByName",path = "name")
	//该方法能在repository中自动实现，会在返回中增加如下内容：
//	http://localhost:8080/api/tacos/
//	 "search" : {
//      "href" : "http://localhost:8080/api/tacos/search"
//    }
	public Taco findByName(String name);
}
