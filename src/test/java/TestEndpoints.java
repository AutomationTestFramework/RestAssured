import java.util.HashMap;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
//import api.engine.endpoints.UserEndpoints;
//import api.payload.User;
import io.restassured.response.Response;

/**
 *
 * Perform API Testing for the services using CRUD Design Pattern
 */
    public class TestEndpoints {
        Faker faker;
        User userPayload;
        @BeforeTest
        public void beforeTest()
        {
            System.out.println("*****************USERNAME UNDER TEST**********************************");
            faker=new Faker();
            userPayload=new User();
            userPayload.setUserId(faker.idNumber().hashCode());
            userPayload.setUserName(faker.name().username());
            userPayload.setFirstName(faker.name().firstName());
            userPayload.setLastName(faker.name().lastName());
            userPayload.setEmail(faker.internet().safeEmailAddress());
            userPayload.setPassword(faker.internet().password(5,10));
            userPayload.setPhone(faker.phoneNumber().cellPhone());
            System.out.println("Some Random username genereated by Faker:\t"+userPayload.getUserName());
            System.out.println("**********************************************************************");
        }

        @Test(priority = 1)
        public void testPostUser()
        {
            Map<String,Object> bodyParams=new HashMap<String,Object>();
            bodyParams.put("id",userPayload.getUserId());
            bodyParams.put("username",userPayload.getUserName());
            bodyParams.put("firstName",userPayload.getFirstName());
            bodyParams.put("lastName",userPayload.getLastName());
            bodyParams.put("email",userPayload.getEmail());
            bodyParams.put("password",userPayload.getPassword());
            bodyParams.put("phone",userPayload.getPhone());
            bodyParams.put("userStatus",0);
            String payload=new Gson().toJson(bodyParams);
            System.out.println("*************************{POST}***************************************");
            Response response=UserEndpoints.createUser(payload);
            response.then().log().all();
            Assert.assertEquals(response.getStatusCode(),200);
            Assert.assertTrue(response.getStatusLine().contains("OK"));
            System.out.println("**********"  +this.userPayload.getUserName()+" is created ************");
        }
        @Test(priority = 2)
        public void testGetUserByName() {
            System.out.println("*************************{GET}****************************************");
            Response response=UserEndpoints.readUser(this.userPayload.getUserName());
            response.then().log().body().statusCode(200);
            System.out.println("************  "+this.userPayload.getUserName()+" is fetched **********");
        }

        @Test(priority = 3)
        public void testUpdateUserByName() {
            Map<String,Object> bodyParams=new HashMap<String,Object>();
            bodyParams.put("id",userPayload.getUserId());
            bodyParams.put("username",userPayload.getUserName());
            /**
             * update starts from here based on existing user name
             */
            bodyParams.put("firstName",userPayload.getFirstName()+" is my first name");
            bodyParams.put("lastName",userPayload.getLastName()+" is my last name");
            bodyParams.put("email",userPayload.getEmail()+" is my email");
            bodyParams.put("password",userPayload.getPassword()+" is my password");
            bodyParams.put("phone",userPayload.getPhone()+" is my phone number");
            bodyParams.put("userStatus",1);
            String payload=new Gson().toJson(bodyParams);
            System.out.println("*************************{UPDATE}************************************");
            Response response=UserEndpoints.updateUser(this.userPayload.getUserName(), payload);
            response.then().log().body().statusCode(200);
            Response afterUpdateResponse=UserEndpoints.readUser(this.userPayload.getUserName());
            afterUpdateResponse.then().log().body().statusCode(200);
            System.out.println("*********  "+this.userPayload.getUserName()+" is updated ************");

        }
        @Test(priority = 4)
        public void testDeleteUserByName() {

            System.out.println("*************************{DELETE}************************************");

            Response response=UserEndpoints.deleteUser(this.userPayload.getUserName());
            response.then().log().body().statusCode(200);

            System.out.println("********  "+this.userPayload.getUserName()+" is deleted *************");
        }

    }
