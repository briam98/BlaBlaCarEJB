package bbcar.modelo;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerHelper implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final EntityManagerFactory emf;
	private static final ThreadLocal<EntityManager> threadLocal;
	private static final Logger logger;
	static {
		emf = Persistence.createEntityManagerFactory("BlaBlaCarEJB");
		threadLocal = new ThreadLocal<EntityManager>();
		logger = Logger.getLogger("BlaBlaCar");
		logger.setLevel(Level.ALL);
	}

	public static EntityManager getEntityManager() {
		EntityManager manager = threadLocal.get();
		if (manager == null || !manager.isOpen()) {
			manager = emf.createEntityManager();
			threadLocal.set(manager);
		}
		return manager;
	}

	public static void closeEntityManager() {
		EntityManager em = threadLocal.get();
		threadLocal.set(null);
		if (em != null) {
			em.close();
		}
	}
}