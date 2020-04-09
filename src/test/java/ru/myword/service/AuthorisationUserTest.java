package ru.myword.service;

import static io.restassured.RestAssured.given;

import javax.inject.Inject;

import org.apache.catalina.connector.Response;
import org.apache.commons.text.RandomStringGenerator;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import ru.myword.service.bo.User;
import ru.myword.service.controller.LoginController;
import ru.myword.service.controller.RegistrationController;
import ru.myword.service.services.UserService;

/**
 * Тестирование функционала регистрации и авторизации
 */
@ContextConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorisationUserTest extends BaseTest
{

	@Inject
	private UserService userService;

	private String loginUser;
	private String passwordUser;
	private User user;

	@Before
	public void setUp()
	{
	    RandomStringGenerator generator = new RandomStringGenerator.Builder()
            .withinRange('a', 'z').build();
        loginUser = generator.generate(6);
        passwordUser = generator.generate(15);

		user = new User();
		user.setLogin(loginUser);
		user.setPassword(passwordUser);
	}

	@After
    public void setDown()
    {
        if (user.getId() != null)
        {
            userService.delete(user);
        }
    }

	/**
	 * Тестирование успешной регистрации пользователя и присвоение id
	 */
	@Test
	public void testRegistrationUser(){
		user = given()
				.body(user)
				.when()
				.post(RegistrationController.BASE_PATH_REQUEST)
				.then()
				.statusCode(Response.SC_CREATED)
				.extract().body().as(User.class);

		Assert.assertEquals(loginUser, user.getLogin());
		Assert.assertEquals(passwordUser, user.getPassword());
		Assert.assertNotNull(user.getId());
	}

	/**
	 * Тестирование регистрации пользователя с указанием существующего логина
	 */
	@Test
	public void testDoubleRegistrationUser(){
		userService.save(user);

		User user = new User();
		user.setLogin(loginUser);
		user.setPassword(passwordUser);

		given()
				.body(user)
				.when()
				.post(RegistrationController.BASE_PATH_REQUEST)
				.then()
				.statusCode(Response.SC_CONFLICT);
	}

	/**
	 * Тестирование выдачи token пользователю
	 */
	@Test
	public void testLoginUser() throws JSONException
	{
		userService.save(user);

		String body = new JSONObject()
                .put("login", loginUser)
                .put("password", passwordUser)
                .toString();

		String token = given()
				.body(body)
				.when()
				.post(LoginController.BASE_PATH_REQUEST)
				.then()
				.statusCode(Response.SC_OK)
                .extract().body().asString();

		Assert.assertNotNull(token);
	}
}
