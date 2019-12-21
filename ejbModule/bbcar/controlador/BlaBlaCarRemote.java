package bbcar.controlador;

import java.util.Date;

import javax.ejb.Remote;

import bbcar.dao.interfaces.DAOFactoriaLocal;

@Remote
public interface BlaBlaCarRemote {
	public DAOFactoriaLocal getFactoria();
	public boolean login(String usuario, String clave);
	public boolean registrarUsuario(String usuario, String clave, Date fecha_nacimiento, 
			String profesion, String email, String nombre, String apellidos);

	//Falta declarar el resto de metodos
}
