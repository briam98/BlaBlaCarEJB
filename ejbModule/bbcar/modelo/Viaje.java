package bbcar.modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

/**
 * Entity implementation class for Entity: Viaje
 *
 */
@Entity

public class Viaje implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer num_plazas;
	private Integer plazasLibres;

	private Double precio;

	@OneToMany(cascade = { CascadeType.REMOVE }, mappedBy = "viaje", fetch=FetchType.EAGER)
	@OrderBy("estado ASC")
	private List<Reserva> reservas;

	@ManyToOne(fetch=FetchType.EAGER)
	private Coche coche;

	@OneToOne(cascade = { CascadeType.REMOVE })
	private Parada origen;

	@OneToOne(cascade = { CascadeType.REMOVE })
	private Parada destino;

	public Viaje() {
		super();
	}
	
	public Viaje(Integer num_plazas, Double precio, Coche coche) {
		this.num_plazas = num_plazas;
		this.precio = precio;
		this.coche = coche;
		this.plazasLibres = num_plazas;
	}

	// Métodos útiles
	
	
	public Date getFechaFinal() {
		return this.destino.getFecha_hora();
	}

	public boolean isPediente() {
		
		for (Reserva r: this.reservas) {
			if (r.getEstado().equals(EstadoReserva.PENDIENTE)) {
				return true;
			}
		}
		return false;
	}

	public Integer getCocheId() {
		if (this.coche != null) {
			return this.coche.getId();			
		} else {
			return -1;
		}
	}

	public List<Usuario> getPasajeros() {
		
		LinkedList<Usuario> pasajeros = new LinkedList<Usuario>();
		
		for(Reserva r: this.reservas) {
			if(r.isAceptada()) {
				pasajeros.add(r.getUsuario());
			}
		}
		
		return pasajeros;
	}

	public Integer getIdConductor() {
		return this.coche.getIdConductor();
	}

	public boolean isPlazasDisponibles() {
		return this.plazasLibres > 0;
	}

	public void decrementarPlazas() {
		
		this.plazasLibres--;
	}

	public void rechazarOtrasReservas() {
		for (Reserva r : this.reservas) {
			if (r.isPendiente()) {
				r.rechazarReserva();
			}
		}
	}
	
	//GETTERS y SETTERS
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNum_plazas() {
		return num_plazas;
	}

	public void setNum_plazas(Integer num_plazas) {
		this.num_plazas = num_plazas;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	public List<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}
	
	public void addReserva(Reserva r) {
		reservas.add(r);
	}

	public Coche getCoche() {
		return coche;
	}

	public void setCoche(Coche coche) {
		this.coche = coche;
	}

	public Parada getOrigen() {
		return origen;
	}

	public void setOrigen(Parada origen) {
		this.origen = origen;
	}

	public Parada getDestino() {
		return destino;
	}

	public void setDestino(Parada destino) {
		this.destino = destino;
	}
	
	public int getPlazasLibres() {
		return plazasLibres;
	}

	public boolean isRealizado() {
		return this.destino.getFecha_hora().before(new Date());
	}

	public boolean isPendiente() {
		return this.origen.getFecha_hora().after(new Date());
	}

	public List<Reserva> getReservasRechazadas() {
		
		LinkedList<Reserva> reservasRechazadas = new LinkedList<Reserva>();
		
		for (Reserva r : this.reservas) {
			if (r.isRechazada()) {
				reservasRechazadas.add(r);
			}
		}
		return reservasRechazadas;
	}

	public Reserva getReservaById(Integer idReserva) {
		Reserva r = null;
		for (Reserva re : this.reservas) {
			if (re.getId().equals(idReserva)) {
				r = re;
			}
		}
		
		return r;
	}


}
