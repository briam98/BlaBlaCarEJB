package bbcar.dao.interfaces;

import java.util.Date;
import java.util.List;

import bbcar.modelo.Viaje;

public interface ViajeDAO {

	public Viaje createViaje(Integer idUsuario, Integer plazas, Double precio);
	
	public Viaje findById(Integer idViaje); 
	
	public List<Viaje> buscarViajes(String ciudadOrigen, String ciudadDestino, Date fechaHora);

	public List<Viaje> buscarViajesLazy(String ciudadOrigen, String ciudadDestino, Date fechaHora, int start, int max);

	public Long countViajes(String ciudadOrigen, String ciudadDestino, Date fechaHora);
}
