package bbcar.controlador;

import java.util.Date;
import java.util.List;

import bbcar.dao.DAOException;
import bbcar.dao.DAOFactoria;
import bbcar.dao.interfaces.CocheDAO;
import bbcar.dao.interfaces.MunicipioDAO;
import bbcar.dao.interfaces.ParadaDAO;
import bbcar.dao.interfaces.ProvinciaDAO;
import bbcar.dao.interfaces.ReservaDAO;
import bbcar.dao.interfaces.UsuarioDAO;
import bbcar.dao.interfaces.ValoracionDAO;
import bbcar.dao.interfaces.ViajeDAO;
import bbcar.modelo.Coche;
import bbcar.modelo.EstadoReserva;
import bbcar.modelo.Municipio;
import bbcar.modelo.Parada;
import bbcar.modelo.Provincia;
import bbcar.modelo.Reserva;
import bbcar.modelo.Usuario;
import bbcar.modelo.Valoracion;
import bbcar.modelo.Viaje;

public class BlaBlaCar {
	
	// CONSTANTES DE ERROR PARA INDICAR A LA VISTA LO QUE FALLA 
	public final static int EXITO = 0;
	
	public final static int ERROR_JPA = -1;
	
	public final static int ERROR_MATRICULA_EXISTENTE = -2;
	
	public final static int ERROR_REGISTRAR_PARADA_ORIGEN = -3;
	public final static int ERROR_REGISTRAR_PARADA_DESTINO = -4;
	
	public final static int ERROR_REGISTRAR_VIAJE = -5;

	public final static int ERROR_NO_EXISTE_USUARIO = -6;
	public final static int ERROR_CLAVE_INCORRECTA = -7;
	
	
	
	private static BlaBlaCar unicaInstancia;

	public static BlaBlaCar getInstancia() throws DAOException {

		if (unicaInstancia == null) {
			unicaInstancia = new BlaBlaCar();
		}

		return unicaInstancia;
	}

	private UsuarioDAO usuarioDAO;
	private CocheDAO cocheDAO;
	private ParadaDAO paradaDAO;
	private ReservaDAO reservaDAO;
	private ValoracionDAO valoracionDAO;
	private ViajeDAO viajeDAO;
	private ProvinciaDAO provinciaDAO;
	private MunicipioDAO municipioDAO;

	private Usuario usuarioActual;

	public Usuario getUsuarioActual() {
		return usuarioActual;
	}

	public void setUsuarioActual(Usuario usuarioActual) {
		this.usuarioActual = usuarioActual;
	}

//	public BlaBlaCar() throws DAOException {
//		this.usuarioDAO = DAOFactoria.getDAOFactoria(DAOFactoria.JPA).getUsuarioDAO();
//		this.cocheDAO = DAOFactoria.getDAOFactoria(DAOFactoria.JPA).getCocheDAO();
//		this.paradaDAO = DAOFactoria.getDAOFactoria(DAOFactoria.JPA).getParadaDAO();
//		this.reservaDAO = DAOFactoria.getDAOFactoria(DAOFactoria.JPA).getReservaDAO();
//		this.valoracionDAO = DAOFactoria.getDAOFactoria(DAOFactoria.JPA).getValoracionDAO();
//		this.viajeDAO = DAOFactoria.getDAOFactoria(DAOFactoria.JPA).getViajeDAO();
//		this.provinciaDAO = DAOFactoria.getDAOFactoria(DAOFactoria.JPA).getProvinciaDAO();
//		this.municipioDAO = DAOFactoria.getDAOFactoria(DAOFactoria.JPA).getMunicipioDAO();
//	}

//	public boolean registrarUsuario(String usuario, String clave, Date fecha_nacimiento, String profesion, String email,
//			String nombre, String apellidos) {
//	}
//
//	
//	public int login(String usuario, String clave) {
//		
//	}
//	
//	public int addCoche(String matricula, String modelo, Integer anyo, Integer confort) {
//		
//	}

	public boolean existeMatricula(String matricula) {
		
		Coche c = this.cocheDAO.findByMatricula(matricula);
		
		if (c == null) {
			return false;
		}
		
		return true;
	}

	public Integer registrarViaje(Integer plazas, Double precio) {

		Coche coche = this.usuarioActual.getCoche();
		Viaje viaje = this.viajeDAO.createViaje(coche , plazas, precio);

		if (viaje == null) {
			return ERROR_REGISTRAR_VIAJE;
		}
		coche.anyadirViaje(viaje);

		return viaje.getId();
	}
	
	public Integer registrarParadaOrigen(Integer idViaje, String ciudad, String calle, Integer numero,
			Integer CP, Date fecha) {

		Municipio origen = this.municipioDAO.findByMunicipio(ciudad);
		Parada parada = paradaDAO.createParadaOrigen(idViaje, origen, calle, numero, CP, fecha);
		
		if (parada == null) {
			return ERROR_REGISTRAR_PARADA_ORIGEN;
		}
		
		Viaje v = this.usuarioActual.getViaje(idViaje);
		v.setOrigen(parada);
		
		return parada.getId();
	}
	
	public Integer registrarParadaDestino(Integer idViaje, String ciudad, String calle, Integer numero,
			Integer CP, Date fecha) {
		
		Municipio destino = this.municipioDAO.findByMunicipio(ciudad);
		Parada parada = paradaDAO.createParadaDestino(idViaje, destino, calle, numero, CP, fecha);
		
		if (parada == null) {
			return ERROR_REGISTRAR_PARADA_DESTINO;
		}

		Viaje v = this.usuarioActual.getViaje(idViaje);
		v.setDestino(parada);
		return parada.getId();
	}

	public Integer reservarViaje(Integer idViaje, String comentario) {
			
		Viaje viaje = viajeDAO.findById(idViaje);
		if (viaje.getPlazasLibres() > 0) {// && !viaje.yaHaReservado(this.usuarioActual.getId())) {
			Reserva reserva = reservaDAO.createReserva(usuarioActual.getId(), idViaje, comentario);
			this.usuarioActual.addReserva(reserva);
			return reserva.getId();
		} else {
			return -1;
		}
	}

	public int aceptarReserva(Reserva reserva) {
		
		Viaje viaje = this.usuarioActual.getViaje(reserva.getIdViaje());

		reserva.setEstado(EstadoReserva.ACEPTADA);
		this.reservaDAO.actualizarEstado(reserva);
		viaje.decrementarPlazas();

		return viaje.getPlazasLibres();
	}


	public void rechazarReserva(Reserva reserva) {
		reserva.setEstado(EstadoReserva.RECHAZADA);
		this.reservaDAO.actualizarEstado(reserva);
	}

	public int valorarUnPasajero(Integer idViaje, Integer idUsuarioPasajero, String comentario, Integer puntuacion) {
			
			Reserva reserva = this.reservaDAO.findByViajeAndUsuario(idViaje, idUsuarioPasajero);
			
			Valoracion nuevaValoracion = this.valoracionDAO.createValoracion(idUsuarioPasajero, this.usuarioActual.getId(), comentario, puntuacion, reserva);
			
			if(nuevaValoracion == null) {
				return ERROR_JPA;
			}

			this.usuarioActual.addValoracionEmitida(nuevaValoracion);
			
			return nuevaValoracion.getId();
	}

	public int valorarUnConductor(Integer idViaje, Integer idUsuarioConductor, String comentario, Integer puntuacion) {

			Reserva reservaUsuario = this.usuarioActual.getReservaByIdViaje(idViaje);

			Valoracion nuevaValoracion = this.valoracionDAO.createValoracion(idUsuarioConductor, this.usuarioActual.getId() , comentario, puntuacion, reservaUsuario);
			
			if (nuevaValoracion == null) {
				return ERROR_JPA;
			}
			
			this.usuarioActual.addValoracionEmitida(nuevaValoracion);
			return nuevaValoracion.getId();

	}
	
	public List<Viaje> buscarViajesLazy(String municipioOrigen, String municipioDestino, Date fechaHora, int start, int max) {
		List<Viaje> aux = this.viajeDAO.buscarViajesLazy(municipioOrigen, municipioDestino, fechaHora, start, max, this.usuarioActual.getId());

		return aux;
	}
	
	public Integer countViajes(String ciudadOrigen, String ciudadDestino, Date fechaHora) {
		Long aux = this.viajeDAO.countViajes(ciudadOrigen, ciudadDestino, fechaHora, this.usuarioActual.getId());
		return aux.intValue();
	}
	
	public Coche getCocheUsuario() {
		return this.usuarioActual.getCoche();
	}

	public boolean isUsuarioLogueado() {
		return this.usuarioActual != null;
	}

	public boolean isCocheRegistrado() {
		return this.usuarioActual.isCocheRegistrado();
	}

	public void actualizarCoche(Integer anyo, String matricula, String modelo, Integer nivelConfort) {
		this.usuarioActual.actualizarCoche(anyo, matricula, modelo, nivelConfort);
		this.cocheDAO.updateCoche(this.usuarioActual.getCoche());
	}

	public List<Provincia> getProvincias() {
		return this.provinciaDAO.findAll();
	}
	
	public List<Municipio> getMunicipios() {
		return this.municipioDAO.findAll();
	}

	public List<String> getMunicipios(Provincia p) {
		return this.municipioDAO.findAll(p);
	}
	
	public List<String> getMunicipios(String nombreProvincia) {
		return this.municipioDAO.findAll(nombreProvincia);
	}

	public void eliminarViaje(Integer idViaje) {
		this.usuarioActual.eliminarViaje(idViaje);
		this.viajeDAO.removeViaje(idViaje);
	}

	public List<Usuario> getPasajeros(Integer idViaje) {
		
		if(idViaje == null) {
			return null;
		}
		
		Viaje v = this.viajeDAO.findById(idViaje);
		return v.getPasajeros();
	}

	public Integer getIdConductor(Integer idViaje) {
		Viaje viaje = this.viajeDAO.findById(idViaje);
		
		return viaje.getIdConductor();
	}

	public List<Viaje> getViajesConductorRealizados() {
		
		return this.usuarioActual.getViajesRealizados();
	}

	public List<Viaje> getViajesConductorPendientes() {
		
		return this.usuarioActual.getViajesPendientes();
	}

	public boolean isValoracionPasajeroHecha(Integer idViaje, Integer idPasajero) {
		
		Reserva reservaPasajero = this.reservaDAO.findByViajeAndUsuario(idViaje, idPasajero);
		
		return this.usuarioActual.isValoracionPasajeroEmitida(reservaPasajero.getId(), idPasajero);
	}

	public List<Viaje> getViajesPasajeroRealizados() {
		return this.viajeDAO.findViajesRealizadosPasajero(this.usuarioActual.getId());
	}

	public List<Viaje> getViajesPasajeroPendientes() {
		return this.viajeDAO.findViajesPendientesPasajero(this.usuarioActual.getId());
	}

	public boolean isValoradoConductor(Integer idViaje, Integer idConductor) {
		
		Reserva reserva = this.usuarioActual.getReservaByIdViaje(idViaje);
		
		return this.usuarioActual.isConductorValorado(idConductor, reserva.getId());
	}

//	public boolean existeUsuario(String usuario) {
//	}

	public boolean isReservaPendiente(Integer idReserva) {
		
		Reserva r = this.reservaDAO.findById(idReserva);
		return r.isPendiente();
	}

	public List<Valoracion> getValoracionesRealizadas() {
		return this.usuarioActual.getValoracionesEmitidas();
	}

	public List<Valoracion> getValoracionesRecibidas() {
		return this.usuarioActual.getValoracionesRecibidas();
	}

	public List<Reserva> getReservas() {
		return this.usuarioActual.getReservas();
	} 

	public void rechazarOtrasReservas(Integer idViaje) {
		
		this.usuarioActual.rechazarOtrasReservas(idViaje);
		
		actualizarReservasRechazadas(idViaje);
	}
	
	private void actualizarReservasRechazadas(Integer idViaje) {
		
		List<Reserva> reservasRechazadas = this.usuarioActual.getReservasRechazadas(idViaje);
		
		for (Reserva r : reservasRechazadas) {
			this.reservaDAO.actualizarEstado(r);
		}
	}
}
