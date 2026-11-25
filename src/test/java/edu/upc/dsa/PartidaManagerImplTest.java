package edu.upc.dsa;

import edu.upc.dsa.domain.models.EntradaInventario;
import edu.upc.dsa.domain.models.Partida;
import edu.upc.dsa.domain.models.Usuario;
import edu.upc.dsa.domain.repository.PartidaManager;
import edu.upc.dsa.domain.repository.PartidaManagerImpl;
import edu.upc.dsa.domain.repository.TiendaManager;
import edu.upc.dsa.domain.repository.TiendaManagerImpl;
import edu.upc.dsa.domain.repository.UsuarioManager;
import edu.upc.dsa.domain.repository.UsuarioManagerImpl;
import edu.upc.dsa.dto.RankingTO;
import edu.upc.dsa.exceptions.MuseoNoEncontrado;
import edu.upc.dsa.exceptions.ObjetosNoDisponibles;
import edu.upc.dsa.exceptions.UsuarioNoEncontrado;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class PartidaManagerImplTest {
    UsuarioManager um;
    TiendaManager tm;
    PartidaManager pm;
    Usuario vera;
    HashMap<Integer, Integer> sinObjetos;

    @Before
    public void setUp() throws Exception {
        um = UsuarioManagerImpl.getInstance();
        tm = TiendaManagerImpl.getInstance();
        pm = PartidaManagerImpl.getInstance();
        vera = um.registrar("vera", "vera@mail.com", "1234");
        sinObjetos = new HashMap<>();
    }

    @After
    public void tearDown() {
        pm.clear();
        tm.clear();
        um.clear();
    }

    @Test
    public void testPartidaExito() throws Exception {
        Partida p = pm.registrarPartida(vera.getId(), 1, true, 184, sinObjetos);
        Assert.assertEquals(150, p.getMonedasGanadas());
        Assert.assertEquals(10, p.getFecha().length());
        Assert.assertEquals(250, um.getUsuario(vera.getId()).getMonedas());
        Assert.assertEquals(1, pm.numPartidas());
    }

    @Test
    public void testPartidaFallida() throws Exception {
        Partida p = pm.registrarPartida(vera.getId(), 2, false, 60, sinObjetos);
        Assert.assertEquals(0, p.getMonedasGanadas());
        Assert.assertEquals(100, um.getUsuario(vera.getId()).getMonedas());
        Assert.assertEquals(1, pm.numPartidas());
    }

    @Test
    public void testObjetosUsados() throws Exception {
        tm.comprar(vera.getId(), 1, 2);
        HashMap<Integer, Integer> usados = new HashMap<>();
        usados.put(1, 1);

        pm.registrarPartida(vera.getId(), 1, false, 30, usados);
        List<EntradaInventario> inventario = tm.getInventario(vera.getId());
        Assert.assertEquals(1, inventario.size());
        Assert.assertEquals(1, inventario.get(0).getCantidad());

        // al llegar a 0 desaparece del inventario
        pm.registrarPartida(vera.getId(), 1, true, 30, usados);
        Assert.assertTrue(tm.getInventario(vera.getId()).isEmpty());
    }

    @Test
    public void testNoEncontrado() {
        Assert.assertThrows(MuseoNoEncontrado.class, () -> pm.registrarPartida(vera.getId(), 99, true, 10, sinObjetos));
        Assert.assertThrows(UsuarioNoEncontrado.class, () -> pm.registrarPartida(99, 1, true, 10, sinObjetos));
        Assert.assertEquals(0, pm.numPartidas());
    }

    @Test
    public void testObjetosNoDisponibles() throws Exception {
        tm.comprar(vera.getId(), 1, 1);

        HashMap<Integer, Integer> demasiados = new HashMap<>();
        demasiados.put(1, 2);
        Assert.assertThrows(ObjetosNoDisponibles.class, () -> pm.registrarPartida(vera.getId(), 1, true, 10, demasiados));

        HashMap<Integer, Integer> noLoTiene = new HashMap<>();
        noLoTiene.put(2, 1);
        Assert.assertThrows(ObjetosNoDisponibles.class, () -> pm.registrarPartida(vera.getId(), 1, true, 10, noLoTiene));

        // no ha cambiado nada
        Assert.assertEquals(50, um.getUsuario(vera.getId()).getMonedas());
        Assert.assertEquals(1, tm.getInventario(vera.getId()).get(0).getCantidad());
        Assert.assertEquals(0, pm.numPartidas());
    }

    @Test
    public void testRanking() throws Exception {
        Usuario nico = um.registrar("nico", "nico@mail.com", "1234");
        Usuario ana = um.registrar("ana", "ana@mail.com", "1234");
        Usuario pepe = um.registrar("pepe", "pepe@mail.com", "1234");

        // vera: museo 1 dos veces (cuenta una) y museo 2 -> 2 museos, 350 s
        pm.registrarPartida(vera.getId(), 1, true, 100, sinObjetos);
        pm.registrarPartida(vera.getId(), 1, true, 50, sinObjetos);
        pm.registrarPartida(vera.getId(), 2, true, 200, sinObjetos);
        // nico: 2 museos, 180 s -> gana a vera por tiempo
        pm.registrarPartida(nico.getId(), 1, true, 80, sinObjetos);
        pm.registrarPartida(nico.getId(), 2, true, 100, sinObjetos);
        pm.registrarPartida(nico.getId(), 2, false, 999, sinObjetos);
        // ana: 1 museo
        pm.registrarPartida(ana.getId(), 1, true, 30, sinObjetos);
        // pepe: solo ha perdido -> no sale
        pm.registrarPartida(pepe.getId(), 1, false, 10, sinObjetos);

        List<RankingTO> ranking = pm.getRanking(10);
        Assert.assertEquals(3, ranking.size());
        Assert.assertEquals("nico", ranking.get(0).getNombre());
        Assert.assertEquals(1, ranking.get(0).getPosicion());
        Assert.assertEquals(2, ranking.get(0).getMuseosCompletados());
        Assert.assertEquals(180, ranking.get(0).getTiempoTotal());
        Assert.assertEquals("vera", ranking.get(1).getNombre());
        Assert.assertEquals(2, ranking.get(1).getMuseosCompletados());
        Assert.assertEquals(350, ranking.get(1).getTiempoTotal());
        Assert.assertEquals("ana", ranking.get(2).getNombre());
        Assert.assertEquals(3, ranking.get(2).getPosicion());

        Assert.assertEquals(2, pm.getRanking(2).size());
    }
}