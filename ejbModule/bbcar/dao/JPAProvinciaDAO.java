package bbcar.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import bbcar.modelo.EntityManagerHelper;
import bbcar.modelo.Provincia;
import bbcar.dao.interfaces.ProvinciaDAO;

public class JPAProvinciaDAO implements ProvinciaDAO {

	@SuppressWarnings("unchecked")
	public List<Provincia> findAll() {
		try {
			String queryString = "SELECT p FROM Provincia p ";

			EntityManager em = EntityManagerHelper.getEntityManager();
			Query q = em.createQuery(queryString);

			return q.getResultList();
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw(re);
		}

	}

}
