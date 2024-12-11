package org.ibs;

import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.pojos.ProductDataPojo;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.reset;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestApiProductsTest {

    private static final CookieFilter cookieFilter = new CookieFilter();

    @BeforeAll
    static void setUp() {
        Specifications.installSpecification(
                Specifications.requestSpecification("http://localhost:8080"),
                Specifications.responseSpecification(200)
        );
    }

    @AfterAll
    static void resetSandBox() {
        given()
                .filter(cookieFilter)
                .basePath("/api/data/reset")
                .when()
                .post();
        reset();

        given()
                .filter(cookieFilter)
                .basePath("/api/food")
                .when()
                .contentType(ContentType.JSON)
                .get("")
                .then()
                .log().all()
                .extract()
                .jsonPath().getList("", ProductDataPojo.class);
    }

    @Test
    @Order(1)
    void addNotExoticVeg() {
        String data = "{\"name\": \"Огурец\", \"type\": \"VEGETABLE\", \"exotic\": false}";

        given()
                .filter(cookieFilter)
                .basePath("/api/food")
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post()
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Test
    @Order(2)
    void addExoticVeg() {
        String data = "{\"name\": \"Батат\", \"type\": \"VEGETABLE\", \"exotic\": true}";

        given()
                .filter(cookieFilter)
                .basePath("/api/food")
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post()
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Test
    @Order(3)
    void addNotExoticFruit() {
        String data = "{\"name\": \"Виноград\", \"type\": \"FRUIT\", \"exotic\": false}";

        given()
                .filter(cookieFilter)
                .basePath("/api/food")
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post()
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Test
    @Order(4)
    void addExoticFruit() {
        String data = "{\"name\": \"Манго\", \"type\": \"FRUIT\", \"exotic\": true}";

        given()
                .filter(cookieFilter)
                .basePath("/api/food")
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post()
                .then()
                .log().all()
                .extract()
                .response();
    }

    @Test
    @Order(5)
    void getProductList(){
        List<ProductDataPojo> products = given()
                .filter(cookieFilter)
                .basePath("/api/food")
                .when()
                .contentType(ContentType.JSON)
                .get("")
                .then()
                .log().all()
                .extract()
                .jsonPath().getList("", ProductDataPojo.class);

        // Проверяем наличие всех добавленных продуктов
        Assertions.assertTrue(products.stream().anyMatch(
                product -> "Огурец".equals(product.getName()) && "VEGETABLE".equals(product.getType())
        ), "Список продуктов должен содержать 'Огурец'");

        Assertions.assertTrue(products.stream().anyMatch(
                product -> "Батат".equals(product.getName()) && "VEGETABLE".equals(product.getType())
        ), "Список продуктов должен содержать 'Батат'");

        Assertions.assertTrue(products.stream().anyMatch(
                product -> "Виноград".equals(product.getName()) && "FRUIT".equals(product.getType())
        ), "Список продуктов должен содержать экзотический 'Виноград'");

        Assertions.assertTrue(products.stream().anyMatch(
                product -> "Манго".equals(product.getName()) && "FRUIT".equals(product.getType())
        ), "Список продуктов должен содержать 'Манго'");
    }
}
