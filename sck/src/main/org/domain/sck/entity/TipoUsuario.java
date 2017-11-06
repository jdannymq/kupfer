package org.domain.sck.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.domain.sck.entity.enums.EstadoEntityType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "tipoUsuario")
public class TipoUsuario {

	private Long idTipo;
	private String descripcion;
	private String abreviado;
	private EstadoEntityType estado;

	public TipoUsuario() {
	}

	public TipoUsuario(long idTipo, String descripcion, EstadoEntityType estado,String abreviado) {
		this.idTipo = idTipo;
		this.descripcion = descripcion;
		this.estado = estado;
		this.abreviado = abreviado;
	}

	@Id
	@GenericGenerator(name = "tipoUsuarioSequenceGenerator", strategy = "org.hibernate.id.enhanced.TableGenerator", parameters = {
			@Parameter(name = "table_name", value = "hibernate_sequences"),
			@Parameter(name = "segment_value", value = "SEQ_TIPO_USUARIO"),
			@Parameter(name = "initial_value", value = "1"),
			@Parameter(name = "increment_size", value = "1") })

	@GeneratedValue(generator = "tipoUsuarioSequenceGenerator")
	@Column(name = "idTipo", unique = true, nullable = false, precision = 18, scale = 0)
	public Long getIdTipo() {
		return this.idTipo;
	}

	public void setIdTipo(long idTipo) {
		this.idTipo = idTipo;
	}

	@Column(name = "descripcion", nullable = false)
	@NotNull
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	@Column(name = "abreviado", nullable = false)
	@NotNull
	public String getAbreviado() {
		return abreviado;
	}

	public void setAbreviado(String abreviado) {
		this.abreviado = abreviado;
	}

	@Column(name = "estado", nullable = false)
	public EstadoEntityType getEstado() {
		return this.estado;
	}

	public void setEstado(EstadoEntityType estado) {
		this.estado = estado;
	}

}