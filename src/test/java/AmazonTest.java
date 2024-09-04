import com.codeborne.selenide.Configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;


public class AmazonTest {

    @BeforeEach
    void setUp() {
        open("https://www.amazon.com/");
        Configuration.browserSize = "1920x1080";
    }

    @ValueSource(strings = {"Jeans", "Skirt"})
    @ParameterizedTest(name = "Для поискового запроса {0} должен быть не пустой список карточек")
    void searchResultShouldNotBeEmptyTest(String searchQuery) {
        $("#twotabsearchtextbox").setValue(searchQuery).pressEnter();
        $$("[data-component-type=s-search-result]").shouldBe(sizeGreaterThan(0));
    }

    // можно использовать или @CsvSource или @CsvFileSource, использовала два для практики
    @CsvSource(value = {
            "Maxlite Air Carry-on Hardside Expandable Spinner,$109.90 ",
            "Platinum Elite Business Plus Carry-On Expandable Hardside Spinner, $256.40"})
    @CsvFileSource(resources = "/test_data/productCardShouldBeWithPrice.csv")
    @ParameterizedTest(name = "Для поискового запроса {0} должны быть указаны цена {1} на данный продукт")
    void productCardShouldBeWithPriceTest(String searchQuery, String expectedPrice) {
        $("#twotabsearchtextbox").setValue(searchQuery).pressEnter();
        $("[data-component-type=s-search-result]").click();
        $(".a-text-price").shouldHave(text(expectedPrice));
    }

    @DisplayName("Проверка смены языка на сайте")
    @EnumSource(Language.class)
    @ParameterizedTest
    void amazonSiteShouldBeChangeLanguageTest(Language language) {
        $(".icp-nav-link-inner").click();
        $(byText(language.name())).click();
        $("[aria-labelledby=icp-save-button-announce]").click();
        $(".hm-icon-label").shouldHave(text(language.description));

    }

    static Stream<Arguments> amazonSiteShouldBeDisplayCorrectButtonsTest() {
        return Stream.of(
                Arguments.of(
                        Language.DE,
                        List.of("Angebote des Tages", "Kundenservice", "Wunschlisten", "Geschenkkarten ", "Verkaufen bei Amazon")),
                Arguments.of(
                        Language.EN,
                        List.of("Today's Deals", "Customer Service", "Registry", "Gift Cards", "Sell")
                )
        );
    }

    @DisplayName("Проверка смены языка на сайте по меню")
    @MethodSource
    @ParameterizedTest
    void amazonSiteShouldBeDisplayCorrectButtonsTest(Language language, List<String> expectedButtons) {
        $(".icp-nav-link-inner").click();
        $(byText(language.name())).click();
        $("[aria-labelledby=icp-save-button-announce]").click();
        $$("#nav-xshop a").filter(visible).shouldHave(texts(expectedButtons));

    }
}
