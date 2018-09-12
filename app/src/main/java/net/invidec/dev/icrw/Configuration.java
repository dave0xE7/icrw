package net.invidec.dev.icrw;

public class Configuration {
    private static final String app_host = "195.201.178.220";
    private static final String domain_path = "http://" + app_host + "/";
    private static final String app_auth = domain_path + "icrw-server/api.php";
    private static final String app_pricecharts = domain_path + "icrw-server/pricecharts.json";

    public static String apiUrl_EURBTC = "https://beatcoin.pl/public/graphs/EURBTC/1day.json";
    public static String apiUrl_BTCICR = "https://beatcoin.pl/public/graphs/BTCICR/1day.json";

    public static String getApp_host() {
        return app_host;
    }

    public static String getDomain_path() {
        return domain_path;
    }

    public static String getApp_auth() {
        return app_auth;
    }

    public static String getApp_pricecharts() {return app_pricecharts; }
}
