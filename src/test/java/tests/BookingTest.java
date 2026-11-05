package tests;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import pages.HomePage;
import pages.SearchResultsPage;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    Playwright playwright;
    Browser browser;
    Page page;

    HomePage homePage;
    SearchResultsPage searchResultsPage;

    @BeforeEach
    void setUp() {

        playwright = Playwright.create();

        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setChannel("chrome")
                        .setHeadless(false)
                        .setArgs(java.util.Arrays.asList(
                                "--disable-quic",
                                "--start-maximized"
                        ))
        );

        page = browser.newPage(
                new Browser.NewPageOptions()
                        .setViewportSize(null)
        );

        homePage = new HomePage(page);

        searchResultsPage = new SearchResultsPage(page);
    }

    @Test
    void openBookingWebsite() {

        // Step 1: Open website
        homePage.open();

        assertTrue(
                page.url().contains("booking.com"),
                "Booking.com website did not open."
        );

        // Step 2: Close initial popups
        homePage.closeInitialPopups();

        // Step 3: Select currency
        homePage.selectCurrency();

        // Step 4: Select destination
        homePage.selectDestination("London");

        // Step 5: Select flexible dates
        homePage.selectFlexibleDates();

        homePage.selectOneMonth();

        homePage.selectNovember2026();

        homePage.confirmFlexibleDates();

        // Step 6: Set adults
        homePage.setAdults();

        // Step 7: Enable pets
        homePage.enablePets();

        // Step 8: Confirm occupancy
        homePage.clickDone();

        Locator occupancyButton = page.locator(
                "[data-testid='occupancy-config']"
        );

        String occupancyText = occupancyButton.innerText();

        assertTrue(
                occupancyText.contains("8 adults"),
                "Adults count should be 8."
        );

        assertTrue(
                occupancyText.contains("Pets"),
                "Traveling with pets should be enabled."
        );

        // Step 9: Search
        homePage.clickSearch();

        // Step 10: Verify search results
        assertTrue(
                page.url().contains("searchresults.html"),
                "Search results page did not open."
        );

        int hotelCount = searchResultsPage.getHotelCount();

        assertTrue(
                hotelCount >= 3,
                "At least 3 hotels should be displayed."
        );

        // Step 11: Get third hotel name
        String selectedHotelName =
                searchResultsPage.getThirdHotelName();

        assertFalse(
                selectedHotelName.isBlank(),
                "Third hotel name should not be empty."
        );

        // Step 12: Open third hotel
        Page hotelPage =
                searchResultsPage.clickThirdHotelAvailability();

        // Step 13: Verify hotel page
        assertNotNull(
                hotelPage,
                "Hotel page should not be null."
        );

        assertTrue(
                hotelPage.url().contains("booking.com"),
                "Unexpected hotel page URL."
        );

        String hotelTitle = hotelPage.title();

        assertFalse(
                hotelTitle.isBlank(),
                "Hotel page title should not be empty."
        );

        assertTrue(
                hotelTitle.contains(selectedHotelName),
                "Hotel page title does not match selected hotel."
        );
    }

    @AfterEach
    void tearDown() {

        if (browser != null) {
            browser.close();
        }

        if (playwright != null) {
            playwright.close();
        }
    }
}