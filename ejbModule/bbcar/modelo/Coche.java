package bbcar.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Entity implementation class for Entity: Coche
 *
 */
@Entity
public class Coche implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String matricula;
	private String modelo;
	private Integer confort;
	private Integer anyo;
	
	@OneToOne(mappedBy="coche")
	private Usuario usuario;
	
	@OneToMany(mappedBy="coche", fetch=FetchType.EAGER)
	private List<Viaje> listaViajes;

	public Coche() {
		super();
	}
	
	public Coche(String matricula, String modelo, Integer confort, Integer anyo, Usuario usuario) {
		this.matricula = matricula;
		this.modelo = modelo;
		this.confort = confort;
		this.anyo = anyo;
		this.usuario = usuario;
		this.listaViajes = new LinkedList<Viaje>();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Integer getConfort() {
		return confort;
	}

	public void setConfort(Integer confort) {
		this.confort = confort;
	}

	public Integer getAnyo() {
		return anyo;
	}

	public void setAnyo(Integer anyo) {
		this.anyo = anyo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Viaje> getListaViajes() {
		return listaViajes;
	}

	public void setListaViajes(List<Viaje> listaViajes) {
		this.listaViajes = listaViajes;
	}
   
	//Métodos útiles

	public void anyadirViaje(Viaje viaje) {
		this.listaViajes.add(viaje);
	}
	
	public List<Viaje> getListaViajesPendientes() {
		ArrayList<Viaje> viajesPendientes = new ArrayList<Viaje>();
		
		for (Viaje v: this.listaViajes) {
			if(v.isPediente()) {
				viajesPendientes.add(v);
			}
		}
		
		return viajesPendientes;
	}

	public List<Reserva> getReservasByViaje(Integer idViaje) {
		Viaje viaje = null;
		for (Viaje v: this.listaViajes) {
			if(v.getId() == idViaje) {
				viaje = v;
			}
		}
		
		if (viaje != null) {
			return new ArrayList<Reserva>(viaje.getReservas());
		}
		
		return new ArrayList<Reserva>();
		
	}

	public void actualizar(Integer anyo, String matricula, String modelo, Integer nivelConfort) {
		this.anyo = anyo; 
		this.matricula = matricula;
		this.modelo = modelo;
		this.confort = nivelConfort;
	}

	public Integer getIdConductor() {
		return this.usuario.getId();
	}

	public boolean isPlazasLibres(Integer idViaje) {
		
		for (Viaje v: this.listaViajes) {
			if (v.getId().equals(idViaje)) {
				return v.isPlazasDisponibles();
			}
		}
		return false;
	}

	public Viaje getViaje(Integer idViaje) {
		
		for (Viaje v: this.listaViajes) {
			if (v.getId().equals(idViaje)) {
				return v;
			}
		}
		
		return null;
	}

	public void rechazarOtrasReservas(Integer idViaje) {

		for (Viaje v : this.listaViajes) {
			if (v.getId().equals(idViaje)) {
				v.rechazarOtrasReservas();
			}
		}
	}

	public List<Viaje> getViajesRealizados() {
		
		LinkedList<Viaje> viajesRealizados = new LinkedList<Viaje>(); 
		
		for (Viaje v: this.listaViajes) {
			if (v.isRealizado()) {
				viajesRealizados.add(v);
			}
		}
		
		return viajesRealizados;
	}

	public List<Viaje> getViajesPendientes() {

		LinkedList<Viaje> viajesPendientes = new LinkedList<Viaje>(); 

		for (Viaje v: this.listaViajes) {
			if (v.isPendiente()) {
				viajesPendientes.add(v);
			}
		}

		return viajesPendientes;
	}

	public List<Reserva> getReservasRechazadas(Integer idViaje) {
		
		for (Viaje v : this.listaViajes) {
			if (v.getId().equals(idViaje)) {
				return v.getReservasRechazadas();
			}
		}
		
		return null;
	}

	public void eliminarViaje(Integer idViaje) {
		Iterator<Viaje> it= this.listaViajes.iterator();
		while(it.hasNext()) {
			Viaje v = it.next();
			if (v.getId().equals(idViaje)) {
				it.remove();
			}
		}
	}
}
