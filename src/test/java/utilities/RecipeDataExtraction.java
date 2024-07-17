package utilities;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import base.BaseTest;

public class RecipeDataExtraction {
	//WebDriver driver;
	ReadExcel readExcel = new ReadExcel();
	List<String> recipeCategoryCheckList = readExcel.getRecipeFilterItemsList("Recipe Categories",2);
	List<String> foodCategoryCheckList = readExcel.getRecipeFilterItemsList("Recipe Categories",0);
	List<String> cuisineCategoryCheckList = readExcel.getRecipeFilterItemsList("Recipe Categories",1);


		public Map<String, String> recipeData(String url) { //String url //Map<String, String>
			Map<String,String> dataMappedToHeader = new LinkedHashMap<String, String>(); 

		//String url = "https://www.tarladalal.com/aam-chana-achar-rajasthani-pickle-3904r";
		//To collect the column headers as list
		
			Document document;
			try {
				document = Jsoup.connect(url).get();
			
			
			//Recipe ID
		String recipeId=document.selectXpath("//form[@name='aspnetForm']").attr("action").split("recipeid=")[1];
		//pojo.setRecipeId(recipeId);
		//System.out.println("Recipe Id is: "+recipeId);
		dataMappedToHeader.put("Recipe ID",recipeId);
		
				
		//Recipe Name
		String recipeName=document.getElementById("ctl00_cntrightpanel_lblRecipeName").text();
		//System.out.println("Recipe name is: "+recipeName);
		//pojo.setRecipeName(recipeName);	
		dataMappedToHeader.put("Recipe Name",recipeName);
		
		
		//Ingredients
		int ingreSize = document.selectXpath("//span[@itemprop='recipeIngredient']").size();
		String ingredients = "";
		for (int k = 1; k <= ingreSize; k++) {
			String eachIngrednt = document.selectXpath("//span[@itemprop='recipeIngredient'][" + k + "]").text();
			ingredients = ingredients + eachIngrednt + "|";
		}
		//System.out.println("Ingredients are: "+ingredients);
		//pojo.setIngredients(ingredients);
		dataMappedToHeader.put("Ingredients",ingredients);
				
		
		//Preparation time
		String prepTime = document.selectXpath("//time[@itemprop='prepTime']").text();
		//System.out.println("Preparation time is: "+prepTime);
		//pojo.setPrepTime(prepTime);
		dataMappedToHeader.put("Preparation Time",prepTime);
		
		
		//Cooking time
		String cookTime = document.selectXpath("//time[@itemprop='cookTime']").text();
		//System.out.println("Cooking time is: "+cookTime);
		//pojo.setCookTime(cookTime);
		dataMappedToHeader.put("Cooking Time",cookTime);
		
		
		//Tags
		List<Element> recipeTagList=document.selectXpath("//div[@id='recipe_tags']/a/span");
		String tags= ""; 
		for (Element recipeTag: recipeTagList) {
			String tagConcat= recipeTag.text();			
			tags= tags+tagConcat+"|";
		}
		if (!tags.isEmpty()) {
			tags = tags.substring(0, tags.length() - 1);
		}
		//System.out.println("Tags are: "+tags);
		//pojo.setTags(tags);
		dataMappedToHeader.put("Tag",tags);
		
		
		//Recipe category
				String recipeCategoryNames="";
		for (Element recipeTagName: recipeTagList) {
			for (String recipeCategory : recipeCategoryCheckList) {
				if (recipeTagName.text().contains(recipeCategory)) {
					 if (!recipeCategoryNames.contains(recipeCategory + "|")) {
			                recipeCategoryNames = recipeCategoryNames + recipeCategory + "|";
			            }					 
		        }	
			}
		}
		if (!recipeCategoryNames.isEmpty()) {
		    recipeCategoryNames = recipeCategoryNames.substring(0, recipeCategoryNames.length() - 1);
		}
		//System.out.println("The recipe category is: "+recipeCategoryNames);
		//pojo.setRecipeCategoryNames(recipeCategoryNames);
		dataMappedToHeader.put("Recipe Category",recipeCategoryNames);
		
		
		//Food category
		String foodCategoryNames="";
		for (Element recipeTagElement : recipeTagList) {		    		    
		    for (String foodCategory : foodCategoryCheckList) {
		        if (recipeTagElement.text().toLowerCase().contains(foodCategory.toLowerCase())) {
		        	if (!foodCategoryNames.contains(foodCategory + "|")) {
		        		foodCategoryNames = foodCategoryNames + foodCategory + "|";
		            }		            
		        	//break; 
		        }
		    }
	}
		if (!foodCategoryNames.isEmpty()) {
			foodCategoryNames = foodCategoryNames.substring(0, foodCategoryNames.length() - 1);
		}
		//System.out.println("The food category is: "+foodCategoryNames);
		//pojo.setFoodCategoryNames(foodCategoryNames);
		dataMappedToHeader.put("Food Category",foodCategoryNames);
		
		
	//No of servings
		String noOfServings = document.selectXpath("//span[@itemprop='recipeYield']").text();
		//System.out.println("No of servings is: "+noOfServings);
		//pojo.setNoOfServings(noOfServings);
		dataMappedToHeader.put("No of servings",noOfServings);
		
		
		//Cuisine category
		String cuisineCategoryNames="";
		for (Element recipeTagName: recipeTagList) {
			for (String cuisineCategory : cuisineCategoryCheckList) {
				if (recipeTagName.text().toLowerCase().contains(cuisineCategory.toLowerCase())) {
					if (!cuisineCategoryNames.contains(cuisineCategory + "|")) {
						cuisineCategoryNames = cuisineCategoryNames + cuisineCategory + "|";
		            }
		        	//break; 
		        }	
			}
		}
		if (!cuisineCategoryNames.isEmpty()) {
			cuisineCategoryNames = cuisineCategoryNames.substring(0, cuisineCategoryNames.length() - 1);
		}
    	//System.out.println("The cuisine category is: "+cuisineCategoryNames);		            
    	//pojo.setCuisineCategoryNames(cuisineCategoryNames);
    	dataMappedToHeader.put("Cuisine category",cuisineCategoryNames);
    	
    	
		//Recipe Description
    	String recDescrptn = document.getElementById("ctl00_cntrightpanel_lblDesc").text();
		//System.out.println("Recipe description is: "+recDescrptn);
		//pojo.setRecDescrptn(recDescrptn);
		dataMappedToHeader.put("Recipe Description",recDescrptn);
		
		
		//Preparation method
    	List<Element> methodList = document.selectXpath("//div[@id='recipe_small_steps']/span/ol/li");
    	String prepMethods = "";
		for (Element methods : methodList) {
			prepMethods = prepMethods + methods.text() + "\n";
		}
		//System.out.println("Preparation method is: "+prepMethods);
		//pojo.setPrepMethods(prepMethods);
		dataMappedToHeader.put("Preparation method",prepMethods);
		
		
		//Nutrient values
		List<String> nutrientList = new ArrayList<String>();
		int rows = document.selectXpath("//table[@id='rcpnutrients']//tr").size();
		int cols = document.selectXpath("//table[@id='rcpnutrients']//tr[1]/td").size();
		for (int r=1; r<=rows; r++) {
			String nutrientVal = "";
			for (int c=1; c<=cols; c++) {
				String nutrients = 
						document.selectXpath("//table[@id='rcpnutrients']//tr["+r+"]/td["+c+"]").text();
				nutrientVal = nutrientVal+nutrients;
				}
			nutrientList.add(nutrientVal);
		}
		String nutrientVal="";
		for (String nutrients : nutrientList) {
			nutrientVal = nutrientVal+nutrients+"|";
		}
		//System.out.println("Nutrient value is: "+nutrientVal);
		//pojo.setNutrientVal(nutrientVal);
		dataMappedToHeader.put("Nutrient values",nutrientVal);
		
		
		//Recipe url		
		dataMappedToHeader.put("Recipe URL",url);
		
		//System.out.println("The mapped list: "+dataMappedToHeader);
		
	}
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return dataMappedToHeader;
		}		
}
	

