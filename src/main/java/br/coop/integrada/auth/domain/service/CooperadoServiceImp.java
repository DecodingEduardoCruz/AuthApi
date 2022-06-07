package br.coop.integrada.auth.domain.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.coop.integrada.auth.domain.modelo.Role;
import br.coop.integrada.auth.domain.repository.CooperadoRep;
import br.coop.integrada.auth.domain.repository.RoleRep;
import br.coop.integrada.auth.AuthApplication;
import br.coop.integrada.auth.domain.modelo.Cooperado;

import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor @Transactional
public class CooperadoServiceImp implements CooperadoService, UserDetailsService{
	private static final Logger logger = LoggerFactory.getLogger(AuthApplication.class);
	private final PasswordEncoder passwordEncoder;
	private final CooperadoRep cooperadoRep;
	private final RoleRep roleRep;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Cooperado cooperado = cooperadoRep.findByMatricula(Integer.parseInt(username));
		if(cooperado == null) {
			logger.info("Cooperado não encontrado!");
			throw new UsernameNotFoundException("Cooperado não encontrado!");			
		}
		
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		cooperado.getRoles().forEach(role -> { 
			authorities.add(new SimpleGrantedAuthority(role.getName())); 
		});
		
		logger.info("Autenticando cooperado {}!", cooperado.getNome());
		return new org.springframework.security.core.userdetails.User(String.valueOf(cooperado.getMatricula()),cooperado.getPassword(), authorities);
	}
	
	@Override
	public List<Cooperado> todosCooperados() {
		List<Cooperado> cooperado = cooperadoRep.findAll();
		if(cooperado.isEmpty()) throw new NullPointerException("Não encontramos cooperados cadastrados!");
		
		logger.info("Encontramos {} cooperados", cooperado.size());
		return cooperado;
	}

	@Override
	public Cooperado daMatricula(Integer matricula) {
		Cooperado cooperado = cooperadoRep.findByMatricula(matricula);
		if(cooperado == null) throw new NullPointerException("Cooperado não encontrado!");
	    
	    if(cooperado.getDataInativacao() == null) {
	    	cooperado.setAtivo(true);
	    } else {
	    	cooperado.setAtivo(false);
	    	throw new NullPointerException("Cooperado encontra-se inativo, procure sua filial!");
	    };
	    
		logger.info("Cooperado encontrado {}", cooperado.getNome());
		return cooperado;
	}

	@Override
	public Cooperado saveCooperado(Cooperado cooperado) {
		if(cooperado == null) throw new NullPointerException("Todos os campos cooperado são obrigatórios para cadastro!");
		cooperado.setPassword(passwordEncoder.encode(cooperado.getPassword()));
		logger.info("Permissão cadastro novo cooperado negada!");
		throw new NullPointerException("Você não possui permissão para cadastrar novo cooperado!");
	}

	@Override
	public Role saveRole(Role role) {
		if(role == null) throw new NullPointerException("Nome da permissão é obrigatória!");
		
		logger.info("Nova permissão cadastrada {}", role.getName());
		return roleRep.save(role);
	}

	@Override
	public void addRoleToCooperado(Integer matricula, String roleName) {
		if(matricula == null || roleName == null) throw new NullPointerException("Matricula e Permissão são obrigatórios!");
		
		Role role = roleRep.findByName(roleName);
		Cooperado cooperado = cooperadoRep.findByMatricula(matricula);		
		logger.info("Cooperado {} recebeu a permissão {}", cooperado.getNome(), role.getName());
		cooperado.getRoles().add(role);	
	}
}
