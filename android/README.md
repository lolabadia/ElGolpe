# El Golpe · App Android

App Android (Java) del joc El Golpe. Fa servir l'API REST del servidor d'aquest mateix repo.

## Què hi ha fet

- **Client Retrofit** (`api/ApiClient.java`, `api/ElGolpeApi.java`): una sola instància per a tota l'app, amb Gson i el log de les peticions al Logcat (filtre `okhttp`).
- **Registre** (`RegisterActivity`): crida `POST /usuarios/registro`. Comprova abans d'enviar res que hi hagi tots els camps, que el correu sigui vàlid i que les dues contrasenyes coincideixin. Mostra els errors 400 i 409 del servidor. Si el compte es crea, la sessió queda iniciada.
- **Login** (`LoginActivity`): crida `POST /usuarios/login` i mostra els errors 400 i 401. Guarda l'usuari (id, nom, email i monedes) a SharedPreferences amb `SessionManager`. Si en tornar a obrir l'app ja hi ha una sessió guardada, entra directament.
- **Pantalla principal** (`MainActivity`): de moment mostra el nom i les monedes i té un botó per tancar la sessió. Aquí s'hi enllaçaran la botiga i l'inventari.

## Com provar-ho

1. Engegueu el servidor (`Main.java`). Ha de respondre a `http://localhost:8080/dsaApp/`.
2. Obriu la carpeta `android/` amb Android Studio (File → Open) i espereu que acabi el Gradle sync.
3. Executeu l'app en un **emulador**. Allà, `10.0.2.2` és el `localhost` de l'ordinador.

Per provar-la amb un **mòbil real**:
- A `ApiClient.BASE_URL`, poseu la IP de l'ordinador (per exemple `http://192.168.1.20:8080/dsaApp/elgolpe/`).
- Al `Main.java` del servidor, poseu `BASE_URI = "http://0.0.0.0:8080/dsaApp/"` perquè accepti connexions de fora.
- El mòbil i l'ordinador han d'estar a la mateixa xarxa.

## Per a la resta de l'equip

- L'id de l'usuari que ha iniciat sessió s'agafa amb `new SessionManager(context).getUsuarioId()`.
- Les rutes noves (botiga, compres, inventari...) s'afegeixen a `ElGolpeApi`. Les rutes van sense `/` inicial, per exemple `@GET("tienda/objetos")`.
- Després d'una compra, actualitzeu les monedes amb `sessio.setMonedas(...)`.
