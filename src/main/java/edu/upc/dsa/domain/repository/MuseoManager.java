package edu.upc.dsa.domain.repository;

import edu.upc.dsa.domain.models.Museo;

// Minimo para el issue #10. La lista de museos (GET /museos) y los vigilantes son del issue #9.
public interface MuseoManager {

    Museo getMuseo(int id);
}