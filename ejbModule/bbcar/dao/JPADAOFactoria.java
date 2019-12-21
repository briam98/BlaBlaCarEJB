package bbcar.dao;

import bbcar.dao.interfaces.*;

public class JPADAOFactoria extends DAOFactoria {

	@Override
	public ParadaDAO getParadaDAO() {
		return (ParadaDAO) new JPAParadaDAO();
	}

	@Override
	public ViajeDAO getViajeDAO() {
		return (ViajeDAO) new JPAViajeDAO();
	}

	@Override
	public ReservaDAO getReservaDAO() {
		return (ReservaDAO) new JPAReservaDAO();
	}
	
	@Override
	public CocheDAO getCocheDAO() {
		return (CocheDAO) new JPACocheDAO();
	}
	
	@Override
	public UsuarioDAO getUsuarioDAO() {
		return (UsuarioDAO) new JPAUsuarioDAO();
	}
	
	@Override
	public ValoracionDAO getValoracionDAO() {
		return (ValoracionDAO) new JPAValoracionDAO();
	}
	
	@Override 
	public ProvinciaDAO getProvinciaDAO() {
		return (ProvinciaDAO) new JPAProvinciaDAO();
	}
	
	@Override 
	public MunicipioDAO getMunicipioDAO() {
		return (MunicipioDAO) new JPAMunicipioDAO();
	}

}
