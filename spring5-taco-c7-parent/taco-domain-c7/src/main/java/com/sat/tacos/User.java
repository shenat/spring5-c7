package com.sat.tacos;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

//自定义用户领域对象 注意这里实现了security中的UserDetails接口
@Entity
@Data
//force为true表示初始化final变量为0/null/false
//如果不设置force为true,则会报错final变量未初始化
@NoArgsConstructor(access=AccessLevel.PRIVATE,force=true)
@RequiredArgsConstructor
public class User implements UserDetails {

	private static final long serialVersionUID = 3302569644289189498L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private final String username;
	private final String password;
	private final String fullname;
	private final String street;
	private final String city;
	private final String state;
	private final String zip;
	private final String phoneNumber;
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		//表明所有用户赋予ROLE_USER权限
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		//永不过期
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		//永不锁定
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		//认证永不过期
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		//永远可用
		return true;
	}

}
