package bbcar.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import bbcar.dao.interfaces.ValoracionDAO;
import bbcar.modelo.EntityManagerHelper;
import bbcar.modelo.Reserva;
import bbcar.modelo.Usuario;
import bbcar.modelo.Valoracion;

public class JPAValoracionDAO implements ValoracionDAO {

	@Override
	public Valoracion createValoracion(Integer idUsuarioRecibidor, Integer idUsuarioEmisor, String comentario, Integer puntuacion, Reserva reserva) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		em.getTransaction().begin();
		
		Usuario usuarioValorado = em.find(Usuario.class, idUsuarioRecibidor);
		Usuario usuarioEmisor = em.find(Usuario.class, idUsuarioEmisor);
		
		Valoracion valoracion = new Valoracion(comentario, puntuacion);
		valoracion.setReserva(reserva);
		valoracion.setUsuarioEmisor(usuarioEmisor);
		valoracion.setUsuarioValorado(usuarioValorado);
		
				
		try {
			em.persist(valoracion);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return null;
		} finally {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
				valoracion = null;
			}
			em.close();
		}
		

		return valoracion;
	}

	@Override
	public List<Valoracion> findByIdsReservasAndUsuario(List<Integer> idsReservas, Integer idUsuarioEmisor) {

		EntityManager em = EntityManagerHelper.getEntityManager();
		
		String jpql = "SELECT v FROM Valoracion v WHERE v.reserva IN :idsReservas AND v.usuarioEmisor.id = :idUsuarioEmisor";
		Query q = em.createQuery(jpql);
		q.setParameter("idsReservas", idsReservas);
		q.setParameter("idUsuarioEmisor", idUsuarioEmisor);
		
		
		@SuppressWarnings("unchecked")
		List<Valoracion> valoraciones = q.getResultList();
		
		return valoraciones;
	}

	@Override
	public Valoracion findByIdReservaAndUsuario(Integer idReserva, Integer idEmisor) {
		EntityManager em = EntityManagerHelper.getEntityManager();

		String jpql = "SELECT v FROM Valoracion v WHERE v.reserva.id = :idReserva AND v.usuarioEmisor.id = :idEmisor";
		Query q = em.createQuery(jpql);
		q.setParameter("idReserva", idReserva);
		q.setParameter("idEmisor", idEmisor);


		@SuppressWarnings("unchecked")
		List<Valoracion> valoraciones = q.getResultList();
		
		if(valoraciones.isEmpty()) {
			return null;
		}

		return valoraciones.get(0);
	}

	@Override
	public Valoracion findByReservaAndReceptor(Integer idReserva, Integer idValorado) {
		EntityManager em = EntityManagerHelper.getEntityManager();

		String jpql = "SELECT v FROM Valoracion v WHERE v.reserva.id = :idReserva AND v.usuarioValorado.id = :idValorado";
		Query q = em.createQuery(jpql);
		q.setParameter("idReserva", idReserva);
		q.setParameter("idValorado", idValorado);


		@SuppressWarnings("unchecked")
		List<Valoracion> valoraciones = q.getResultList();
		
		if(valoraciones.isEmpty()) {
			return null;
		}

		return valoraciones.get(0);	}

}
