package bbcar.controlador;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import bbcar.dao.DAOException;
import bbcar.dao.DAOFactoria;
import bbcar.dao.interfaces.DAOFactoriaLocal;
import bbcar.modelo.Coche;
import bbcar.modelo.Municipio;
import bbcar.modelo.Provincia;
import bbcar.modelo.Reserva;
import bbcar.modelo.Usuario;
import bbcar.modelo.Valoracion;
import bbcar.modelo.Viaje;

@Stateless(name = "BlaBlaCarRemoto")
public class BlaBlaCarEJB implements BlaBlaCarRemote { 
	
	@EJB(beanName = "Contador")
	private ContadorEJB contador;
	
	@EJB(beanName="Factoria")
	private DAOFactoriaLocal factoria;
	
	private Usuario usuarioActual;

	@Resource
	private SessionContext context; //Dice que es un ejemplo, alomejor se puede quitar

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer registrarParadaOrigen(Integer idViaje, String ciudad, String calle, Integer numero, Integer CP,
			Date fecha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer registrarParadaDestino(Integer idViaje, String ciudad, String calle, Integer numero, Integer CP,
			Date fecha) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer reservarViaje(Integer idViaje, String comentario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int aceptarReserva(Reserva reserva) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void rechazarReserva(Reserva reserva) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int valorarUnPasajero(Integer idViaje, Integer idUsuarioPasajero, String comentario, Integer puntuacion) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int valorarUnConductor(Integer idViaje, Integer idUsuarioConductor, String comentario, Integer puntuacion) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Viaje> buscarViajesLazy(String municipioOrigen, String municipioDestino, Date fechaHora, int start,
			int max) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer countViajes(String ciudadOrigen, String ciudadDestino, Date fechaHora) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Coche getCocheUsuario() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCocheRegistrado() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void actualizarCoche(Integer anyo, String matricula, String modelo, Integer nivelConfort) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Provincia> getProvincias() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Municipio> getMunicipios() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getMunicipios(Provincia p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getMunicipios(String nombreProvincia) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminarViaje(Integer idViaje) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Usuario> getPasajeros(Integer idViaje) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getIdConductor(Integer idViaje) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Viaje> getViajesConductorRealizados() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Viaje> getViajesConductorPendientes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValoracionPasajeroHecha(Integer idViaje, Integer idPasajero) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Viaje> getViajesPasajeroRealizados() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Viaje> getViajesPasajeroPendientes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValoradoConductor(Integer idViaje, Integer idConductor) {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Valoracion> getValoracionesRealizadas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Valoracion> getValoracionesRecibidas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Reserva> getReservas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rechazarOtrasReservas(Integer idViaje) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean existeMatricula(String matricula) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUsuarioLogueado() {
		// TODO Auto-generated method stub
		return false;
	}
}