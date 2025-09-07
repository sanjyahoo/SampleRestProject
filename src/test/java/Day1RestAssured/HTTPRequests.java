package Day1RestAssured;

import io.restassured.http.ContentType;
import org.testng.annotations.*;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class HTTPRequests {

    int userID;

    //given()-> pre-requisite(content-type, cookies, headers etc)
    //when()-> all request URLs (get put post delete)
    //then()-> all validations(headers, cookies, response, response body etc)

    // We need to import static packages in order to use these methods

    @Test(priority = 1)
    public void getUser(){
        given()
                .when()
                .get("https://reqres.in/api/users/2")

                .then()
                .statusCode(200)
                .body("data.last_name",equalTo("Weaver"))
                .log().all();
    }

    @Test(priority = 2)
    public void createUser(){

        HashMap<String, String> map = new HashMap<>();
        map.put("name","bro");
        map.put("job","leader");

        userID = given()
                .header("x-api-key", "reqres-free-v1")
                .contentType(ContentType.JSON)
                .body(map)

                .when()
                .post("https://reqres.in/api/users")
                .jsonPath().getInt("id");
    }

    @Test(priority = 3, dependsOnMethods = "createUser")
    public void updateUser(){

        HashMap<String, String> map = new HashMap<>();
        map.put("name","not jay");
        map.put("job","not a leader");

        given()
                .header("x-api-key", "reqres-free-v1")
                .contentType(ContentType.JSON)
                .body(map)

                .when()
                .put("https://reqres.in/api/users/"+userID+"")

                .then().statusCode(200).log().all();

    }


    @Test(priority = 4, dependsOnMethods = "createUser")
    public void deleteUser(){
        given()
                .header("x-api-key", "reqres-free-v1")
                .contentType(ContentType.JSON)

                .when()
                .delete("https://reqres.in/api/users/"+userID+"")
                .then().statusCode(204).log().all();

    }
}
