package bbcar.dao.interfaces;

import java.util.Date;

import bbcar.modelo.Usuario;

public interface UsuarioDAO {

	public Usuario createUsuario(String usuario, String clave, Date fecha_nacimiento, String profesion, String email,
			String nombre, String apellidos);

	public Usuario findById(Integer idUsuario);

	public Usuario findByUsuario(String usuario);

	public Usuario findByUsuarioAndPassword(String username, String password);
	
}
