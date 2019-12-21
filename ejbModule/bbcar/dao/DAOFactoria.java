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
	protected DAOFactoria factoria;
	
	public ParadaDAO getParadaDAO() {
		return factoria.getParadaDAO();
	}
	
	public ViajeDAO getViajeDAO() {
		return factoria.getViajeDAO();
	}
	
	public ReservaDAO getReservaDAO() {
		return factoria.getReservaDAO();
	}
	
	public UsuarioDAO getUsuarioDAO() {
		return factoria.getUsuarioDAO();
	}
	
	public CocheDAO getCocheDAO() {
		return factoria.getCocheDAO();
	}
	
	public ValoracionDAO getValoracionDAO() {
		return factoria.getValoracionDAO();
	}
	
	public ProvinciaDAO getProvinciaDAO() {
		return factoria.getProvinciaDAO();
	}
	
	public MunicipioDAO getMunicipioDAO() {
		return factoria.getMunicipioDAO();
	}

	public final static int JPA = 2;

	@Override
	public void setDAOFactoria(int tipo) throws DAOException {
		switch(tipo) {
		case JPA:
			try {
				factoria = new JPADAOFactoria();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

}
