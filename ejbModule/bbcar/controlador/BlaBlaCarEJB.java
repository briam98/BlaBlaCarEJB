package bbcar.controlador;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.ejb.Stateless;

import bbcar.dao.DAOException;
import bbcar.dao.DAOFactoria;
import bbcar.dao.interfaces.DAOFactoriaLocal;

@Stateless(name = "BlaBlaCarRemoto")
public class BlaBlaCarEJB implements BlaBlaCarRemote {
	@EJB(beanName = "Contador")
	private ContadorEJB contador;
	
	@EJB(beanName="Factoria")
	private DAOFactoriaLocal factoria;
	
	@Resource
	private SessionContext context; //Dice que es un ejemplo, alomejor se puede quitar

	@Override
	public DAOFactoriaLocal getFactoria() {
		return factoria;
	}

	@PostConstruct
	public void configurarBlaBlaCarEJB() {
		try {
			factoria.setDAOFactoria(DAOFactoria.JPA);
		} catch(DAOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean login(String usuario, String clave) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean registrarUsuario(String usuario, String clave, Date fecha_nacimiento, String profesion, String email,
			String nombre, String apellidos) {
		// TODO Auto-generated method stub
		return false;
	}
}