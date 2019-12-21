package bbcar.dao.interfaces;

import javax.ejb.Local;

import bbcar.dao.DAOException;

@Local
public interface DAOFactoriaLocal {
	
	public ParadaDAO getParadaDAO();
	public ViajeDAO getViajeDAO();
	public ReservaDAO getReservaDAO();
	public UsuarioDAO getUsuarioDAO();
	public CocheDAO getCocheDAO();
	public ValoracionDAO getValoracionDAO();
	public ProvinciaDAO getProvinciaDAO();
	public MunicipioDAO getMunicipioDAO();
	
	public void setDAOFactoria(int tipo) throws DAOException;
	
}
