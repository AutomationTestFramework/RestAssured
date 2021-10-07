package hooks;

import com.github.javafaker.Faker;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.ITestResult;
import org.testng.annotations.*;
import report.Extent;
import config.*;
import report.*;

public class hooks {
    public Faker faker = new Faker() ;
    public User userPayload=new User();
    public Extent extent = new Extent();
    public Logger logger = new Logger();

    @BeforeTest
    public void beforeTest() {
        extent.createReport();
        System.out.println("*****************USERNAME UNDER TEST**********************************");
        userPayload.setUserId(faker.idNumber().hashCode());
        userPayload.setUserName(faker.name().username());
        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        userPayload.setEmail(faker.internet().safeEmailAddress());
        userPayload.setPassword(faker.internet().password(5,10));
        userPayload.setPhone(faker.phoneNumber().cellPhone());
        System.out.println("Some Random username generated by Faker:\t"+userPayload.getUserName());
        System.out.println("**********************************************************************");
    }

    @AfterTest
    public void afterTest() throws Exception {
        extent.endReport();
    }

    @BeforeMethod
    public void beforeMethod(ITestResult result){
        logger.setLogger(extent.startTest(result.getMethod().getMethodName() + " : " + result.getMethod().getDescription()));
        logger.info("********* Test Case Started *********");
    }

    @AfterMethod
    public void getResult(ITestResult result) throws Exception{
        if(result.getStatus() == ITestResult.FAILURE){
            logger.fail("Test Case Failed is "+ result.getName());
            logger.fail("Test Case Failed is "+ result.getThrowable());
        }else if(result.getStatus() == ITestResult.SKIP){
            logger.skip("Test Case Skipped is "+ result.getName());
        }else {
            logger.pass("Test Case Passed is "+ result.getName());
        }
        // ending test
        //endTest(logger) : It ends the current test and prepares to create HTML report
        extent.endTest(logger.getLogger());
    }

}
