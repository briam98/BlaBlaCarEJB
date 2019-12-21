package bbcar.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import bbcar.controlador.BlaBlaCar;
import bbcar.dao.interfaces.ViajeDAO;
import bbcar.modelo.Coche;
import bbcar.modelo.EntityManagerHelper;
import bbcar.modelo.EstadoReserva;
import bbcar.modelo.Viaje;

public class JPAViajeDAO implements ViajeDAO {

	@Override
	public Viaje createViaje(Integer idCoche, Integer plazas, Double precio) {

		EntityManager em = EntityManagerHelper.getEntityManager();
		em.getTransaction().begin();
		
		
		Coche coche;
		try {
			coche = BlaBlaCar.getInstancia().getFactoria().getCocheDAO().findById(idCoche);
		} catch (DAOException e1) {
			e1.printStackTrace();
			em.close();
			return null;
		}
		
		Viaje viaje = new Viaje(plazas, precio, coche);
		
		coche.anyadirViaje(viaje);
		
		try {
			em.persist(viaje);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			em.close();
			return null;
		} finally {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
				viaje = null;
			}
			em.close();
		}
		return viaje;
	}

	@Override
	public Viaje findById(Integer idViaje) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		
		Viaje v = em.find(Viaje.class, idViaje);
		
		return v;
	}

	@Override
	public List<Viaje> buscarViajes(String ciudadOrigen, String ciudadDestino, Date fechaHora) {
		try {
			String queryString = "SELECT v FROM Viaje v " + " JOIN FETCH v.reservas r "
					+ " WHERE r.estado = :estado and v.origen.fecha >= :today ";
			if (ciudadOrigen != null && !ciudadOrigen.trim().isEmpty()) {
				queryString += " and v.origen.ciudad = :ciudadOrigen ";
			}
			if (ciudadDestino != null && !ciudadDestino.trim().isEmpty()) {
				queryString += " and v.destino.ciudad = :ciudadDestino ";
			}
			if (fechaHora != null) {
				queryString += " and v.origen.fecha >= :fechaHora ";
			}
			EntityManager em = EntityManagerHelper.getEntityManager();
			Query q = em.createQuery(queryString);
			q.setParameter("estado", EstadoReserva.ACEPTADA);
			q.setParameter("today", new Date());
			if (ciudadOrigen != null && !ciudadOrigen.trim().isEmpty()) {
				q.setParameter("ciudadOrigen", ciudadOrigen);
			}
			if (ciudadDestino != null && !ciudadDestino.trim().isEmpty()) {
				q.setParameter("ciudadDestino", ciudadDestino);
			}
			if (fechaHora != null) {
				q.setParameter("fechaHora", fechaHora);
			}
			// Para hacer que se recupera todos los objetos de BBDD
//			q.setHint(QueryHints.REFRESH, HintValues.TRUE);
			return q.getResultList();
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw (re);
		}
	}
	
	@Override
	public List<Viaje> buscarViajesLazy(String ciudadOrigen, String ciudadDestino, Date fechaHora, int start, int max) {
		try {
			String queryString = "SELECT v FROM Viaje v " + " JOIN FETCH v.reservas r "
					+ " WHERE r.estado = :estado and v.origen.fecha >= :today ";
			
			if (ciudadOrigen != null && !ciudadOrigen.trim().isEmpty()) {
				queryString += " and v.origen.ciudad = :ciudadOrigen ";
			}
			if (ciudadDestino != null && !ciudadDestino.trim().isEmpty()) {
				queryString += " and v.destino.ciudad = :ciudadDestino ";
			}
			if (fechaHora != null) {
				queryString += " and v.origen.fecha >= :fechaHora ";
			}
			
			EntityManager em = EntityManagerHelper.getEntityManager();
			Query q = em.createQuery(queryString);
			q.setParameter("estado", EstadoReserva.ACEPTADA);
			q.setParameter("today", new Date());
			
			if (ciudadOrigen != null && !ciudadOrigen.trim().isEmpty()) {
				q.setParameter("ciudadOrigen", ciudadOrigen);
			}
			if (ciudadDestino != null && !ciudadDestino.trim().isEmpty()) {
				q.setParameter("ciudadDestino", ciudadDestino);
			}
			if (fechaHora != null) {
				q.setParameter("fechaHora", fechaHora);
			}
			
			// Para hacer que se recupera todos los objetos de BBDD
//			q.setHint(QueryHints.REFRESH, HintValues.TRUE);

			q.setFirstResult(start);
			q.setMaxResults(max);
			return q.getResultList();
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw (re);
		}
	}
		
	@Override
	public Long countViajes(String ciudadOrigen, String ciudadDestino, Date fechaHora) {
		try {
			String queryString = "SELECT count(v) FROM Viaje v " + " JOIN FETCH v.reservas r "
					+ " WHERE r.estado = :estado and v.origen.fecha >= :today ";
			if (ciudadOrigen != null && !ciudadOrigen.trim().isEmpty()) {
				queryString += " and v.origen.ciudad = :ciudadOrigen ";
			}
			if (ciudadDestino != null && !ciudadDestino.trim().isEmpty()) {
				queryString += " and v.destino.ciudad = :ciudadDestino ";
			}
			if (fechaHora != null) {
				queryString += " and v.origen.fecha >= :fechaHora ";
			}
			EntityManager em = EntityManagerHelper.getEntityManager();
			Query q = em.createQuery(queryString);
			q.setParameter("estado", EstadoReserva.ACEPTADA);
			q.setParameter("today", new Date());
			if (ciudadOrigen != null && !ciudadOrigen.trim().isEmpty()) {
				q.setParameter("ciudadOrigen", ciudadOrigen);
			}
			if (ciudadDestino != null && !ciudadDestino.trim().isEmpty()) {
				q.setParameter("ciudadDestino", ciudadDestino);
			}
			if (fechaHora != null) {
				q.setParameter("fechaHora", fechaHora);
			}
			// Para hacer que se recupera todos los objetos de BBDD
//			q.setHint(QueryHints.REFRESH, HintValues.TRUE);
			return (Long) q.getSingleResult();
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw (re);
		}
	}
	
}