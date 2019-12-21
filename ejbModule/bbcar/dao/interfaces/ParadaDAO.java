package bbcar.dao.interfaces;

import java.util.Date;

import bbcar.modelo.Parada;

public interface ParadaDAO {

	public Parada createParadaOrigen(Integer idViaje, String ciudad, String calle, 
			Integer numero, Integer CP, Date fecha);

	public Parada createParadaDestino(Integer idViaje, String ciudad, String calle, 
			Integer numero, Integer CP, Date fecha);
}
