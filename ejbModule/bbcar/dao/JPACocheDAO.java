package bbcar.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import bbcar.controlador.BlaBlaCar;
import bbcar.dao.interfaces.CocheDAO;
import bbcar.modelo.Coche;
import bbcar.modelo.EntityManagerHelper;
import bbcar.modelo.Usuario;

public class JPACocheDAO implements CocheDAO {

	@Override
	public Coche createCoche(Integer idUsuario, String matricula, String modelo, Integer anyo, Integer confort) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		em.getTransaction().begin();
		
		Usuario usuario;
		try {
			usuario = BlaBlaCar.getInstancia().getFactoria().getUsuarioDAO().findById(idUsuario);
		} catch (DAOException e) {
			e.printStackTrace();
			return null;
		}
		
		if (usuario == null) {
			return null;
		}
		
		Coche c = new Coche(matricula, modelo, confort, anyo, usuario);
		
		usuario.setCoche(c);
		
		try {
			em.persist(c);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return null;
		} finally {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
				c = null;
			}
			
			em.close();
		}
		
		return c;
		
	}

	@Override
	public Coche findById(Integer idCoche) {
		
		EntityManager em = EntityManagerHelper.getEntityManager();
		
		Coche c = em.find(Coche.class, idCoche);
		
		return c;
	}

	@Override
	public void updateCoche(Coche coche) {
		EntityManager em = EntityManagerHelper.getEntityManager();

		em.getTransaction().begin();

		em.merge(coche);

		em.getTransaction().commit();
		
	}

	@Override
	public Coche findByMatricula(String matricula) {
		
		EntityManager em = EntityManagerHelper.getEntityManager();

		String jpql = "SELECT c FROM Coche c WHERE c.matricula = :matricula";
		Query q = em.createQuery(jpql);
		q.setParameter("matricula", matricula);

		@SuppressWarnings("unchecked")
		List<Coche> list = q.getResultList();
		
		if(list == null || list.isEmpty()) {
			return null;
		}

		return list.get(0);
	}

}
