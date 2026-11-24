package edu.upc.dsa.domain.repository;

import edu.upc.dsa.domain.models.Usuario;
import edu.upc.dsa.exceptions.CredencialesIncorrectas;
import edu.upc.dsa.exceptions.UsuarioYaExiste;

public interface UsuarioManager {

    Usuario registrar(String nombre, String email, String password) throws UsuarioYaExiste;

    Usuario login(String nombre, String password) throws CredencialesIncorrectas;

    Usuario getUsuario(int id);

    int numUsuarios();

    void clear();
}