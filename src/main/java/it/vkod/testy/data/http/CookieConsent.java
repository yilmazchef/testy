package it.vkod.testy.data.http;

public enum CookieConsent {

    Necessary("Necessary cookies help make a website usable by enabling basic functions like page navigation and access to secure areas of the website. The website cannot function properly without these cookies."),
    Preferences("Preference cookies enable a website to remember information that changes the way the website behaves or looks, like your preferred language or the region that you are in."),
    Statistics("Statistic cookies help website owners to understand how visitors interact with websites by collecting and reporting information anonymously."),
    Marketing("Marketing cookies are used to track visitors across websites. The intention is to display ads that are relevant and engaging for the individual user and thereby more valuable for publishers and third party advertisers."),
    Unclassified("Unclassified cookies are cookies that we are in the process of classifying, together with the providers of individual cookies.");

    private final String description;

    CookieConsent(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getHeader() {
        return "This website uses cookies.";
    }

    public String getBody() {
        return " We use cookies to personalize content and ads, to provide social media features and to analyse our traffic." +
                " We also share information about your use of our site with" +
                " our social media, advertising and analytics partners who may combine it with other information" +
                " that you’ve provided to them or that they’ve collected from your use of their services";
    }

    public String getLearnMoreLink() {
        return "https://vaadin.com/terms-of-service";
    }
}
