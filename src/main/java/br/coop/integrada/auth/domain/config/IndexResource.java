package br.coop.integrada.auth.domain.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin(origins = "*")
public class IndexResource {
	
	@GetMapping
	public String index() {
		return "/swagger/openapi/swagger-ui.html";
	}
}
