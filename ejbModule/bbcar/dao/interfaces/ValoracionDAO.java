package bbcar.dao.interfaces;

import java.util.List;

import bbcar.modelo.Reserva;
import bbcar.modelo.Valoracion;

public interface ValoracionDAO {
	public Valoracion createValoracion(Integer idUsuarioRecibidor, Integer idUsuarioEmisor, String comentario, Integer puntuacion, Reserva reserva);
	
	public List<Valoracion> findByIdsReservasAndUsuario(List<Integer> idsReservas, Integer idUsuarioEmisor);

	public Valoracion findByIdReservaAndUsuario(Integer idReserva, Integer idEmisor);

	public Valoracion findByReservaAndReceptor(Integer idReserva, Integer idValorado);
}
