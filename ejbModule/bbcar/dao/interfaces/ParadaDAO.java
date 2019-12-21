package bbcar.dao.interfaces;

import java.util.Date;

import bbcar.modelo.Municipio;
import bbcar.modelo.Parada;

public interface ParadaDAO {

	public Parada createParadaOrigen(Integer idViaje, Municipio ciudad, String calle, 
			Integer numero, Integer CP, Date fecha);

	public Parada createParadaDestino(Integer idViaje, Municipio ciudad, String calle, 
			Integer numero, Integer CP, Date fecha);
}
