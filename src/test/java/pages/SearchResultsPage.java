package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.LoadState;

public class SearchResultsPage extends BasePage {

    public SearchResultsPage(Page page) {
        super(page);
    }

    // تعداد هتل‌های نمایش داده شده
    public int getHotelCount() {

        Locator hotelCards = page.locator(
                "[data-testid='property-card']"
        );

        hotelCards.first().waitFor();

        return hotelCards.count();
    }

    // دریافت نام هتل سوم
    public String getThirdHotelName() {

        Locator hotelCards = page.locator(
                "[data-testid='property-card']"
        );

        Locator thirdHotel = hotelCards.nth(2);

        Locator hotelName = thirdHotel.locator(
                "[data-testid='title']"
        );

        return hotelName.innerText();
    }

    // باز کردن صفحه هتل سوم
    public Page clickThirdHotelAvailability() {

        Locator hotelCards = page.locator(
                "[data-testid='property-card']"
        );

        Locator thirdHotel = hotelCards.nth(2);

        Locator availabilityButton = thirdHotel.locator(
                "[data-testid='availability-cta-btn']"
        );

        availabilityButton.waitFor();

        Page hotelPage = page.waitForPopup(() -> {
            availabilityButton.click();
        });

        hotelPage.waitForLoadState(
                LoadState.DOMCONTENTLOADED
        );

        return hotelPage;
    }
}