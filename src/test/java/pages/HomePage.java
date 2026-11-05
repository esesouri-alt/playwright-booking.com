package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.TimeoutError;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HomePage extends BasePage {

    public HomePage(Page page) {
        super(page);
    }

    public void open() {
        // page.navigate("https://www.booking.com/");
        page.navigate(
                "https://www.booking.com/",
                new Page.NavigateOptions()
                        .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
                        .setTimeout(180000)
        );

        System.out.println("Booking.com opened successfully.");//comment
        assertTrue(page.url().contains("booking.com"), "Booking.com website did not open successfully.");

    }


    // close popup
    public void closeInitialPopups() {

        System.out.println("Checking Genius popup...");

        Locator geniusCloseButton = page.locator(
                "button[aria-label='Dismiss sign-in info.']"
        );

        try {

            geniusCloseButton.waitFor(
                    new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.VISIBLE)
                            .setTimeout(60000)
            );

            geniusCloseButton.click();

            System.out.println("Genius popup closed successfully.");

        } catch (TimeoutError e) {

            System.out.println("Genius popup was not displayed.");

        }
        System.out.println("Checking Cookie popup...");

        Locator declineButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Decline")
        );

        try {
            declineButton.waitFor(
                    new Locator.WaitForOptions()
                            .setState(WaitForSelectorState.VISIBLE)
                            .setTimeout(60000)
            );

            declineButton.click();

            System.out.println("Cookie popup closed successfully.");

        } catch (TimeoutError e) {
            System.out.println("Cookie popup was not displayed.");
        }

    }


    // انتخاب ارز TRY

    public void selectCurrency() {

        Locator currencyButton = page.locator(
                "button[data-testid='header-currency-picker-trigger']"
        );

        currencyButton.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(60000)
        );

        currencyButton.click();

        Locator tryCurrency = page.getByText(
                "Turkish Lira",
                new Page.GetByTextOptions().setExact(false)
        );

        tryCurrency.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
        );

        tryCurrency.click(
                new Locator.ClickOptions().setNoWaitAfter(true)
        );

        // منتظر تکمیل تغییر آدرس می‌مانیم
        page.waitForURL(
                "**selected_currency=TRY**",
                new Page.WaitForURLOptions()
                        .setTimeout(60000)
        );

        // منتظر لود صفحه
        page.waitForLoadState(
                com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED
        );

        page.locator("#searchbox-horizontal-destination-input").waitFor();

        System.out.println("Currency selection completed.");
        System.out.println("Current URL: " + page.url());
        // انتظار برای لود کامل شبکه یا DOM بعد از تغییر ارز
        try {
            page.waitForLoadState(LoadState.NETWORKIDLE, new Page.WaitForLoadStateOptions().setTimeout(15000));
        } catch (Exception e) {
            // اگر شبکه کاملاً idle نشد، یک مکث کوتاه یا بررسی URL انجام می‌دهیم
            page.waitForTimeout(3000);
        }
    }

    // جستجوی مقصد London

    public void selectDestination(String destinationName) {
        // ۱. پیدا کردن اینپوت جستجوی مقصد
        Locator destinationInput = page.locator("#searchbox-horizontal-destination-input");

        // اطمینان از اینکه صفحه کاملاً بارگذاری شده و اینپوت قابل کلیک است
        destinationInput.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
        );

        // کلیک روی فیلد برای فوکوس
        destinationInput.click();

        // پاک کردن محتوای احتمالی قبلی
        destinationInput.fill("");

        // ۲. تایپ نام مقصد با تاخیر طبیعی برای اینکه سیستم جستجوی سایت (React) متوجه شود
        destinationInput.pressSequentially(
                destinationName,
                new Locator.PressSequentiallyOptions().setDelay(200)
        );

        System.out.println("Destination entered: " + destinationName);

        // ۳. پیدا کردن گزینه مربوط به شهر مورد نظر در منوی کشویی پیشنهادها
        Locator option = page.locator("[role='option']")
                .filter(new Locator.FilterOptions().setHasText(destinationName))
                .first();

        // ۴. انتظار برای ظاهر شدن لیست و کلیک روی آن
        option.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        // کمی تایم‌اوت را بیشتر می‌کنیم تا پیشنهادهای سایت کامل لود شوند
                        .setTimeout(6000)
        );

        option.click();

        System.out.println("Destination selected successfully.");
    }

    // انتخاب تاریخ انعطاف پذیر و ماه جاری

    public void selectFlexibleDates() {

        Locator flexibleOption = page.getByText(
                "I'm flexible",
                new Page.GetByTextOptions().setExact(true)
        );

        flexibleOption.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
        );

        flexibleOption.click();

        System.out.println("I'm flexible selected.");

          }

    public void selectOneMonth() {

        Locator monthOption = page.getByText(
                "A month",
                new Page.GetByTextOptions().setExact(true)
        );

        monthOption.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
        );

        monthOption.click();

        System.out.println("A month option selected.");

    }

    public void selectNovember2026() {

        Locator november2026 = page.locator("div.ab19514172")
                .filter(new Locator.FilterOptions()
                        .setHasText("Nov")
                )
                .filter(new Locator.FilterOptions()
                        .setHasText("2026")
                );


        november2026.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
        );

          System.out.println(
                "November 2026 matches: " + november2026.count()
        );//commend

        november2026.click();

        System.out.println("November 2026 selected.");

        page.waitForTimeout(1000);

        System.out.println("----- Calendar state after selecting November -----");
    }

    public void confirmFlexibleDates() {

        Locator selectDatesButton = page.getByText(
                "Select dates",
                new Page.GetByTextOptions().setExact(true)
        );

        System.out.println(
                "Select dates matches: " + selectDatesButton.count()
        );

        selectDatesButton.first().waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
        );

        selectDatesButton.first().click();

    }

    // تنظیم تعداد بزرگسالان روی 8

    public void setAdults() {

        // Step 1: Open occupancy settings
        Locator occupancyButton = page.locator(
                "[data-testid='occupancy-config']"
        );

        occupancyButton.click();

        System.out.println("Occupancy settings opened.");

        // Step 2: Locate occupancy popup
        Locator occupancyPopup = page.locator(
                "[data-testid='occupancy-popup']"
        );

        // Step 3: Locate Adults input
        Locator adultsInput = occupancyPopup.locator(
                "input#group_adults"
        );

        // Step 4: Locate Adults row
        Locator adultsRow = adultsInput.locator(
                "xpath=.."
        );

        // Step 5: Locate Adults increase button
        Locator adultsPlusButton = adultsRow.locator(
                "button:has(svg path[d^='M20.25 11.25'])"
        );

        // Step 6: Check locator
       /* if (adultsPlusButton.count() != 1) {
            throw new RuntimeException(
                    "Adults increase button was not found correctly."
            );
        }*/
        assertEquals(
                1,
                adultsPlusButton.count(),
                "Adults increase button was not found correctly."
        );

        // Step 7: Set target number of adults
        int targetAdults = 8;

        int currentAdults = Integer.parseInt(
                adultsInput.getAttribute("aria-valuenow")
        );

        System.out.println("Current adults: " + currentAdults);

        // Step 8: Increase adults until reaching target
        while (currentAdults < targetAdults) {

            adultsPlusButton.click();

            currentAdults = Integer.parseInt(
                    adultsInput.getAttribute("aria-valuenow")
            );

            System.out.println(
                    "Adults count: " + currentAdults
            );
        }

        // Step 9: Verify final value
       /* if (currentAdults == targetAdults) {

            System.out.println(
                    "Adults count successfully changed to 8."
            );

        } else {

            throw new RuntimeException(
                    "Failed to set adults count to 8."
            );
        }*/
        assertEquals(
                targetAdults,
                currentAdults,
                "Failed to set adults count to 8."
        );
    }

    // فعال کردن گزینه حیوانات خانگی

    public void enablePets() {

        // Step 1: Locate occupancy popup
        Locator occupancyPopup = page.locator(
                "[data-testid='occupancy-popup']"
        );

        // Step 2: Locate pets checkbox
        Locator petsCheckbox = occupancyPopup.locator(
                "input#pets"
        );

        // Step 3: Locate associated label
        Locator petsLabel = occupancyPopup.locator(
                "label[for='pets']"
        );

        // Step 4: Check current status
        boolean isPetsEnabled = Boolean.parseBoolean(
                petsCheckbox.getAttribute("aria-checked")
        );

        System.out.println(
                "Pets enabled before action: " + isPetsEnabled
        );

        // Step 5: Enable pets if disabled
        if (!isPetsEnabled) {

            petsLabel.click();

            System.out.println(
                    "Clicked on Traveling with pets label."
            );

        } else {

            System.out.println(
                    "Traveling with pets is already enabled."
            );
        }

        // Step 6: Verify final status
        boolean finalStatus = Boolean.parseBoolean(
                petsCheckbox.getAttribute("aria-checked")
        );

        /*if (finalStatus) {
            System.out.println(
                    "Pets option successfully verified."
            );
        } else {
            throw new RuntimeException(
                    "Failed to enable Traveling with pets."
            );*/
              assertTrue(
                finalStatus,
                "Failed to enable Traveling with pets."
        );
        }


    public void clickDone() {

        // Step 1: Locate occupancy popup
        Locator occupancyPopup = page.locator(
                "[data-testid='occupancy-popup']"
        );

        // Step 2: Locate Done button
        Locator doneButton = occupancyPopup.getByRole(
                AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Done")
        );

        // Step 3: Verify button exists
        if (doneButton.count() != 1) {
            throw new RuntimeException(
                    "Done button was not found correctly."
            );
        }

        // Step 4: Click Done
        doneButton.click();

        System.out.println(
                "Done button clicked successfully."
        );

        // Step 5: Verify popup is closed
        occupancyPopup.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.HIDDEN)
        );

        System.out.println(
                "Occupancy popup closed successfully."
        );
        page.waitForTimeout(2000);
    }

    // اجرای جستجو

    public void clickSearch() {

        Locator searchButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Search")
        );

        if (searchButton.count() != 1) {
            throw new RuntimeException(
                    "Search button was not found correctly."
            );
        }

        System.out.println("Current URL before search: " + page.url());

        searchButton.click();

        System.out.println("Search button clicked successfully.");

        // منتظر تغییر URL نمی‌مانیم؛ ابتدا وضعیت واقعی صفحه را بررسی می‌کنیم.
        page.waitForTimeout(30000);

        System.out.println("Current URL after search: " + page.url());

    }
}

