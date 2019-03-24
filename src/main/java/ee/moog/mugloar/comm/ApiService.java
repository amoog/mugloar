package ee.moog.mugloar.comm;

import ee.moog.mugloar.model.*;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

public class ApiService {
    private final MugloarService mugloarService;

    public ApiService() {
        final Logger okhttpLogger = LoggerFactory.getLogger(OkHttpClient.class);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(okhttpLogger::debug);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.dragonsofmugloar.com")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();

        mugloarService = retrofit.create(MugloarService.class);
    }

    public GameStart startGame() {
        return runRequest(mugloarService.startGame(""));
    }

    public Reputation investigate(String gameId) {
        return runRequest(mugloarService.investigate(gameId));
    }

    public AdList getAds(String gameId) {
        return runRequest(mugloarService.getAds(gameId));
    }

    public SolveResult solveAd(String gameId, AdMessage adMessage) {
        return runRequest(mugloarService.solveAd(gameId, adMessage.adId));
    }

    public List<ShopItem> getShopItems(String gameId) {
        return runRequest(mugloarService.getShopItems(gameId));
    }

    public ShoppingResult buyItem(String gameId, String shopItemId) {
        return runRequest(mugloarService.buyItem(gameId, shopItemId));
    }

    private <T> T runRequest(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if(response.isSuccessful()) {
                return response.body();
            } else {
                if(response.errorBody() != null) {
                    throw new RuntimeException("Request failed: " + response.code() + ":" +
                            response.errorBody().string());
                } else {
                    throw new RuntimeException("Request failed: " + response.code());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Request failed", e);
        }
    }

}
