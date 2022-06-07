package br.coop.integrada.auth.domain.repository;

import br.coop.integrada.auth.domain.modelo.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRep extends JpaRepository<Role, Integer>{
	Role findByName(String Name);
}
