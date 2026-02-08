package helper;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RouteHelper {

    public interface RouteCallback {
        void onRouteReady(Polyline polyline);
        void onFailure(Exception e);
    }

    /**
     * Dobavlja rutu sa OSRM i vraća Polyline kroz callback
     */
    public static void fetchRoutePolyline(List<GeoPoint> points, RouteCallback callback) {
        new Thread(() -> {
            try {
                List<GeoPoint> routePoints = getRoute(points);

                if (routePoints == null || routePoints.isEmpty()) {
                    postFailure(callback, new Exception("No route points returned"));
                    return;
                }

                Polyline polyline = new Polyline();
                polyline.setPoints(routePoints);
                polyline.setColor(Color.BLUE);
                polyline.setWidth(8f);
                polyline.setGeodesic(true);

                // vrati callback na UI thread
                new Handler(Looper.getMainLooper()).post(() -> callback.onRouteReady(polyline));

            } catch (IOException | JSONException e) {
                postFailure(callback, e);
            }
        }).start();
    }

    private static void postFailure(RouteCallback callback, Exception e) {
        new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
    }

    private static List<GeoPoint> getRoute(List<GeoPoint> points)
            throws IOException, JSONException {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < points.size(); i++) {
            GeoPoint point = points.get(i);
            builder.append(point.getLongitude())
                    .append(",")
                    .append(point.getLatitude());
            if (i < points.size() - 1) {
                builder.append(";");
            }
        }
        String pointsStr = builder.toString();

        String url = "https://router.project-osrm.org/route/v1/driving/" +
                pointsStr +
                "?overview=simplified&geometries=geojson";

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "OSMDroidExample/1.0, osmdroid@gmail.com")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                return null;
            }

            JSONObject json = new JSONObject(response.body().string());
            JSONArray routes = json.optJSONArray("routes");
            if (routes == null || routes.length() == 0) {
                return null;
            }

            JSONArray coordinates = routes.getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONArray("coordinates");

            List<GeoPoint> routePoints = new ArrayList<>(coordinates.length());

            for (int i = 0; i < coordinates.length(); i++) {
                JSONArray point = coordinates.getJSONArray(i);
                double lon = point.getDouble(0);
                double lat = point.getDouble(1);
                routePoints.add(new GeoPoint(lat, lon));
            }

            return routePoints;
        }
    }
}
