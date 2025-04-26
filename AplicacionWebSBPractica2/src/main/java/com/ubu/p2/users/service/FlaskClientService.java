package com.ubu.p2.users.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * hace un GET y devuelve el JSON (o lanza una excepción si falla).
 */
@Service
public class FlaskClientService {

    //baseUrl se coge de application.properties o si no existe toma el valor por defecto python-api:5000 (Docker)
    @Value("${flask.base-url:http://python-api:5000}") // python-api:5000 / localhost:5000
    private String baseUrl;

    private final RestTemplate rest;

    public FlaskClientService(RestTemplate rest) {
        this.rest = rest;
    }

    public Object get(String path) {
        // path llegará como "/api/db/users" ...
        // RestTemplate convierte JSON en Map/List automáticamente
        return rest.getForObject(baseUrl + path, Object.class);
    }
}
