package bbcar.dao;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.persistence.EntityManager;

import bbcar.dao.interfaces.DAOFactoriaLocal;
import bbcar.dao.interfaces.ParadaDAO;
import bbcar.modelo.Direccion;
import bbcar.modelo.EntityManagerHelper;
import bbcar.modelo.Municipio;
import bbcar.modelo.Parada;
import bbcar.modelo.Viaje;

public class JPAParadaDAO implements ParadaDAO {
	
	@EJB(beanName="Factoria")
	private DAOFactoriaLocal factoria;
	
	@PostConstruct
	public void configurarBlaBlaCarEJB() {
		try {
			factoria.setDAOFactoria(DAOFactoria.JPA);
		} catch(DAOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Parada createParadaOrigen(Integer idViaje, Municipio ciudad, String calle, Integer numero, Integer CP, Date fecha) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		em.getTransaction().begin();
		
		Direccion dir = new Direccion(calle, numero, CP);
		Parada parada = new Parada(ciudad, fecha, dir);
		
		Viaje viaje;
		viaje = factoria.getViajeDAO().findById(idViaje);
		
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
		viaje = factoria.getViajeDAO().findById(idViaje);
		
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
