package bbcar.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import bbcar.dao.interfaces.ReservaDAO;
import bbcar.modelo.EntityManagerHelper;
import bbcar.modelo.Reserva;
import bbcar.modelo.Usuario;
import bbcar.modelo.Viaje;

public class JPAReservaDAO implements ReservaDAO {

	@Override
	public Reserva createReserva(Integer idUsuario, Integer idViaje, String comentario) {

		EntityManager em = EntityManagerHelper.getEntityManager();

		// A traves de los ids del usuario y el viaje recuperamos la instancia del
		// objeto para settearlos en la reserva
		Viaje viaje = null;
		Usuario usuario = null;

		try {
			viaje = DAOFactoria.getDAOFactoria(DAOFactoria.JPA).getViajeDAO().findById(idViaje);
			usuario = DAOFactoria.getDAOFactoria(DAOFactoria.JPA).getUsuarioDAO().findById(idUsuario);
		} catch (DAOException e) {
			e.printStackTrace();
			return null;
		}
		

		Reserva reserva = new Reserva(comentario, viaje, usuario);
		
		usuario.addReserva(reserva);
		viaje.addReserva(reserva);
		
		// Comenzamos la transacción y en caso de que algo falle hacemos un rollback y
		// devolvemos null.
		em.getTransaction().begin();
		try {
			em.persist(reserva);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return null;
		} finally {

			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
				reserva = null;
			}
			em.close();
		}

		return reserva;

	}

	@Override
	public List<Reserva> findByIdViaje(Integer idViaje) {
		
		EntityManager em = EntityManagerHelper.getEntityManager();
		
		String jpql = "SELECT r FROM Reserva r WHERE r.viaje = :idViaje";
		Query q = em.createQuery(jpql);
		q.setParameter("idViaje", idViaje);
		
		
		@SuppressWarnings("unchecked")
		List<Reserva> reservas =(List<Reserva>) q.getResultList();
		
		return reservas;
	}

	@Override
	public Reserva findByViajeAndUsuario(Integer idViaje, Integer idUsuario) {
		EntityManager em = EntityManagerHelper.getEntityManager();

		String jpql = "SELECT r FROM Reserva r WHERE r.viaje.id = :idViaje AND r.usuario.id = :idUsuario";
		
		Query q = em.createQuery(jpql);
		
		q.setParameter("idViaje", idViaje);
		q.setParameter("idUsuario", idUsuario);
		
		@SuppressWarnings("unchecked")
		List<Reserva> reservas = q.getResultList();
		
		if(reservas.isEmpty()) {
			return null;
		}
		
		return reservas.get(0);
	}

	@Override
	public Reserva findById(Integer idReserva) {
		
		EntityManager em = EntityManagerHelper.getEntityManager();

		String jpql = "SELECT r FROM Reserva r WHERE r.id = :idReserva";
		
		Query q = em.createQuery(jpql);
		q.setParameter("idReserva", idReserva);
		
		@SuppressWarnings("unchecked")
		List<Reserva> reservas = q.getResultList();
		
		if(reservas.isEmpty()) {
			return null;
		}
		
		return reservas.get(0);
	}

	@Override
	public void actualizarEstado(Reserva reserva) {
		EntityManager em = EntityManagerHelper.getEntityManager();

		em.getTransaction().begin();

		em.merge(reserva);

		em.getTransaction().commit();
	}

}
