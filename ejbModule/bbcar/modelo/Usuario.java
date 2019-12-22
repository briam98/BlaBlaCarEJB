package bbcar.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity implementation class for Entity: Usuario
 *
 */
@Entity
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String usuario;
	private String clave;
	
	@Temporal(TemporalType.DATE)
	private Date fecha_nacimiento;
	
	private String profesion;
	private String email;
	private String nombre;
	private String apellidos;
	
	@OneToOne
	private Coche coche;
	
	@OneToMany(mappedBy="usuarioValorado", fetch=FetchType.EAGER)
	private List<Valoracion> valoracionesRecibidas;
	
	@OneToMany(mappedBy="usuarioEmisor", fetch=FetchType.EAGER)
	private List<Valoracion> valoracionesEmitidas;
	
	@OneToMany(mappedBy="usuario", fetch=FetchType.EAGER)
	private List<Reserva> reservas;
	
	public Usuario() {
		super();
	}
	
	public Usuario(String nombre, String apellidos, String usuario, String clave,
			Date fecha_nacimiento, String profesion, String email) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.usuario = usuario;
		this.clave = clave;
		this.fecha_nacimiento = fecha_nacimiento;
		this.profesion = profesion;
		this.email = email;
		this.valoracionesRecibidas = new LinkedList<Valoracion>();
		this.valoracionesEmitidas = new LinkedList<Valoracion>();
		this.reservas = new LinkedList<Reserva>();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Date getFecha_nacimiento() {
		return fecha_nacimiento;
	}

	public void setFecha_nacimiento(Date fecha_nacimiento) {
		this.fecha_nacimiento = fecha_nacimiento;
	}

	public String getProfesion() {
		return profesion;
	}

	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public Coche getCoche() {
		return coche;
	}

	public void setCoche(Coche coche) {
		this.coche = coche;
	}

	public List<Valoracion> getValoracionesRecibidas() {
		return valoracionesRecibidas;
	}

	public void setValoracionesRecibidas(List<Valoracion> valoracionesRecibidas) {
		this.valoracionesRecibidas = valoracionesRecibidas;
	}

	public List<Valoracion> getValoracionesEmitidas() {
		return valoracionesEmitidas;
	}

	public void setValoracionesEmitidas(List<Valoracion> valoracionesEmitidas) {
		this.valoracionesEmitidas = valoracionesEmitidas;
	}

	public List<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}
	
	public Reserva getReservaId(Integer idReserva) {
		
		for (Reserva r : this.reservas) {
			if (r.getId().equals(idReserva)) 
				return r;
		}
		
		return null;
	}
	
	//Metodos utiles
	
	public boolean isClaveCorrecta(String clave) {
		return this.clave.equals(clave);
	}
	
	public boolean isConductor() {
		return this.coche != null;
	}
	
	public void addReserva(Reserva reserva) {
		this.reservas.add(reserva);
	}

	public void addValoracionEmitida(Valoracion valoracion) {
		this.valoracionesEmitidas.add(valoracion);
	}
	
	public void addValoracionRecibida(Valoracion valoracion) {
		this.valoracionesRecibidas.add(valoracion);
	}
	
	public List<Viaje> getListViajesPendientes() {
		return this.coche.getListaViajesPendientes();
	}

	public List<Viaje> getViajesReservados() {

		ArrayList<Viaje> listadoViajes = new ArrayList<Viaje>();
		for(Reserva r: this.reservas) {
			listadoViajes.add(r.getViaje());
		}
		
		return listadoViajes;
	}

	public List<Reserva> getReservasByViaje(Integer idViaje) {
		return this.coche.getReservasByViaje(idViaje);
	}

	public boolean isCocheRegistrado() {
		return this.coche != null;
	}

	public void actualizarCoche(Integer anyo, String matricula, String modelo, Integer nivelConfort) {
		this.coche.actualizar(anyo, matricula, modelo, nivelConfort);
		
	}

	public Integer getCocheId() {

		if (this.coche == null) {
			return -1;
		}
		
		return this.coche.getId();
	}

	public Reserva getReservaByIdViaje(Integer idViaje) {
		
		for (Reserva r: this.reservas) {
			if (r.getViaje().getId().equals(idViaje)) {
				return r;
			}
		}
		return null;
	}

	public boolean isValoracionEmitida(Reserva reserva) {
		
		for(Valoracion v: this.valoracionesEmitidas) {
			if (v.getReserva().getId().equals(reserva.getId())) {
				return true;
			}
		}
		return false;
	}

	public boolean isValoracionPasajeroEmitida(Integer idReserva, Integer idPasajero) {
		
		for (Valoracion v: this.valoracionesEmitidas) {
			if (v.isReservaEquals(idReserva) && v.isEmisorEquasl(this.id) && v.isUsuarioValoradoEquals(idPasajero)) {
				return true;
			}
		}
		
		return false;
	}

	public boolean isConductorValorado(Integer idConductor, Integer idReserva) {
		for (Valoracion v: this.valoracionesEmitidas) {
			if (v.isReservaEquals(idReserva) && v.isEmisorEquasl(this.id) && v.isUsuarioValoradoEquals(idConductor)) {
				return true;
			}
		}
		
		return false;
	}

	public boolean isPlazasLibres(Integer idViaje) {
		return this.coche.isPlazasLibres(idViaje);
	}

	public Viaje getViaje(Integer idViaje) {
		return this.coche.getViaje(idViaje);
	}

	public void rechazarOtrasReservas(Integer idViaje) {
		
		this.coche.rechazarOtrasReservas(idViaje);
	}

	public List<Viaje> getViajesRealizados() {
		return this.coche.getViajesRealizados();
	}

	public List<Viaje> getViajesPendientes() {
		return this.coche.getViajesPendientes();
	}

	public List<Reserva> getReservasRechazadas(Integer idViaje) {
		
		return this.coche.getReservasRechazadas(idViaje);
	}

	public void eliminarViaje(Integer idViaje) {
		
		this.coche.eliminarViaje(idViaje);
	}
}
