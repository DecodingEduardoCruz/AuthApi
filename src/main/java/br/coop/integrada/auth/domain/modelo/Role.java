package br.coop.integrada.auth.domain.modelo;

import lombok.Data;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
@Table(schema = "portal_cooperado", name = "cooperado_roles")
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String name;
}
