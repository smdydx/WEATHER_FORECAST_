// first i am importing  classes and packages and handling IO operations, network connections, and the org.json package to  manipulate JSON data
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//Here, i define a main class which have  two constant fields: API_KEY and BASE_URL.  

public class WeatherApp {
    private static final String API_KEY = "b6907d289e10d714a6e88b30761fae22";
    private static final String BASE_URL = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=" + API_KEY;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // I create a Scanner object to read user input from the console.

        while (true) //It display a menu of options to the user. 
        {
            System.out.println("Choose an option:");
            System.out.println("1. Get weather");
            System.out.println("2. Get Wind Speed");
            System.out.println("3. Get Pressure");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); //Consume the newline character after reading the integer

            switch (choice) //switch case handles different cases based on the user's choice 
            {
                case 1:
                    System.out.print("Enter date (YYYY-MM-DD HH:mm:ss): ");
                    String date1 = scanner.nextLine();
                    getWeather(date1);
                    break;
                case 2:
                    System.out.print("Enter date (YYYY-MM-DD HH:mm:ss): ");
                    String date2 = scanner.nextLine();
                    getWindSpeed(date2);
                    break;
                case 3:
                    System.out.print("Enter date (YYYY-MM-DD HH:mm:ss): ");
                    String date3 = scanner.nextLine();
                    getPressure(date3);
                    break;
                case 0:
                    System.out.println("Exiting program...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
 //(getWeather, getWindSpeed, getPressure) to fetch and display the weather data accordingly.
    private static void getWeather(String date) {
        try {
             String url = BASE_URL;
        String response = makeAPICall(url);
        if (response != null) {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray forecastList = jsonObject.getJSONArray("list");
            for (int i = 0; i < forecastList.length(); i++) {
                JSONObject forecast = forecastList.getJSONObject(i);
                String dtTxt = forecast.getString("dt_txt");
                if (dtTxt.startsWith(date)) {
                    JSONObject main = forecast.getJSONObject("main");
                    double temp = main.getDouble("temp");
                    System.out.println("Temperature on " + dtTxt + ": " + temp + "°C");
                }
            }
        } else {
            System.out.println("Error occurred while fetching data. Please try again later.");
        }
            
        } catch (Exception e) {
            System.out.println("not executed");
            System.out.printf("%S%n",e.getMessage());
            
        }
    }
       

    private static void getWindSpeed(String date) {
    String url = BASE_URL;
    String response = makeAPICall(url);
    if (response != null) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray forecastList = jsonObject.getJSONArray("list");
            boolean found = false;
            for (int i = 0; i < forecastList.length(); i++) {
                JSONObject forecast = forecastList.getJSONObject(i);
                String dtTxt = forecast.getString("dt_txt");
                if (dtTxt.startsWith(date)) {
                    JSONObject wind = forecast.getJSONObject("wind");
                    double windSpeed = wind.getDouble("speed");
                    System.out.println("Wind Speed on " + dtTxt + ": " + windSpeed + " m/s");
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No data found for the input date: " + date);
            }
        } catch (JSONException e) {
            System.out.println("Error occurred while parsing JSON response.");
        }
    } else {
        System.out.println("Error occurred while fetching data. Please try again later.");
    }
}


private static void getPressure(String date) {
    String url = BASE_URL;
    String response = makeAPICall(url);
    if (response != null) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray forecastList = jsonObject.getJSONArray("list");
            boolean found = false;
            for (int i = 0; i < forecastList.length(); i++) {
                JSONObject forecast = forecastList.getJSONObject(i);
                String dtTxt = forecast.getString("dt_txt");
                if (dtTxt.startsWith(date)) {
                    JSONObject main = forecast.getJSONObject("main");
                    double pressure = main.getDouble("pressure");
                    System.out.println("Pressure on " + dtTxt + ": " + pressure + " hPa");
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No data found for the input date: " + date);
            }
        } catch (JSONException e) {
            System.out.println("Error occurred while parsing JSON response.");
        }
    } else {
        System.out.println("Error occurred while fetching data. Please try again later.");
    }
}

// GET request to the OpenWeatherMap API using the provided URL
    private static String makeAPICall(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
