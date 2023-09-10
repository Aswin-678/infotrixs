package renjith_jr;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONObject;

public class CurrencyConverter {

    private static Map<String, BigDecimal> favoriteCurrencies = new HashMap<>();

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Select an option:");
            System.out.println("1. Convert Currency");
            System.out.println("2. Add Favorite Currency");
            System.out.println("3. View Favorite Currencies");
            System.out.println("4. Edit Favorite Currency");
            System.out.println("5. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline

            switch (choice) {
                case 1:
                    convertCurrency(scanner);
                    break;
                case 2:
                    addFavoriteCurrency(scanner);
                    break;
                case 3:
                    viewFavoriteCurrencies(scanner);
                    break;
                case 4:
                    editFavoriteCurrency(scanner);
                    break;
                case 5:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void convertCurrency(Scanner scanner) throws IOException {
        System.out.println("Type currency to convert from");
        String convertFrom = scanner.nextLine().toUpperCase();
        System.out.println("Type currency to convert to");
        String convertTo = scanner.nextLine().toUpperCase();
        System.out.println("Type quantity to convert");
        BigDecimal quantity = scanner.nextBigDecimal();

        String urlString = "https://api.exchangerate.host/latest?base=" + convertFrom;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlString)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String stringResponse = response.body().string();
        JSONObject jsonObject = new JSONObject(stringResponse);
        JSONObject ratesObject = jsonObject.getJSONObject("rates");
        BigDecimal rate = ratesObject.getBigDecimal(convertTo);

        BigDecimal result = rate.multiply(quantity);
        System.out.println("Converted amount: " + result);
    }

    public static void addFavoriteCurrency(Scanner scanner) {
        System.out.println("Enter a currency code to add to favorites");
        String currencyCode = scanner.nextLine().toUpperCase();

        favoriteCurrencies.put(currencyCode, BigDecimal.ZERO); // Default exchange rate to zero
        System.out.println(currencyCode + " has been added to your favorites.");
    }

    public static void viewFavoriteCurrencies(Scanner scanner) {
        System.out.println("Favorite Currencies:");
        for (String currencyCode : favoriteCurrencies.keySet()) {
            System.out.println(currencyCode);
        }
    }

    public static void editFavoriteCurrency(Scanner scanner) {
        System.out.println("Enter a currency code to edit");
        String currencyCode = scanner.nextLine().toUpperCase();

        if (favoriteCurrencies.containsKey(currencyCode)) {
            System.out.println("Enter the new exchange rate for " + currencyCode);
            BigDecimal exchangeRate = scanner.nextBigDecimal();
            favoriteCurrencies.put(currencyCode, exchangeRate);
            System.out.println(currencyCode + " exchange rate has been updated.");
        } else {
            System.out.println(currencyCode + " is not in your favorites. Add it first.");
        }
    }
}
