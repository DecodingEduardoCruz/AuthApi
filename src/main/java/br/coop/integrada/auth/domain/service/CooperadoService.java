package br.coop.integrada.auth.domain.service;

import java.util.List;
import br.coop.integrada.auth.domain.modelo.Role;
import br.coop.integrada.auth.domain.modelo.Cooperado;

public interface CooperadoService {
	
	List<Cooperado> todosCooperados();
	Cooperado daMatricula(Integer matricula);
	Cooperado saveCooperado(Cooperado cooperado);
	
	Role saveRole(Role role);	
	void addRoleToCooperado(Integer matricula, String roleName);
}
