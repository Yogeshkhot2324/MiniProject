package Amazon;

import java.io.File;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;


public class Amazonmethod {
	public static WebDriver driver;
	//Read product list from excel file.
	public String [] readexcel() throws IOException 
	{
		FileInputStream fs= new FileInputStream("C:\\Users\\shital_Khot\\Desktop\\Yogesh Training\\Productlist.xlsx");
		XSSFWorkbook wb=new XSSFWorkbook(fs);
		XSSFSheet sheet=wb.getSheetAt(0);
		
		// total row in excel file.
	    int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();
	  //array to store product
	  		String [] Product = new String[rowCount+1];
		 for (int i = 0; i <rowCount+1; i++) {
			 XSSFRow row = sheet.getRow(i);
			 for (int j = 0; j < row.getLastCellNum(); j++) {
				 //insert cell value into array.
				 Product[i]=row.getCell(j).getStringCellValue(); 
		        }
		 }
		 if(Product.length==0)
		 {
			WriteTestresult("Read Excel File", "Fail", "Excel file read failed.");
		 }
		 else
		 {
			 WriteTestresult("Read Excel File", "Pass", "Excel file read sucessfully.");
		 }
		return Product;
		
	}
	//redirct to URL.
	public void Goamazon() throws IOException
	{
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\shital_Khot\\Desktop\\Yogesh Training\\chromedriver_win32 (2)\\chromedriver.exe");  
        
        // Instantiate a ChromeDriver class.     
		   driver=new ChromeDriver();
		   //read page URL from Text file.
		   String [] URL=ReadUsername_Password();
		   for(int i=0;i<URL.length;i++)
		   {
			   //Navigate to url.
			   driver.navigate().to(URL[i]);
			   break;
		   }
		   //maximize window.
		   driver.manage().window().maximize();
		   if(driver.getTitle().equals("Amazon.com. Spend less. Smile more."))
		   {
			   WriteTestresult("Go to URL", "Pass", "URL lauch sucessfully");
		   }
		   else
		   {
			   WriteTestresult("Go to URL", "Fail", "URL launch failed");
		   }
	}
	 public Boolean login() throws InterruptedException, IOException
	 {
		 //Read username and password from text file.
		 String [] URL=ReadUsername_Password();
		 String userName="",Password="";
	 
		 for(int i=1;i<URL.length;i++)
		 {
			 userName=URL[i];
			 i++;
			 Password=URL[i];
		 }
			Thread.sleep(1000);

			  //click on sign in.
			 driver.findElement(By.xpath("//span[@id='nav-link-accountList-nav-line-1']")).click();
			 Thread.sleep(1000);
			 //send username for login
			 driver.findElement(By.xpath("//input[@id='ap_email']")).sendKeys(userName);
			 Thread.sleep(1000);
			 //Click on Continue button.
			 driver.findElement(By.xpath("//input[@id='continue']")).click();
			//send password for login.
			 driver.findElement(By.xpath("//input[@id='ap_password']")).sendKeys(Password);
			 Thread.sleep(1000);
			 //click on submit button.
			 driver.findElement(By.xpath("//input[@id='signInSubmit']")).click();
			 
			 String eTitle = "Hello, Sign in";
			 //Get sign in text. If user logged in text must be "hello,username".
			 String aTitle=driver.findElement(By.xpath("//span[@id='nav-link-accountList-nav-line-1']")).getText();
			 //If aTitle and eTitle is equal then loggin failed return false otherwise true.
			 if(eTitle.equals(aTitle))
			 {
				 return false;
			 }
			else
				{
				return true;
				}
		
	 }
	 //Serach products.
	 public Boolean SearchProduct() throws IOException, InterruptedException
		{
		 List<String> productName = new ArrayList<String>();
		 //Get list of products from excel file.
		 String [] SerachProduct = readexcel();
		 //Loop for all product
		 for(int i=0; i<SerachProduct.length;i++)
			{
			 	//Seach box .
				WebElement serachBox = driver.findElement(By.xpath("//input[@id='twotabsearchtextbox']"));
				Actions act=new Actions(driver);
				act.moveToElement(serachBox).build().perform();
				serachBox.clear();
				//send first product to search box.
				Thread.sleep(1000);
				serachBox.sendKeys(SerachProduct[i]);
				//Click on search button.
				driver.findElement(By.xpath("//input[@id='nav-search-submit-button']")).click();
				
				//Get first 5 results.
				for(int i1=1; i1<=5; i1++)
				{	
						//get product name.
					    List<WebElement> allProductsName = driver.findElements(By.xpath("(//a[@class='a-link-normal a-text-normal'])["+i1+"]"));
					    for(WebElement w : allProductsName) {
					        productName.add(w.getText());
					    }
					}
				//Print first 5 results on console.  
				System.out.print(productName);
				//Write first 5 result in text file.
				WriteProductFile(productName,SerachProduct[i]);
				//remove
				List remList = new ArrayList();
				for(int j=0;j<=4;j++){
				remList.add(productName.get(j));
				}
				productName.removeAll(remList);
			}
			if(productName.isEmpty()) {
				return true;
			}
			else
			{
				return false;
			}
		
	}
	//Write test result for modules in text file.
	public void WriteTestresult(String moduleName, String result, String comment) throws IOException {
		
		 @SuppressWarnings("deprecation")
			String TestFile = "D:\\TestResult.txt";
			  File FC = new File(TestFile);//Created object of java File class.
			  //Writing In to file.
			  //Create Object of java FileWriter and BufferedWriter class.
			  
			  FileWriter FW = new FileWriter(TestFile,true);
			  BufferedWriter BW = new BufferedWriter(FW);
			  
			  BW.newLine();//To write next string on new line.
			  BW.append(moduleName+" || ");
			  BW.append(result+" || ");
			  BW.append(comment);
			  
			  BW.close();
			 
			  
		
	}
	//Write first 5 results for all product in text file.
	public void WriteProductFile(List<String> productList,String serachProduct) throws IOException
	{
		 @SuppressWarnings("deprecation")
			String TestFile = "D:\\ProductList.txt";
			  File FC = new File(TestFile);//Created object of java File class.
			  //Writing In to file.
			  //Create Object of java FileWriter and BufferedWriter class.
			  
			  FileWriter FW = new FileWriter(TestFile,true);
			  BufferedWriter BW = new BufferedWriter(FW);
			  	BW.newLine();
				BW.append(serachProduct +" - ");
				
				  
				for (String product : productList) {
					BW.newLine();
					BW.append(product);
					BW.newLine();
				
			  }
				
	 BW.close();
}
	//Read URL, UserName and password from text file.
	public String[] ReadUsername_Password() throws IOException
	{
		 File file1=new File("C:\\Users\\shital_Khot\\Desktop\\Yogesh Training\\Username_Password.txt");
		 FileReader fr=new FileReader(file1);
		 String [] data= new String[3];
		 Object strCurrentLine;
		 BufferedReader br = new BufferedReader(fr);
		for(int i=0;i<data.length;i++)
		{
			while ((strCurrentLine = br.readLine()) != null) {
			
				data[i]=(String) strCurrentLine;
				i++;
			}
		}
		return data;
			    
	}
	public void CloseBrowser() throws InterruptedException
	{
		Thread.sleep(1000);
		driver.close();
	}
}