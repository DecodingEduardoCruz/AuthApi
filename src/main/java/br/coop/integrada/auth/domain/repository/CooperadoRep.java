package br.coop.integrada.auth.domain.repository;

import org.springframework.stereotype.Repository;
import br.coop.integrada.auth.domain.modelo.Cooperado;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CooperadoRep extends JpaRepository<Cooperado, Integer>{
	Cooperado findByMatricula(Integer matricula);
}
