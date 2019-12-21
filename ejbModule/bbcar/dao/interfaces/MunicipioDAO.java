package bbcar.dao.interfaces;

import java.util.List;

import bbcar.modelo.Municipio;
import bbcar.modelo.Provincia;

public interface MunicipioDAO {

	List<Municipio> findAll();

	List<String> findAll(Provincia provincia);
	
	List<String> findAll(String nombreProvincia);

	Municipio findByMunicipio(String ciudad);
}
