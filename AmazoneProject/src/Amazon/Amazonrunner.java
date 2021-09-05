package Amazon;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Amazonrunner {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		Amazonmethod m1=new Amazonmethod();
		m1.WriteTestresult("Module Name ", " Test Result ", " Comments");
		m1.Goamazon();
		
		Boolean Loginresult=m1.login();
		
		if(Loginresult==true)
		{
			m1.WriteTestresult("Login", "Pass", "Login sucessfully");
		}
		else
		{
			m1.WriteTestresult("Login", "Fail", "Error:"+Thread.currentThread().getStackTrace());
		}
		Boolean Searchresult = m1.SearchProduct();
		if(Searchresult==true) {
			m1.WriteTestresult("Search Product", "Pass", "Product serach sucessfully");
		}
		else
		{
			m1.WriteTestresult("Search Product", "Fail", "Error:"+Thread.currentThread().getStackTrace());
		}
	
	m1.CloseBrowser();
		
}

}
