package bbcar.dao.interfaces;

import java.util.Date;
import java.util.List;

import bbcar.modelo.Coche;
import bbcar.modelo.Viaje;

public interface ViajeDAO {

	public Viaje createViaje(Coche coche, Integer plazas, Double precio);
	
	public Viaje findById(Integer idViaje); 
	
	public List<Viaje> buscarViajesLazy(String ciudadOrigen, String ciudadDestino, Date fechaHora, int start, int max, Integer idUsuario);

	public Long countViajes(String ciudadOrigen, String ciudadDestino, Date fechaHora, Integer idUsuario);

	public void removeViaje(Integer idViaje);
	
	public List<Viaje> findViajesRealizadosPasajero(Integer idUsuario);

	public List<Viaje> findViajesPendientesPasajero(Integer idUsuario);

    public void actualizarViaje(Viaje viaje);


}
