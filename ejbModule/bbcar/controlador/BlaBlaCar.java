package bbcar.controlador;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import bbcar.dao.DAOException;
import bbcar.dao.DAOFactoria;
import bbcar.dao.interfaces.*;
import bbcar.modelo.*;

@Stateless(name="BlaBlaCarRemoto")
public class BlaBlaCar implements BlaBlaCarRemote {
	
	@EJB(beanName="Contador")
	private ContadorEJB contador;
	
	@EJB(beanName="Factoria")
	private DAOFactoriaLocal factoria;
		
	@Resource
	private SessionContext context;
	
	public DAOFactoriaLocal getFactoria() {
		return factoria;
	}
	
	@PostConstruct
	public void configurarBlaBlaCarEJB() {
		try {
			factoria.setDAOFactoria(DAOFactoria.JPA);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		this.usuarioDAO = factoria.getUsuarioDAO();
		this.cocheDAO = factoria.getCocheDAO();
		this.paradaDAO = factoria.getParadaDAO();
		this.reservaDAO = factoria.getReservaDAO();
		this.valoracionDAO = factoria.getValoracionDAO();
		this.viajeDAO = factoria.getViajeDAO();
		this.provinciaDAO = factoria.getProvinciaDAO();
		this.municipioDAO = factoria.getMunicipioDAO();
	}
	
	// CONSTANTES DE ERROR
	public final static int EXITO = 0;
	public final static int ERROR_JPA = 1;
	public final static int ERROR_MATRICULA_EXISTENTE = 2;

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

	public boolean registrarUsuario(String usuario, String clave, Date fecha_nacimiento, String profesion, String email,
			String nombre, String apellidos) {
		
		//TODO Estas comprobaciones las debemos de llevar en la parte de la vista
		Usuario u = this.usuarioDAO.findByUsuario(usuario);

		if(u != null) {
			return false;
		}

		// Solo deberiamos hacer el create
		Usuario user = this.usuarioDAO.createUsuario(usuario, clave, fecha_nacimiento, profesion, email, nombre,
				apellidos);

		if (user == null) {
			return false;
		}

		return true;

	}

	
	public boolean login(String usuario, String clave) {
		if (factoria.getUsuarioDAO().login(usuario, clave)) {
			System.out.println("Correcto - Login correctos por app " + contador.siguienteValor());
			return false;
		} else {
			System.out.println("Incorrecto - Login correctos por app " + contador.valorActual());
			return false;
		}
	}

	public int addCoche(String matricula, String modelo, Integer anyo, Integer confort) {
		
		if(existeMatricula(matricula)) {
			return ERROR_MATRICULA_EXISTENTE;
		}
		
		Coche coche = this.cocheDAO.createCoche(usuarioActual.getId(), matricula, modelo, anyo, confort);

		if (coche == null) {
			return ERROR_JPA;
		} 
		
		this.usuarioActual = this.usuarioDAO.findById(this.usuarioActual.getId());
		
		return EXITO;

	}

	private boolean existeMatricula(String matricula) {
		
		Coche c = this.cocheDAO.findByMatricula(matricula);
		
		if(c == null){
			return false;
		}
		
		return true;
	}

	public Integer registrarViaje(Integer plazas, Double precio) {
		
		// TODO Llevar a la vista
		if (plazas <= 0 || precio < 0) {
			return -1;
		}
		
		if (this.usuarioActual.isConductor()) {
			Coche coche = this.usuarioActual.getCoche();
			Viaje viaje = this.viajeDAO.createViaje(coche.getId() , plazas, precio);
			return viaje.getId();
		} else {
			return -1;
		}
	}

	public Integer registrarParadaOrigen(Integer idViaje, String ciudad, String calle, Integer numero,
			Integer CP, Date fecha) {
		
		if (idViaje > 0 && numero >= 0 && CP > 0) {
			
			Parada parada = paradaDAO.createParadaOrigen(idViaje, ciudad, calle, numero, CP, fecha);
			return parada.getId();			
		}
		return -1;
	}
	
	public Integer registrarParadaDestino(Integer idViaje, String ciudad, String calle, Integer numero,
			Integer CP, Date fecha) {
		if (idViaje > 0 && numero >= 0 && CP > 0) {
			
			Parada parada = paradaDAO.createParadaDestino(idViaje, ciudad, calle, numero, CP, fecha);
			return parada.getId();			
		}
		return -1;
	}

	public Integer reservarViaje(Integer idViaje, String comentario) {

			Reserva reserva = reservaDAO.createReserva(usuarioActual.getId(), idViaje, comentario);
			this.usuarioActual.addReserva(reserva);
			return reserva.getId();
	}


	public boolean aceptarReserva(Integer idReserva) {
		Reserva r = this.reservaDAO.findById(idReserva);
		if (r != null) {
			r.aceptarReserva();
			return true;
		}
		return false;
	}


	public boolean rechazarReserva(Integer idReserva) {
		Reserva r = this.reservaDAO.findById(idReserva);
		if (r != null) {
			r.rechazarReserva();
			return true;
		}
		return false;
	}

	public boolean valorarViajeConductor(Integer idViaje, Integer idUsuarioPasajero, String comentario, Integer puntuacion) {


		Viaje v = this.viajeDAO.findById(idViaje);

		if (this.isFechaActualPosterior(v.getFechaFinal()) && !this.isValoracionesCompletadas(idViaje)) {
			
			Reserva reserva = this.reservaDAO.findByViajeAndUsuario(idViaje, idUsuarioPasajero);
			
			Valoracion valoracion = this.valoracionDAO.findByReservaAndReceptor(reserva.getId(), idUsuarioPasajero);
			if(valoracion != null) {
				return false;
			}
			
			Valoracion nuevaValoracion = this.valoracionDAO.createValoracion(idUsuarioPasajero, this.usuarioActual.getId(), comentario, puntuacion, reserva);
			
			if(nuevaValoracion == null) {
				return false;
			}

			return true;

		} else {
			return false;
		}
	}

	public boolean valorarViajePasajero(Integer idViaje, Integer idUsuarioConductor, String comentario, Integer puntuacion) {

		Viaje v = this.viajeDAO.findById(idViaje);	

		if(this.isFechaActualPosterior(v.getFechaFinal()) && !this.isValoracionHecha(idViaje)) {
			
			Reserva reserva = this.reservaDAO.findByViajeAndUsuario(idViaje, this.usuarioActual.getId());

			Valoracion nuevaValoracion = this.valoracionDAO.createValoracion(idUsuarioConductor, this.usuarioActual.getId() , comentario, puntuacion, reserva);
			
			if (nuevaValoracion == null) {
				return false;
			}
			
			return true;

		} else {
			return false;
		}
	}

	//TODO
//	public Collection<Viaje> listarViajes(Integer idUsuario, boolean pendientes, boolean propios, Object ordenFecha, Object ordenCiudad) {
//		// El usuario quiere listar todos las reservas en estado de pendiente que pueda
//		// aceptar/rechazar (Usuario conductor)
//		
//		ArrayList<Viaje> listadoViajes = new ArrayList<Viaje>();
//		if (pendientes) {
//			
//			listadoViajes = (ArrayList<Viaje>) this.usuarioActual.getListViajesPendientes();
//			
//		// El usuario quiere ver todos los viajes reservados (sea o no conductor)
//		} else if (propios) {
//			
//			listadoViajes = (ArrayList<Viaje>) this.usuarioActual.getViajesReservados();
//		}
//		// El usuario quiere listar todos los viajes disponibles que no sean suyos para reservar
//		
//		
//			
//		return listadoViajes;
//	}

	// MÉTODOS AUXILIARES
	
	private boolean isFechaActualPosterior(Date fecha) {

		Date fecha_actual = new Date();
		return fecha_actual.after(fecha);
	}

	private boolean isValoracionesCompletadas(Integer idViaje) {

		List<Reserva> reservas = this.usuarioActual.getReservasByViaje(idViaje);

		ArrayList<Integer> idsReservas = new ArrayList<Integer>();

		for(Reserva r : reservas) {
			idsReservas.add(r.getId());
		}
		
		List<Valoracion> valoraciones = this.valoracionDAO.findByIdsReservas(idsReservas);
		
		return reservas.size() == valoraciones.size();
	}
	
	private boolean isValoracionHecha(Integer idViaje) {
		Reserva reserva = this.reservaDAO.findByViajeAndUsuario(idViaje, this.usuarioActual.getId());
		
		Valoracion v = this.valoracionDAO.findByIdReservaAndUsuario(reserva.getId(), this.usuarioActual.getId());
		
		return  v != null;
	}

	public Object getUsuario(String username, String password) {
		return this.usuarioDAO.findByUsuarioAndPassword(username, password);
	}

	public List<Viaje> buscarViajes(String municipioOrigen, String municipioDestino, Date fechaHora) {
		return this.viajeDAO.buscarViajes(municipioOrigen, municipioDestino, fechaHora);
	}
	
	public List<Viaje> buscarViajesLazy(String ciudadOrigen, String ciudadDestino, Date fechaHora, int start, int max) {
		return this.viajeDAO.buscarViajesLazy(ciudadOrigen, ciudadDestino, fechaHora, start, max);
	}
	
	public Integer countViajes(String ciudadOrigen, String ciudadDestino, Date fechaHora) {
		Long aux = this.viajeDAO.countViajes(ciudadOrigen, ciudadDestino, fechaHora);
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

}
