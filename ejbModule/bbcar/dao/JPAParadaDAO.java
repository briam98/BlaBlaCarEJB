package bbcar.dao;

import java.util.Date;
import javax.persistence.EntityManager;

import bbcar.dao.interfaces.ParadaDAO;
import bbcar.modelo.Direccion;
import bbcar.modelo.EntityManagerHelper;
import bbcar.modelo.Municipio;
import bbcar.modelo.Parada;
import bbcar.modelo.Viaje;

public class JPAParadaDAO implements ParadaDAO {

	@Override
	public Parada createParadaOrigen(Integer idViaje, Municipio ciudad, String calle, Integer numero, Integer CP, Date fecha) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		em.getTransaction().begin();
		
		Direccion dir = new Direccion(calle, numero, CP);
		Parada parada = new Parada(ciudad, fecha, dir);
		
		Viaje viaje;
		try {
			viaje = DAOFactoria.getDAOFactoria(DAOFactoria.JPA).getViajeDAO().findById(idViaje);
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
	public Parada createParadaDestino(Integer idViaje, Municipio ciudad, String calle, Integer numero, Integer CP, Date fecha) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		em.getTransaction().begin();
		
		Direccion dir = new Direccion(calle, numero, CP);
		Parada parada = new Parada(ciudad, fecha, dir);
		
		Viaje viaje;
		try {
			viaje = DAOFactoria.getDAOFactoria(DAOFactoria.JPA).getViajeDAO().findById(idViaje);
		} catch (DAOException e) {
			e.printStackTrace();
			return null;
		}
		
		if (viaje != null) {
			viaje.setDestino(parada);
		} else {
			em.getTransaction().rollback();
			em.close();
		}
		
		em.persist(parada);
		em.getTransaction().commit();
		em.close();
		return parada;
	}

}
