package br.coop.integrada.auth.aplication.resource;

import lombok.Data;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import br.coop.integrada.auth.AuthApplication;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import br.coop.integrada.auth.domain.modelo.Role;
import br.coop.integrada.auth.domain.filter.CustomAuthenticationFilter;
import br.coop.integrada.auth.domain.modelo.Cooperado;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.coop.integrada.auth.domain.service.CooperadoService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cooperados/auth/v1")
@Tag(name = "Cooperado", description = "Dados do cooperado.")
public class CooperadoResource {
	private static final Logger logger = LoggerFactory.getLogger(AuthApplication.class);
	private final CooperadoService cooperadoService;
	

	@GetMapping("/cooperado/{matricula}")
	public ResponseEntity<Cooperado> daMatricula(@PathVariable(name = "matricula") Integer matricula){		
		logger.info("Busca de cooperado solicitada:");
		return ResponseEntity.ok(cooperadoService.daMatricula(matricula));
	}	

	@PostMapping("/cooperado")
	public ResponseEntity<Cooperado> saveCooperado(@RequestBody Cooperado cooperado){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/cooperados/auth/v1/cooperado/novo").toUriString());
		logger.info("Novo cadastro solicidado:");
		return ResponseEntity.created(uri).body(cooperadoService.saveCooperado(cooperado));
	}	

	@PostMapping("/permissao")
	public ResponseEntity<Role> saveRole(@RequestBody Role role){
		URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/cooperados/auth/v1/permissao/novo").toUriString());
		logger.info("Novo cadastro de permissão solicidado:");
		return ResponseEntity.created(uri).body(cooperadoService.saveRole(role));
	}	

	@PostMapping("/cooperado-permitir")
	public ResponseEntity<?> permissaoToCooperado(@RequestBody RoleToCooperado form){
		cooperadoService.addRoleToCooperado(form.getMatricula(), form.getRoleName());
		logger.info("Nova permissão adicionada:");
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/refreshToken")
	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws StreamWriteException, DatabindException, IOException{	
		String authorizationHeader = request.getHeader(AUTHORIZATION);
		
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			try {
				String refreshToken = authorizationHeader.substring("Bearer ".length());
				Algorithm algorithm = Algorithm.HMAC512(CustomAuthenticationFilter.secret_token);
				JWTVerifier verifier = JWT.require(algorithm).build();
				DecodedJWT decodeJWT = verifier.verify(refreshToken);
				String username = decodeJWT.getSubject();
				Cooperado cooperado = cooperadoService.daMatricula(Integer.parseInt(username));
				
				String accessToken = JWT.create()
						.withSubject(cooperado.getMatricula().toString())
						.withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
						.withIssuer(request.getRequestURL().toString())
						.withClaim("roles", cooperado.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
						.sign(algorithm);
				
				Map<String, String> tokens = new HashMap<>();		
				tokens.put("acessToken", accessToken);
				tokens.put("refreshToken", refreshToken);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), tokens);
				
			}catch( Exception e ) {
				response.setHeader("Erro ",e.getMessage());
				response.setStatus(HttpStatus.FORBIDDEN.value());
				
				Map<String, String> error = new HashMap<>();		
				error.put("error_message", e.getMessage());
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				new ObjectMapper().writeValue(response.getOutputStream(), error);
			}
		}else {
			throw new RuntimeException("Seu Token precisa de atualização!");
		}
		logger.info("Busca de cooperado solicitada:");
	}
}

@Data
class RoleToCooperado {
	private Integer matricula;
	private String roleName;	
} 