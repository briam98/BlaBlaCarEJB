package bbcar.dao;

import bbcar.dao.interfaces.CocheDAO;
import bbcar.dao.interfaces.MunicipioDAO;
import bbcar.dao.interfaces.ParadaDAO;
import bbcar.dao.interfaces.ProvinciaDAO;
import bbcar.dao.interfaces.ReservaDAO;
import bbcar.dao.interfaces.UsuarioDAO;
import bbcar.dao.interfaces.ValoracionDAO;
import bbcar.dao.interfaces.ViajeDAO;

public class JPADAOFactoria  extends DAOFactoria {

	public ParadaDAO getParadaDAO() {
		return (ParadaDAO) new JPAParadaDAO();
	}

	public ViajeDAO getViajeDAO() {
		return (ViajeDAO) new JPAViajeDAO();
	}

	public ReservaDAO getReservaDAO() {
		return (ReservaDAO) new JPAReservaDAO();
	}
	
	public CocheDAO getCocheDAO() {
		return (CocheDAO) new JPACocheDAO();
	}
	
	public UsuarioDAO getUsuarioDAO() {
		return (UsuarioDAO) new JPAUsuarioDAO();
	}
	
	public ValoracionDAO getValoracionDAO() {
		return (ValoracionDAO) new JPAValoracionDAO();
	}
	
	public ProvinciaDAO getProvinciaDAO() {
		return (ProvinciaDAO) new JPAProvinciaDAO();
	}
	
	public MunicipioDAO getMunicipioDAO() {
		return (MunicipioDAO) new JPAMunicipioDAO();
	}
}
