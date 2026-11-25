package edu.upc.dsa.domain.repository;

import edu.upc.dsa.domain.models.Compra;
import edu.upc.dsa.domain.models.EntradaInventario;
import edu.upc.dsa.domain.models.Objeto;
import edu.upc.dsa.exceptions.MonedasInsuficientes;
import edu.upc.dsa.exceptions.ObjetoNoEncontrado;
import edu.upc.dsa.exceptions.UsuarioNoEncontrado;

import java.util.List;

public interface TiendaManager {

    List<Objeto> getObjetos();

    Objeto getObjeto(int id);

    Compra comprar(int usuarioId, int objetoId, int cantidad) throws UsuarioNoEncontrado, ObjetoNoEncontrado, MonedasInsuficientes;

    List<EntradaInventario> getInventario(int usuarioId) throws UsuarioNoEncontrado;

    int numCompras();

    void clear();
}