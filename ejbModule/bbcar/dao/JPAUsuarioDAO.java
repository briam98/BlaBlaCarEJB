package bbcar.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import bbcar.dao.interfaces.UsuarioDAO;
import bbcar.modelo.EntityManagerHelper;
import bbcar.modelo.Usuario;

public class JPAUsuarioDAO implements UsuarioDAO {


	@Override
	public Usuario createUsuario(String usuario, String clave, Date fecha_nacimiento, String profesion, String email,
			String nombre, String apellidos) {

		EntityManager em = EntityManagerHelper.getEntityManager();
		em.getTransaction().begin();

		Usuario user = new Usuario(nombre, apellidos, usuario, clave, fecha_nacimiento, profesion, email);

		try {
			em.persist(user);
			em.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return null;
		} finally {
			if(em.getTransaction().isActive()) {
				em.getTransaction().rollback();
				user = null;
			}
			em.close();
		}


		return user;
	}

	@Override
	public Usuario findById(Integer idUsuario) {

		EntityManager em = EntityManagerHelper.getEntityManager();

		Usuario u = em.find(Usuario.class, idUsuario);

		return u;
	}

	@Override
	public Usuario findByUsuario(String usuario) {

		EntityManager em = EntityManagerHelper.getEntityManager();

		String jpql = "SELECT u FROM Usuario u WHERE u.usuario = :usuario";
		Query q = em.createQuery(jpql);
		q.setParameter("usuario", usuario);

		@SuppressWarnings("unchecked")
		List<Usuario> list = q.getResultList();
		
		if(list == null || list.isEmpty()) {
			return null;
		}

		return list.get(0);

	}

	@Override
	public Usuario findByUsuarioAndPassword(String username, String password) {
		EntityManager em = EntityManagerHelper.getEntityManager();

		String jpql = "SELECT u FROM Usuario u WHERE u.usuario = :usuario AND u.clave = :clave";
		Query q = em.createQuery(jpql);
		q.setParameter("usuario", username);
		q.setParameter("clave", password);

		@SuppressWarnings("unchecked")
		List<Usuario> list = q.getResultList();
		
		if(list.isEmpty()) {
			return null;
		}

		return list.get(0);

	}
}
