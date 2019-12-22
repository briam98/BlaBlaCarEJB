package bbcar.controlador;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import bbcar.dao.interfaces.DAOFactoriaLocal;
import bbcar.modelo.Coche;
import bbcar.modelo.Municipio;
import bbcar.modelo.Provincia;
import bbcar.modelo.Reserva;
import bbcar.modelo.Usuario;
import bbcar.modelo.Valoracion;
import bbcar.modelo.Viaje;

@Remote
public interface BlaBlaCarRemote {
	public DAOFactoriaLocal getFactoria();
	
	public boolean registrarUsuario(String usuario, String clave, Date fecha_nacimiento, String profesion, String email,
			String nombre, String apellidos);
	
	public int login(String usuario, String clave);
	
	public int addCoche(String matricula, String modelo, Integer anyo, Integer confort);

	public Integer registrarViaje(Integer plazas, Double precio);
	
	public Integer registrarParadaOrigen(Integer idViaje, String ciudad, String calle, Integer numero,
			Integer CP, Date fecha);
	
	public Integer registrarParadaDestino(Integer idViaje, String ciudad, String calle, Integer numero,
			Integer CP, Date fecha);

	public Integer reservarViaje(Integer idViaje, String comentario);

	public int aceptarReserva(Integer  idReserva, Integer idViaje);

	public void rechazarReserva(Integer idReserva, Integer idViaje);

	public int valorarUnPasajero(Integer idViaje, Integer idUsuarioPasajero, String comentario, Integer puntuacion);

	public int valorarUnConductor(Integer idViaje, Integer idUsuarioConductor, String comentario, Integer puntuacion);
	
	public List<Viaje> buscarViajesLazy(String municipioOrigen, String municipioDestino, Date fechaHora, int start, int max);
	
	public Integer countViajes(String ciudadOrigen, String ciudadDestino, Date fechaHora);
	
	public Coche getCocheUsuario();

	public boolean isUsuarioLogueado();

	public boolean isCocheRegistrado();

	public void actualizarCoche(Integer anyo, String matricula, String modelo, Integer nivelConfort);

	public List<Provincia> getProvincias();
	
	public List<Municipio> getMunicipios();

	public List<String> getMunicipios(Provincia p);
	
	public List<String> getMunicipios(String nombreProvincia);

	public void eliminarViaje(Integer idViaje);

	public List<Usuario> getPasajeros(Integer idViaje);

	public Integer getIdConductor(Integer idViaje);

	public List<Viaje> getViajesConductorRealizados();

	public List<Viaje> getViajesConductorPendientes();

	public boolean isValoracionPasajeroHecha(Integer idViaje, Integer idPasajero);

	public List<Viaje> getViajesPasajeroRealizados();

	public List<Viaje> getViajesPasajeroPendientes();

	public boolean isValoradoConductor(Integer idViaje, Integer idConductor);

	public boolean existeUsuario(String usuario);

	public boolean isReservaPendiente(Integer idReserva);

	public List<Valoracion> getValoracionesRealizadas();

	public List<Valoracion> getValoracionesRecibidas();

	public List<Reserva> getReservas(); 

	public void rechazarOtrasReservas(Integer idViaje);
	
	public boolean existeMatricula(String matricula);
}
