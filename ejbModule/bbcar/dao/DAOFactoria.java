package bbcar.dao;

import javax.ejb.Stateless;

import bbcar.dao.interfaces.CocheDAO;
import bbcar.dao.interfaces.DAOFactoriaLocal;
import bbcar.dao.interfaces.MunicipioDAO;
import bbcar.dao.interfaces.ParadaDAO;
import bbcar.dao.interfaces.ProvinciaDAO;
import bbcar.dao.interfaces.ReservaDAO;
import bbcar.dao.interfaces.UsuarioDAO;
import bbcar.dao.interfaces.ValoracionDAO;
import bbcar.dao.interfaces.ViajeDAO;

@Stateless(name="Factoria")
public class DAOFactoria implements DAOFactoriaLocal {
	
	public final static int JPA = 2; 
	
	protected DAOFactoria factoria;

	@Override
	public ParadaDAO getParadaDAO() {
		return factoria.getParadaDAO();
	}

	@Override
	public ViajeDAO getViajeDAO() {
		return factoria.getViajeDAO();
	}

	@Override
	public ReservaDAO getReservaDAO() {
		return factoria.getReservaDAO();
	}

	@Override
	public UsuarioDAO getUsuarioDAO() {
		return factoria.getUsuarioDAO();
	}

	@Override
	public CocheDAO getCocheDAO() {
		return factoria.getCocheDAO();
	}

	@Override
	public ValoracionDAO getValoracionDAO() {
		return factoria.getValoracionDAO();
	}

	@Override
	public ProvinciaDAO getProvinciaDAO() {
		return factoria.getProvinciaDAO();
	}

	@Override
	public MunicipioDAO getMunicipioDAO() {
		return factoria.getMunicipioDAO();
	}

	@Override
	public void setDAOFactoria(int tipo) throws DAOException {
		switch(tipo) {
		case JPA:
			try {
				factoria = new JPADAOFactoria();
			}
			catch(Exception e) {
				throw new DAOException(e.getMessage());
			}
			break;
		}
	}
}
