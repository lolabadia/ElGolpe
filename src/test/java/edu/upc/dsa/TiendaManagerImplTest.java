package edu.upc.dsa;

import edu.upc.dsa.domain.models.Compra;
import edu.upc.dsa.domain.models.EntradaInventario;
import edu.upc.dsa.domain.models.Usuario;
import edu.upc.dsa.domain.repository.TiendaManager;
import edu.upc.dsa.domain.repository.TiendaManagerImpl;
import edu.upc.dsa.domain.repository.UsuarioManager;
import edu.upc.dsa.domain.repository.UsuarioManagerImpl;
import edu.upc.dsa.exceptions.MonedasInsuficientes;
import edu.upc.dsa.exceptions.ObjetoNoEncontrado;
import edu.upc.dsa.exceptions.UsuarioNoEncontrado;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TiendaManagerImplTest {
    UsuarioManager um;
    TiendaManager tm;
    Usuario vera;

    @Before
    public void setUp() throws Exception {
        um = UsuarioManagerImpl.getInstance();
        tm = TiendaManagerImpl.getInstance();
        vera = um.registrar("vera", "vera@mail.com", "1234");
    }

    @After
    public void tearDown() {
        tm.clear();
        um.clear();
    }

    @Test
    public void testCatalogo() {
        Assert.assertEquals(3, tm.getObjetos().size());
        Assert.assertEquals("Ganzúa", tm.getObjeto(1).getNombre());
        Assert.assertEquals(50, tm.getObjeto(1).getPrecio());
    }

    @Test
    public void testComprar() throws Exception {
        Compra c = tm.comprar(vera.getId(), 1, 2);
        Assert.assertEquals(100, c.getPrecioTotal());
        Assert.assertEquals(0, um.getUsuario(vera.getId()).getMonedas());
        Assert.assertEquals(1, tm.numCompras());

        List<EntradaInventario> inventario = tm.getInventario(vera.getId());
        Assert.assertEquals(1, inventario.size());
        Assert.assertEquals(1, inventario.get(0).getObjetoId());
        Assert.assertEquals(2, inventario.get(0).getCantidad());
    }

    @Test
    public void testMonedasInsuficientes() {
        Assert.assertThrows(MonedasInsuficientes.class, () -> tm.comprar(vera.getId(), 3, 1));
        Assert.assertEquals(100, um.getUsuario(vera.getId()).getMonedas());
        Assert.assertEquals(0, tm.numCompras());
    }

    @Test
    public void testObjetoNoEncontrado() {
        Assert.assertThrows(ObjetoNoEncontrado.class, () -> tm.comprar(vera.getId(), 99, 1));
        Assert.assertThrows(UsuarioNoEncontrado.class, () -> tm.comprar(99, 1, 1));
    }

    @Test
    public void testCantidadInvalida() {
        Assert.assertThrows(IllegalArgumentException.class, () -> tm.comprar(vera.getId(), 1, 0));
        Assert.assertThrows(IllegalArgumentException.class, () -> tm.comprar(vera.getId(), 1, -1));
        Assert.assertEquals(100, um.getUsuario(vera.getId()).getMonedas());
    }

    @Test
    public void testInventarioVacio() throws Exception {
        Assert.assertTrue(tm.getInventario(vera.getId()).isEmpty());
        Assert.assertThrows(UsuarioNoEncontrado.class, () -> tm.getInventario(99));
    }
}