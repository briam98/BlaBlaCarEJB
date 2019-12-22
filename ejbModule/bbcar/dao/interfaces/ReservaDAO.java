package bbcar.dao.interfaces;

import java.util.List;

import bbcar.modelo.Reserva;

public interface ReservaDAO {

	public Reserva createReserva(Integer idUsuario, Integer idViaje, String comentario);
	
	public List<Reserva> findByIdViaje(Integer idViaje);
	
	public Reserva findByViajeAndUsuario(Integer idViaje, Integer idUsuario);
	
	public Reserva findById(Integer idReserva);

	public void actualizarEstado(Reserva reserva);
}
