package de.telran.g_10_170123_e_be_security.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private int port;

    private TestRestTemplate template = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    // 1 сценарий:
    // Не аутентифицируемся. Вызываем метод, требующий аутентификации.
    // Ожидание - сервер должен вернуть ошибку UNAUTHORIZED

    @Test
    public void getUserByNameWithoutAuthentication() {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        String url = "http://localhost:" + port + "/user/name/Vanya";

        ResponseEntity<String> response = template
                .exchange(url, HttpMethod.GET, entity, String.class);

        HttpStatusCode expected = HttpStatus.UNAUTHORIZED;
        HttpStatusCode actual = response.getStatusCode();

        assertEquals(expected, actual);
        assertNull(response.getBody());
    }

    // 2 сценарий:
    // Авторизуемся. Вызываем метод, требующий авторизации (получение пользователя по имени).
    // Ожидание - статус ОК и объект пользователя в теле ответа.

    @Test
    public void getUserByNameWitAuthentication() {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        String url = "http://localhost:" + port + "/user/name/Petya";

        ResponseEntity<String> response = template
                .withBasicAuth("Vanya", "qwe")
                .exchange(url, HttpMethod.GET, entity, String.class);

        HttpStatusCode expected = HttpStatus.OK;
        HttpStatusCode actual = response.getStatusCode();

        String expectedBody = "{" +
                "\"id\":2," +
                "\"username\":\"Petya\"," +
                "\"password\":\"$2a$10$du1KnUaOe/rN0Y9r8eGLK.ZEVQCuxe8/jwV23CZWnt7IbhKP0a3HG\"," +
                "\"authorities\":[" +
                "{" +
                "\"id\":2," +
                "\"name\":\"ROLE_USER\"," +
                "\"authority\":\"ROLE_USER\"" +
                "}" +
                "]" +
                "}";

        String actualBody = response.getBody();

        assertEquals(expected, actual);
        assertEquals(expectedBody, actualBody);
    }

    // 3 сценарий:
    // Авторизуемся под ролью USER. Вызываем метод, требующий роли ADMIN.
    // Ожидание - сервер должен вернуть ошибку FORBIDDEN.

    @Test
    public void tryToAddUserWithoutRequiredRole() {

        String body = "{" +
                "\"username\":\"Vanya\"," +
                "\"password\":\"qwe\"" +
                "}";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        String url = "http://localhost:" + port + "/user/add";

        ResponseEntity<String> response = template
                .withBasicAuth("Sidor", "qwe")
                .exchange(url, HttpMethod.POST, entity, String.class);

        HttpStatusCode expected = HttpStatus.FORBIDDEN;
        HttpStatusCode actual = response.getStatusCode();

        assertEquals(expected, actual);
    }
}