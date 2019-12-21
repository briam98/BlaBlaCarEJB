package bbcar.controlador;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import bbcar.dao.DAOException;
import bbcar.dao.DAOFactoria;
import bbcar.dao.interfaces.DAOFactoriaLocal;
import bbcar.modelo.Coche;
import bbcar.modelo.EstadoReserva;
import bbcar.modelo.Municipio;
import bbcar.modelo.Parada;
import bbcar.modelo.Provincia;
import bbcar.modelo.Reserva;
import bbcar.modelo.Usuario;
import bbcar.modelo.Valoracion;
import bbcar.modelo.Viaje;

@Stateful(name = "BlaBlaCarRemoto")
public class BlaBlaCarEJB implements BlaBlaCarRemote {
	
	@EJB(beanName="Factoria")
	private DAOFactoriaLocal factoria;
	
	private Usuario usuarioActual;

	// Dice que es un ejemplo, alomejor se puede quitar
	@Resource
	private SessionContext context;

	@Override
	public DAOFactoriaLocal getFactoria() {
		return factoria;
	}

	@PostConstruct
	public void configurarBlaBlaCarEJB() {
		try {
			factoria.setDAOFactoria(DAOFactoria.JPA);
		} catch(DAOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int login(String usuario, String clave) {
		Usuario u = this.factoria.getUsuarioDAO().findByUsuario(usuario);
		
		MessageDigest md;
		String encodedClave = null;
		try {
			md = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
			md.update(clave.getBytes());
			byte[] digest = md.digest();
			byte[] encoded = Base64.encodeBase64(digest);
			
			encodedClave = new String(encoded);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		if (!u.isClaveCorrecta(encodedClave)) {
			return -1;
		}

		this.usuarioActual = u;
		return u.getId();
	}

	@Override
	public boolean registrarUsuario(String usuario, String clave, Date fecha_nacimiento, String profesion, String email,
			String nombre, String apellidos) {
		
		MessageDigest md;
		String encodedClave = null;
		try {
			md = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
			md.update(clave.getBytes());
			byte[] digest = md.digest();
			byte[] encoded = Base64.encodeBase64(digest);
			
			encodedClave = new String(encoded);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	      

		Usuario user = this.factoria.getUsuarioDAO().createUsuario(usuario, encodedClave, fecha_nacimiento, profesion, email, nombre,
				apellidos);

		if (user == null) {
			return false;
		}

		return true;
	}

	@Override
	public int addCoche(String matricula, String modelo, Integer anyo, Integer confort) {
		Coche coche = this.factoria.getCocheDAO().createCoche(this.usuarioActual.getId(), matricula, modelo, anyo, confort);

		if (coche == null) {
			return -1;
		} 
		
		this.usuarioActual = this.factoria.getUsuarioDAO().findById(this.usuarioActual.getId());
		
		return coche.getId();
	}

	@Override
	public Integer registrarViaje(Integer plazas, Double precio) {
		Coche coche = this.usuarioActual.getCoche();
		Viaje viaje = this.factoria.getViajeDAO().createViaje(coche , plazas, precio);

		if (viaje == null) {
			return -1;
		}
		coche.anyadirViaje(viaje);

		return viaje.getId();
	}

	@Override
	public Integer registrarParadaOrigen(Integer idViaje, String ciudad, String calle, Integer numero, Integer CP,
			Date fecha) {
		Municipio origen = this.factoria.getMunicipioDAO().findByMunicipio(ciudad);
		Parada parada = this.factoria.getParadaDAO().createParadaOrigen(idViaje, origen, calle, numero, CP, fecha);
		
		if (parada == null) {
			return -1;
		}
		
		Viaje v = this.usuarioActual.getViaje(idViaje);
		v.setOrigen(parada);
		
		return parada.getId();
	}

	@Override
	public Integer registrarParadaDestino(Integer idViaje, String ciudad, String calle, Integer numero, Integer CP,
			Date fecha) {
		Municipio destino = this.factoria.getMunicipioDAO().findByMunicipio(ciudad);
		Parada parada = this.factoria.getParadaDAO().createParadaDestino(idViaje, destino, calle, numero, CP, fecha);
		
		if (parada == null) {
			return -1;
		}

		Viaje v = this.usuarioActual.getViaje(idViaje);
		v.setDestino(parada);
		return parada.getId();
	}

	@Override
	public Integer reservarViaje(Integer idViaje, String comentario) {
		Viaje viaje = this.factoria.getViajeDAO().findById(idViaje);
		if (viaje.getPlazasLibres() > 0) {// && !viaje.yaHaReservado(this.usuarioActual.getId())) {
			Reserva reserva = this.factoria.getReservaDAO().createReserva(usuarioActual.getId(), idViaje, comentario);
			this.usuarioActual.addReserva(reserva);
			return reserva.getId();
		} else {
			return -1;
		}
	}

	@Override
	public int aceptarReserva(Reserva reserva) {
		Viaje viaje = this.usuarioActual.getViaje(reserva.getIdViaje());

		reserva.setEstado(EstadoReserva.ACEPTADA);
		this.factoria.getReservaDAO().actualizarEstado(reserva);
		viaje.decrementarPlazas();

		return viaje.getPlazasLibres();
	}

	@Override
	public void rechazarReserva(Reserva reserva) {
		reserva.setEstado(EstadoReserva.RECHAZADA);
		this.factoria.getReservaDAO().actualizarEstado(reserva);
	}

	@Override
	public int valorarUnPasajero(Integer idViaje, Integer idUsuarioPasajero, String comentario, Integer puntuacion) {
		Reserva reserva =this.factoria.getReservaDAO().findByViajeAndUsuario(idViaje, idUsuarioPasajero);
		
		Valoracion nuevaValoracion = this.factoria.getValoracionDAO().createValoracion(idUsuarioPasajero, this.usuarioActual.getId(), comentario, puntuacion, reserva);
		
		if(nuevaValoracion == null) {
			return -1;
		}

		this.usuarioActual.addValoracionEmitida(nuevaValoracion);
		
		return nuevaValoracion.getId();
	}

	@Override
	public int valorarUnConductor(Integer idViaje, Integer idUsuarioConductor, String comentario, Integer puntuacion) {
		Reserva reservaUsuario = this.usuarioActual.getReservaByIdViaje(idViaje);

		Valoracion nuevaValoracion = this.factoria.getValoracionDAO().createValoracion(idUsuarioConductor, this.usuarioActual.getId() , comentario, puntuacion, reservaUsuario);
		
		if (nuevaValoracion == null) {
			return -1;
		}
		
		this.usuarioActual.addValoracionEmitida(nuevaValoracion);
		return nuevaValoracion.getId();
	}

	@Override
	public List<Viaje> buscarViajesLazy(String municipioOrigen, String municipioDestino, Date fechaHora, int start,
			int max) {
		List<Viaje> aux = this.factoria.getViajeDAO().buscarViajesLazy(municipioOrigen, municipioDestino, fechaHora, start, max, this.usuarioActual.getId());
		return aux;
	}

	@Override
	public Integer countViajes(String ciudadOrigen, String ciudadDestino, Date fechaHora) {
		Long aux = this.factoria.getViajeDAO().countViajes(ciudadOrigen, ciudadDestino, fechaHora, this.usuarioActual.getId());
		return aux.intValue();
	}

	@Override
	public Coche getCocheUsuario() {
		return this.usuarioActual.getCoche();
	}

	@Override
	public boolean isCocheRegistrado() {
		return this.usuarioActual != null;
	}

	@Override
	public void actualizarCoche(Integer anyo, String matricula, String modelo, Integer nivelConfort) {
		this.usuarioActual.actualizarCoche(anyo, matricula, modelo, nivelConfort);
		this.factoria.getCocheDAO().updateCoche(this.usuarioActual.getCoche());
	}

	@Override
	public List<Provincia> getProvincias() {
		return this.factoria.getProvinciaDAO().findAll();
	}

	@Override
	public List<Municipio> getMunicipios() {
		return this.factoria.getMunicipioDAO().findAll();
	}

	@Override
	public List<String> getMunicipios(Provincia p) {
		return this.factoria.getMunicipioDAO().findAll(p);
	}

	@Override
	public List<String> getMunicipios(String nombreProvincia) {
		return this.factoria.getMunicipioDAO().findAll(nombreProvincia);
	}

	@Override
	public void eliminarViaje(Integer idViaje) {
		this.usuarioActual.eliminarViaje(idViaje);
		this.factoria.getViajeDAO().removeViaje(idViaje);
	}

	@Override
	public List<Usuario> getPasajeros(Integer idViaje) {
		if(idViaje == null) {
			return null;
		}
		
		Viaje v = this.factoria.getViajeDAO().findById(idViaje);
		return v.getPasajeros();
	}

	@Override
	public Integer getIdConductor(Integer idViaje) {
		Viaje viaje = this.factoria.getViajeDAO().findById(idViaje);
		return viaje.getIdConductor();
	}

	@Override
	public List<Viaje> getViajesConductorRealizados() {
		return this.usuarioActual.getViajesRealizados();
	}

	@Override
	public List<Viaje> getViajesConductorPendientes() {
		return this.usuarioActual.getViajesPendientes();
	}

	@Override
	public boolean isValoracionPasajeroHecha(Integer idViaje, Integer idPasajero) {
		Reserva reservaPasajero = this.factoria.getReservaDAO().findByViajeAndUsuario(idViaje, idPasajero);
		return this.usuarioActual.isValoracionPasajeroEmitida(reservaPasajero.getId(), idPasajero);
	}

	@Override
	public List<Viaje> getViajesPasajeroRealizados() {
		return this.factoria.getViajeDAO().findViajesRealizadosPasajero(this.usuarioActual.getId());
	}

	@Override
	public List<Viaje> getViajesPasajeroPendientes() {
		return this.factoria.getViajeDAO().findViajesPendientesPasajero(this.usuarioActual.getId());
	}

	@Override
	public boolean isValoradoConductor(Integer idViaje, Integer idConductor) {
		Reserva reserva = this.usuarioActual.getReservaByIdViaje(idViaje);
		return this.usuarioActual.isConductorValorado(idConductor, reserva.getId());
	}

	@Override
	public boolean existeUsuario(String usuario) {
		
		Usuario u = this.factoria.getUsuarioDAO().findByUsuario(usuario);

		if (u == null) {
			return false;
		}

		return true;
	}

	@Override
	public boolean isReservaPendiente(Integer idReserva) {
		Reserva r = this.factoria.getReservaDAO().findById(idReserva);
		return r.isPendiente();
	}

	@Override
	public List<Valoracion> getValoracionesRealizadas() {
		return this.usuarioActual.getValoracionesEmitidas();
	}

	@Override
	public List<Valoracion> getValoracionesRecibidas() {
		return this.usuarioActual.getValoracionesRecibidas();
	}

	@Override
	public List<Reserva> getReservas() {
		return this.usuarioActual.getReservas();
	}

	@Override
	public void rechazarOtrasReservas(Integer idViaje) {
		this.usuarioActual.rechazarOtrasReservas(idViaje);
		actualizarReservasRechazadas(idViaje);
	}
	
	private void actualizarReservasRechazadas(Integer idViaje) {

		List<Reserva> reservasRechazadas = this.usuarioActual.getReservasRechazadas(idViaje);

		for (Reserva r : reservasRechazadas) {
			this.factoria.getReservaDAO().actualizarEstado(r);
		}
	}

	@Override
	public boolean existeMatricula(String matricula) {
		Coche c = this.factoria.getCocheDAO().findByMatricula(matricula);
		
		if (c == null) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean isUsuarioLogueado() {
		return this.usuarioActual != null;
	}

}