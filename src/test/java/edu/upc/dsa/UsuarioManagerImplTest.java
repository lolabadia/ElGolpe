package edu.upc.dsa;

import edu.upc.dsa.domain.models.Usuario;
import edu.upc.dsa.domain.repository.UsuarioManager;
import edu.upc.dsa.domain.repository.UsuarioManagerImpl;
import edu.upc.dsa.exceptions.CredencialesIncorrectas;
import edu.upc.dsa.exceptions.UsuarioYaExiste;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UsuarioManagerImplTest {
    UsuarioManager um;

    @Before
    public void setUp() throws Exception {
        um = UsuarioManagerImpl.getInstance();
        um.registrar("vera", "vera@mail.com", "1234");
    }

    @After
    public void tearDown() {
        um.clear();
    }

    @Test
    public void testRegistrar() throws Exception {
        Usuario u = um.registrar("nico", "nico@mail.com", "abcd");
        Assert.assertEquals(2, u.getId());
        Assert.assertEquals(100, u.getMonedas());
        Assert.assertEquals(2, um.numUsuarios());
        Assert.assertThrows(UsuarioYaExiste.class, () -> um.registrar("vera", "otro@mail.com", "x"));
    }

    @Test
    public void testLogin() throws Exception {
        Usuario u = um.login("vera", "1234");
        Assert.assertEquals("vera@mail.com", u.getEmail());
        Assert.assertThrows(CredencialesIncorrectas.class, () -> um.login("vera", "mala"));
        Assert.assertThrows(CredencialesIncorrectas.class, () -> um.login("nadie", "1234"));
    }
}