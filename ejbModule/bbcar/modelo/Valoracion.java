package bbcar.modelo;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Valoracion
 *
 */
@Entity

public class Valoracion implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String comentario;
	private Integer puntuacion;
	
	@ManyToOne
	private Usuario usuarioEmisor;
	
	@ManyToOne
	private Usuario usuarioValorado;
	
	@ManyToOne
	private Reserva reserva;
		
	public Valoracion() {
		super();
	}
	
	public Valoracion(String comentario, Integer puntuacion) {
		this.comentario = comentario;
		this.puntuacion = puntuacion;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Integer getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(Integer puntuacion) {
		this.puntuacion = puntuacion;
	}

	public Reserva getReserva() {
		return reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

	public Usuario getUsuarioEmisor() {
		return usuarioEmisor;
	}

	public void setUsuarioEmisor(Usuario usuarioEmisor) {
		this.usuarioEmisor = usuarioEmisor;
	}

	public Usuario getUsuarioValorado() {
		return usuarioValorado;
	}

	public void setUsuarioValorado(Usuario usuarioRecibidor) {
		this.usuarioValorado = usuarioRecibidor;
	}

	public boolean isReservaEquals(Integer idReserva) {
		return this.reserva.getId().equals(idReserva);
	}

	public boolean isEmisorEquasl(Integer idConductor) {
		return this.usuarioEmisor.getId().equals(idConductor);
	}

	public boolean isUsuarioValoradoEquals(Integer idPasajero) {
		return this.usuarioValorado.getId().equals(idPasajero);
	}
   
}
