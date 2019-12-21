package bbcar.modelo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * Entity implementation class for Entity: Reserva
 *
 */
@Entity

public class Reserva implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String comentario;

	@ManyToOne
	private Viaje viaje;
	
	@ManyToOne
	private Usuario usuario;

	@Enumerated(EnumType.STRING)
	private EstadoReserva estado;

	@OneToMany(mappedBy="reserva")
	private List<Valoracion> valoraciones;

	public Reserva() {
		super();
	}
	
	public Reserva(String comentario, Viaje viaje, Usuario usuario) {
		this.comentario = comentario;
		this.viaje = viaje;
		this.usuario = usuario;
		this.estado = EstadoReserva.PENDIENTE;
		this.valoraciones = new LinkedList<Valoracion>();
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

	public Viaje getViaje() {
		return viaje;
	}

	public void setViaje(Viaje viaje) {
		this.viaje = viaje;
	}

	public EstadoReserva getEstado() {
		return estado;
	}

	public void setEstado(EstadoReserva estado) {
		this.estado = estado;
	}
	
	public void aceptarReserva() {
		this.setEstado(EstadoReserva.ACEPTADA);
	}
	
	public void rechazarReserva() {
		this.setEstado(EstadoReserva.RECHAZADA);
	}

	public List<Valoracion> getValoraciones() {
		return valoraciones;
	}

	public void setValoraciones(List<Valoracion> valoraciones) {
		this.valoraciones = valoraciones;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isAceptada() {
		return this.estado.equals(EstadoReserva.ACEPTADA);
	}

	public int getUsuarioId() {
		return this.usuario.getId();

	}

	public boolean isPendiente() {
		return this.estado.equals(EstadoReserva.PENDIENTE);
	}

	public Integer getIdViaje() {
		return this.viaje.getId();
	}

	public boolean isRechazada() {
		return this.estado.equals(EstadoReserva.RECHAZADA);
	}
}
