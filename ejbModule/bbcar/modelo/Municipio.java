package bbcar.modelo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Municipio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 764748608125615673L;

	@Id	
	private Integer id;
	@ManyToOne(fetch=FetchType.EAGER)
	private Provincia provincia;
	private String municipio;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Provincia getProvincia() {
		return provincia;
	}
	public void setProvincia(Provincia provincia) {
		this.provincia = provincia;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	
}
