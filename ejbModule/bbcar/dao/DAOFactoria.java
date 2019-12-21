package bbcar.dao;

import bbcar.dao.interfaces.CocheDAO;
import bbcar.dao.interfaces.MunicipioDAO;
import bbcar.dao.interfaces.ParadaDAO;
import bbcar.dao.interfaces.ProvinciaDAO;
import bbcar.dao.interfaces.ReservaDAO;
import bbcar.dao.interfaces.UsuarioDAO;
import bbcar.dao.interfaces.ValoracionDAO;
import bbcar.dao.interfaces.ViajeDAO;

public abstract class DAOFactoria {
	
	public abstract ParadaDAO getParadaDAO();
	public abstract ViajeDAO getViajeDAO();
	public abstract ReservaDAO getReservaDAO();
	public abstract UsuarioDAO getUsuarioDAO();
	public abstract CocheDAO getCocheDAO();
	public abstract ValoracionDAO getValoracionDAO();
	public abstract ProvinciaDAO getProvinciaDAO();
	public abstract MunicipioDAO getMunicipioDAO();

//	public final static int MYSQL = 1;
	public final static int JPA = 2;

	public static DAOFactoria getDAOFactoria(int tipo) throws DAOException {
		switch (tipo) {
//		case MYSQL: {
//			try {
//				return new MySQLDAOFactoria();
//			} catch (Exception e) {
//				throw new DAOException(e.getMessage());
//			}	
//		}
		case JPA: {
			try {
				return new JPADAOFactoria();
			} catch (Exception e) {
				throw new DAOException(e.getMessage());
			}
		}
		default:
			return null;
		}
	}

}
