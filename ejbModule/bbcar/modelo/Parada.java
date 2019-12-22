package bbcar.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for Entity: Parada
 *
 */
@Entity

public class Parada implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Municipio ciudad;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha_hora;
	
	@Embedded
	private Direccion direccion;
	
	public Parada() {
		super();
	}

	public Parada(Municipio ciudad, Date fecha, Direccion direccion) {
		this.ciudad = ciudad;
		this.fecha_hora = fecha;
		this.direccion = direccion;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Municipio getCiudad() {
		return ciudad;
	}

	public void setCiudad(Municipio ciudad) {
		this.ciudad = ciudad;
	}

	public Date getFecha_hora() {
		return fecha_hora;
	}

	public void setFecha_hora(Date fecha_hora) {
		this.fecha_hora = fecha_hora;
	}

	public Direccion getDireccion() {
		return direccion;
	}

	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}
   
}
