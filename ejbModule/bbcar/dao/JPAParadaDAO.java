package bbcar.dao;

import java.util.Date;
import javax.persistence.EntityManager;

import bbcar.controlador.BlaBlaCar;
import bbcar.dao.interfaces.ParadaDAO;
import bbcar.modelo.Direccion;
import bbcar.modelo.EntityManagerHelper;
import bbcar.modelo.Parada;
import bbcar.modelo.Viaje;

public class JPAParadaDAO implements ParadaDAO {

	@Override
	public Parada createParadaOrigen(Integer idViaje, String ciudad, String calle, Integer numero, Integer CP, Date fecha) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		em.getTransaction().begin();
		
		Direccion dir = new Direccion(calle, numero, CP);
		Parada parada = new Parada(ciudad, fecha, dir);
		
		Viaje viaje;
		try {
			viaje = BlaBlaCar.getInstancia().getFactoria().getViajeDAO().findById(idViaje);
		} catch (DAOException e) {
			e.printStackTrace();
			return null;
		}
		
		if (viaje != null) {
			viaje.setOrigen(parada);
		} else {
			em.getTransaction().rollback();
			em.close();
		}
		
		em.persist(parada);
		em.getTransaction().commit();
		em.close();
		return parada;
	}
	
	@Override
	public Parada createParadaDestino(Integer idViaje, String ciudad, String calle, Integer numero, Integer CP, Date fecha) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		em.getTransaction().begin();
		
		Direccion dir = new Direccion(calle, numero, CP);
		Parada parada = new Parada(ciudad, fecha, dir);
		
		Viaje viaje;
		try {
			viaje = BlaBlaCar.getInstancia().getFactoria().getViajeDAO().findById(idViaje);
		} catch (DAOException e) {
			e.printStackTrace();
			return null;
		}
		viaje.setDestino(parada);
		
		em.persist(parada);
		em.getTransaction().commit();
		em.close();
		return parada;
	}

}
