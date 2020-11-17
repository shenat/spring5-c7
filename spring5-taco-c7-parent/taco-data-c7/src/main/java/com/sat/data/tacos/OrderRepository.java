package com.sat.data.tacos;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.sat.tacos.Order;
import com.sat.tacos.User;

@CrossOrigin(origins = "*")
public interface OrderRepository extends CrudRepository<Order, Long>{
	
	//根据用户查找Order并根据placedAt倒序
	//pageable是spring的一个根据页号和每页数量显示子集
	List<Order> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);
}
