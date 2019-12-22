package bbcar.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import bbcar.dao.interfaces.ViajeDAO;
import bbcar.modelo.Coche;
import bbcar.modelo.EntityManagerHelper;
import bbcar.modelo.EstadoReserva;
import bbcar.modelo.Viaje;

public class JPAViajeDAO implements ViajeDAO {
	@Override
	public Viaje createViaje(Coche coche, Integer plazas, Double precio) {

		EntityManager em = EntityManagerHelper.getEntityManager();
		em.getTransaction().begin();
		
		Viaje viaje = new Viaje(plazas, precio, coche);
				
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
		
	@SuppressWarnings("unchecked")
	@Override
	public List<Viaje> buscarViajesLazy(String ciudadOrigen, String ciudadDestino, Date fechaHora, int start, int max, Integer idUsuario) {
		try {
			String queryString = "SELECT v FROM Viaje v " + 
					" WHERE v.id NOT IN (SELECT r.viaje.id FROM Reserva r WHERE r.usuario.id = :usuario) " + 
					" and v.origen.fecha_hora >= :today ";
			queryString += "and v.coche.usuario.id != :usuario ";
			queryString += "and v.plazasLibres > 0 ";
			if (ciudadOrigen != null && !ciudadOrigen.trim().isEmpty()) {
				queryString += " and v.origen.ciudad.municipio = :ciudadOrigen ";
			}
			if (ciudadDestino != null && !ciudadDestino.trim().isEmpty()) {
				queryString += " and v.destino.ciudad.municipio = :ciudadDestino ";
			}
			if (fechaHora != null) {
				queryString += " and v.origen.fecha_hora >= :fechaHora ";
			}
			queryString += " ORDER BY v.origen.fecha_hora";
			EntityManager em = EntityManagerHelper.getEntityManager();
			Query q = em.createQuery(queryString);
			q.setParameter("today", new Date());
			q.setParameter("usuario", idUsuario);
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
			q.setHint(QueryHints.REFRESH, HintValues.TRUE);
			
			q.setFirstResult(start);
			q.setMaxResults(max);
			
			return q.getResultList();
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw (re);
		}
	}
		
	@Override
	public Long countViajes(String ciudadOrigen, String ciudadDestino, Date fechaHora, Integer idUsuario) {
		try {
			String queryString = "SELECT count(v) FROM Viaje v " + " WHERE v.origen.fecha_hora >= :today ";
			queryString += "and v.coche.usuario.id != :usuario ";
			queryString += "and v.plazasLibres > 0 ";
			if (ciudadOrigen != null && !ciudadOrigen.trim().isEmpty()) {
				queryString += " and v.origen.ciudad.municipio = :ciudadOrigen ";
			}
			if (ciudadDestino != null && !ciudadDestino.trim().isEmpty()) {
				queryString += " and v.destino.ciudad.municipio = :ciudadDestino ";
			}
			if (fechaHora != null) {
				queryString += " and v.origen.fecha_hora >= :fechaHora ";
			}
			EntityManager em = EntityManagerHelper.getEntityManager();
			Query q = em.createQuery(queryString);
			q.setParameter("today", new Date());
			q.setParameter("usuario", idUsuario);
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
			q.setHint(QueryHints.REFRESH, HintValues.TRUE);
			return (Long) q.getSingleResult();
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw (re);
		}
	}

	@Override
	public void removeViaje(Integer idViaje) {
		
		EntityManager em = EntityManagerHelper.getEntityManager();
		
		Viaje viaje = em.find(Viaje.class, idViaje);
		
		em.getTransaction().begin();
		em.remove(viaje);
		em.getTransaction().commit();
		
	}

	@Override
	public List<Viaje> findViajesRealizadosPasajero(Integer idUsuario) {
		
		EntityManager em = EntityManagerHelper.getEntityManager();

		String query2 = "SELECT v FROM Viaje v WHERE v.id IN ("
				+ "SELECT r.viaje.id "
				+ "FROM Reserva r "
				+ "WHERE r.usuario.id = :idUsuario AND r.estado = :estadoAceptada)" 
				+ "AND v.destino.fecha_hora < :fechaActual";

		Query q2 = em.createQuery(query2);
		q2.setParameter("idUsuario", idUsuario);
		q2.setParameter("estadoAceptada", EstadoReserva.ACEPTADA);
		q2.setParameter("fechaActual", new Date());

		@SuppressWarnings("unchecked")
		List<Viaje> listViajes = q2.getResultList();
		
		return listViajes;
	}

	@Override
	public List<Viaje> findViajesPendientesPasajero(Integer idUsuario) {
		
		EntityManager em = EntityManagerHelper.getEntityManager();

		String query2 = "SELECT v FROM Viaje v WHERE v.id IN ("
				+ "SELECT r.viaje.id "
				+ "FROM Reserva r "
				+ "WHERE r.usuario.id = :idUsuario)" 
				+ "AND v.origen.fecha_hora > :fechaActual";

		Query q2 = em.createQuery(query2);
		q2.setParameter("idUsuario", idUsuario);
		q2.setParameter("fechaActual", new Date());

		@SuppressWarnings("unchecked")
		List<Viaje> listViajes = q2.getResultList();
		
		return listViajes;
	}
	
	@Override
    public void actualizarViaje(Viaje viaje) {
        EntityManager em = EntityManagerHelper.getEntityManager();

        em.getTransaction().begin();

        em.merge(viaje);

        em.getTransaction().commit();
    }

}