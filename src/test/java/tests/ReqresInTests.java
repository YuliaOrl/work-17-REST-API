package tests;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresInTests {

    /*
        1. Make POST- request to https://reqres.in/api/register
            with body { "email": "eve.holt@reqres.in", "password": "pistol" }
        2. Get response { "id": 4, "token": "QpwL5tke4Pnpja7X4" }
        3. Check expected response = actual response
     */

    @Test
    @DisplayName("Позитивная проверка регистрации пользователя")
    void checkRegisterSuccessful() {

        JSONObject requestBody = new JSONObject();
        requestBody.put("email", "eve.holt@reqres.in");
        requestBody.put("password", "pistol");

        JSONObject expectedBody = new JSONObject();
        expectedBody.put("id", 4);
        expectedBody.put("token", "QpwL5tke4Pnpja7X4");

        String actualBody = given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(requestBody.toString())
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract()
                .response().getBody().asString();

        assertEquals(expectedBody.toString(), actualBody);
    }


    /*
        1. Make GET- request to https://reqres.in/api/users/2
        2. Get response {"data":{"id":2,"email":"janet.weaver@reqres.in","first_name":"Janet","last_name":"Weaver",
        "avatar":"https://reqres.in/img/faces/2-image.jpg"},"support":{"url":"https://reqres.in/#support-heading",
        "text":"To keep ReqRes free, contributions towards server costs are appreciated!"}}
        3. Check data.id is 2,
                 data.email is janet.weaver@reqres.in,
                 data.first_name is Janet,
                 data.last_name is Weaver,
                 support.text is "To keep ReqRes free, contributions towards server costs are appreciated!"
     */

    @Test
    @DisplayName("Проверка получения информации по пользователю")
    void checkGetSingleUserTest() {
        given()
                .log().uri()
                .log().body()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .assertThat()
                .body("data.id", is(2))
                .body("data.email", is("janet.weaver@reqres.in"))
                .body("data.first_name", is("Janet"))
                .body("data.last_name", is("Weaver"))
                .body("support.text", is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }


    /*
        1. Make POST- request to https://reqres.in/api/users
            with body { "name": "Cat", "job": "walk around the house" }
        2. Get response { "name": "Cat", "job": "walk around the house", "id": "516", "createdAt": "2022-10-11T13:59:46.182Z" }
        3. Check name is Cat,
                 job is "walk around the house"
     */

    @Test
    @DisplayName("Тест на создание имени и работы пользователя")
    void checkPostCreateTest() {

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "Cat");
        requestBody.put("job", "walk around the house");

        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(requestBody.toString())
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("Cat"))
                .body("job", is("walk around the house"));
    }

    @Test
    @DisplayName("Негативная проверка создания имени и работы пользователя")
    void checkNegativePostCreateTest() {
        given()
                .log().uri()
                .log().body()
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(415);
    }


    /*
        1. Make PUT- request to https://reqres.in/api/users/2
            with body { "name": "Kitty", "job": "sleep all day" }
        2. Get response { "name": "Kitty", "job": "sleep all day", "updatedAt": "2022-10-11T13:01:46.385Z" }
        3. Check name is Kitty,
                 job is "sleep all day"
     */

    @Test
    @DisplayName("Тест на редактирование имени и работы пользователя")
    void checkPutUpdateTest() {

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "Kitty");
        requestBody.put("job", "sleep all day");

        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(requestBody.toString())
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is("Kitty"))
                .body("job", is("sleep all day"));
    }


    /*
        1. Make DELETE- request to https://reqres.in/api/users/2
        2. Check status is 204
     */

    @Test
    @DisplayName("Тест на удаление пользователя")
    void checkDeleteTest() {

        given()
                .log().uri()
                .log().body()
                .when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }
}

