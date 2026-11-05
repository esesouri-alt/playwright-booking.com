package pages;

import com.microsoft.playwright.Page;
public class BasePage {

    protected Page page;

        public BasePage(Page page) {
            this.page = page;
        }

        public void waitForPageLoad() {
            page.waitForLoadState();
        }
        //public void waitForPageLoad() {page.waitForLoadState(LoadState.DOMCONTENTLOADED);}
      //منتظر می‌مانیم ساختار اولیه HTML صفحه بارگذاری شود.
}
