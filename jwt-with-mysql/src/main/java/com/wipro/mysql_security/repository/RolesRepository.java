package com.wipro.mysql_security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wipro.mysql_security.entity.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer>{

	Roles findByName(String role);
	
}
