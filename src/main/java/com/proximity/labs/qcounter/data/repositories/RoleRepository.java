package com.proximity.labs.qcounter.data.repositories;

import com.proximity.labs.qcounter.data.models.role.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

}