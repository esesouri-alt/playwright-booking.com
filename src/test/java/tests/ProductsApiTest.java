package tests;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.microsoft.playwright.options.RequestOptions;
import java.util.Map;
import com.microsoft.playwright.options.FormData;

public class ProductsApiTest {

    @Test
    public void getProductsByPoloBrand() throws Exception {

        // 1. Create Playwright
        Playwright playwright = Playwright.create();

        // 2. Create API request context
        APIRequestContext request =
                playwright.request().newContext();

        // 3. Send GET request
        APIResponse response = request.get(
                "https://automationexercise.com/api/productsList"
        );

        // 4. Check response status
        System.out.println("Status: " + response.status());

        assertEquals(200, response.status());

        // 5. Parse JSON response
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonResponse =
                mapper.readTree(response.text());

        // 6. Get products array
        JsonNode products = jsonResponse.get("products");

        // 7. Filter products with brand = Polo
        System.out.println("Products with brand Polo:");

       /* for (JsonNode product : products) {

            String brand = product.get("brand").asText();

            if (brand.equalsIgnoreCase("Polo")) {

                String name = product.get("name").asText();

                System.out.println(name);
            }*/
        for (int i = 0; i < products.size(); i++) {

            // گرفتن عنصر در ایندکس i با استفاده از متد get()
            JsonNode product = products.get(i);

            String brand = product.get("brand").asText();

            if (brand.equalsIgnoreCase("Polo")) {
                String name = product.get("name").asText();
                System.out.println(name);
            }
        }

        // 8. Close resources
        request.dispose();
        playwright.close();
    }


    @Test
    public void searchProductsByCategory() throws Exception {

        // 1. Create Playwright
        Playwright playwright = Playwright.create();

        // 2. Create API request context
        APIRequestContext request =
                playwright.request().newContext();

        // 3. Send POST request with parameter
        APIResponse response = request.post(
                "https://automationexercise.com/api/searchProduct",
                RequestOptions.create()
                        .setForm(
                                FormData.create()
                                        .set("search_product", "tshirt")
                        )
        );

        // 4. Check response status
        System.out.println("Status: " + response.status());

        assertEquals(200, response.status());

        // 5. Parse JSON response
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonResponse =
                mapper.readTree(response.text());

        // 6. Get products array
        JsonNode products = jsonResponse.get("products");

        // 7. Filter products by category
        System.out.println("Products in T-shirts category:");

       /* for (JsonNode product : products) {

            String category = product
                    .get("category")
                    .get("category")
                    .asText();

            if (category.equalsIgnoreCase("Tshirts")) {

                String name = product.get("name").asText();

                System.out.println(name);
            }
        }*/

        for (int i = 0; i < products.size(); i++) {

            // گرفتن هر محصول با استفاده از ایندکس
            JsonNode product = products.get(i);

            String category = product
                    .get("category")
                    .get("category")
                    .asText();

            if (category.equalsIgnoreCase("Tshirts")) {

                String name = product.get("name").asText();

                System.out.println(name);
            }
        }

        // 8. Close resources
        request.dispose();
        playwright.close();
    }

}
