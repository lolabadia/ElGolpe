package edu.upc.dsa.domain.repository;

import edu.upc.dsa.domain.models.Partida;
import edu.upc.dsa.dto.RankingTO;
import edu.upc.dsa.exceptions.MuseoNoEncontrado;
import edu.upc.dsa.exceptions.ObjetosNoDisponibles;
import edu.upc.dsa.exceptions.UsuarioNoEncontrado;

import java.util.List;
import java.util.Map;

public interface PartidaManager {

    /**
     * objetosUsados: objetoId -> cantidad usada en la partida.
     */
    Partida registrarPartida(int usuarioId, int museoId, boolean exito, int tiempoSegundos, Map<Integer, Integer> objetosUsados)
            throws UsuarioNoEncontrado, MuseoNoEncontrado, ObjetosNoDisponibles;

    List<RankingTO> getRanking(int limite);

    int numPartidas();

    void clear();
}