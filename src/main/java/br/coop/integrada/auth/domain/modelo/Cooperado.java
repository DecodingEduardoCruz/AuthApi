package br.coop.integrada.auth.domain.modelo;

import lombok.Data;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity @Data @NoArgsConstructor @AllArgsConstructor
@Table(schema = "portal_cooperado", name = "cooperado")
public class Cooperado {
	
	@Id 
	@Column(name = "matricula")
	private Integer matricula;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "api_password")
    private String password;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Column(name = "senha_sha1")
    private String senhaSha1;	
	
	private String nome;

    private String email;

    private String celular;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "data_inativacao")
    private Date dataInativacao;
    
    @Transient
    private Boolean ativo;  
    
    @JoinTable(schema = "portal_cooperado", name = "cooperado_rc")
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();
}
