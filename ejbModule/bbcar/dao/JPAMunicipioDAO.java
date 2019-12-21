package bbcar.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import bbcar.dao.interfaces.MunicipioDAO;
import bbcar.modelo.EntityManagerHelper;
import bbcar.modelo.Municipio;
import bbcar.modelo.Provincia;

public class JPAMunicipioDAO implements MunicipioDAO {

	@SuppressWarnings("unchecked")
	@Override
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

	@SuppressWarnings("unchecked")
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

	@Override
	public Municipio findByMunicipio(String ciudad) {
		EntityManager em = EntityManagerHelper.getEntityManager();
		
		String queryString = "SELECT m FROM Municipio m WHERE m.municipio = :municipio";
		
		Query q = em.createQuery(queryString);
		q.setParameter("municipio", ciudad);

		@SuppressWarnings("unchecked")
		List<Municipio> listResult = q.getResultList();
		
		if(listResult.isEmpty()) {
			return null;
		}

		return listResult.get(0);
	}

}
