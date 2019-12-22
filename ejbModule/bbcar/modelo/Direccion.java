package bbcar.modelo;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Direccion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6676856992368694285L;
	private String calle;
	private Integer numero;
	private Integer CP;
	
	public Direccion() {
		super();
	}
	
	public Direccion(String calle, Integer numero, Integer CP) {
		this.calle = calle;
		this.numero = numero;
		this.CP = CP;
	}

	public String getCalle() {
		return calle;
	}
	
	public void setCalle(String calle) {
		this.calle = calle;
	}
	
	public Integer getNumero() {
		return numero;
	}
	
	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
	public Integer getCP() {
		return CP;
	}
	
	public void setCP(Integer cP) {
		CP = cP;
	}
	
}
