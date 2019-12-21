package bbcar.dao.interfaces;

import bbcar.modelo.Coche;

public interface CocheDAO {
	
	public Coche createCoche(Integer idUsuario, String matricula, String modelo, Integer anyo, Integer confort);
	
	public Coche findById(Integer idCoche);

	public Coche findByMatricula(String matricula);
	
	public void updateCoche(Coche coche);

}
