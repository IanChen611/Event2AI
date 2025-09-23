package adapter.dump;

public final class HtmlUtils {
    private HtmlUtils() {}

    public static String toPlainText(String html) {
        if (html == null) return "";
        // very naive strip; placeholder for better impl later
        return html.replaceAll("<[^>]+>", "").replace("&nbsp;", " ").trim();
    }
}
