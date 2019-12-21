package bbcar.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import bbcar.modelo.EntityManagerHelper;
import bbcar.modelo.Municipio;
import bbcar.modelo.Provincia;
import bbcar.dao.interfaces.MunicipioDAO;

public class JPAMunicipioDAO implements MunicipioDAO {

	public List<Municipio> findAll() {
		try {
			String queryString = "SELECT m FROM Municipio m ";

			EntityManager em = EntityManagerHelper.getEntityManager();
			Query q = em.createQuery(queryString);

			return q.getResultList();
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw(re);
		}

	}

	@Override
	public List<String> findAll(Provincia provincia) {
		try {
			String queryString = "SELECT m FROM Municipio m WHERE m.provincia = :provincia";
			
			EntityManager em = EntityManagerHelper.getEntityManager();
			Query q = em.createQuery(queryString);
			q.setParameter("provincia", provincia);

			return q.getResultList();
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw(re);
		}
	}

	@Override
	public List<String> findAll(String nombreProvincia) {
		try {
			EntityManager em = EntityManagerHelper.getEntityManager();
//			String primeraQuery = "SELECT p FROM Provincia p WHERE p.provincia = :provincia";
//			Query q = em.createQuery(primeraQuery);
//			q.setParameter("provincia", nombreProvincia);
//			Provincia aux = (Provincia) q.getResultList().get(0);
			
			String queryString = "SELECT m.municipio FROM Municipio m WHERE m.provincia.provincia = :provincia";
			
			Query q2 = em.createQuery(queryString);
			q2.setParameter("provincia", nombreProvincia);

			@SuppressWarnings("unchecked")
			List<String> municipios = q2.getResultList();
			
			return municipios;
			
		} catch (RuntimeException re) {
			re.printStackTrace();
			throw(re);
		}
	}

}
