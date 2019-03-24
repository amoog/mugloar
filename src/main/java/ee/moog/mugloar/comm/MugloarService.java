package ee.moog.mugloar.comm;

import ee.moog.mugloar.model.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface MugloarService {
    @POST("/api/v2/game/start")
    Call<GameStart> startGame(@Body String empty);

    @POST("/api/v2/{gameId}/investigate/reputation")
    Call<Reputation> investigate(@Path("gameId") String gameId);

    @GET("/api/v2/{gameId}/messages")
    Call<AdList> getAds(@Path("gameId") String gameId);

    @POST("/api/v2/{gameId}/solve/{adId}")
    Call<SolveResult> solveAd(@Path("gameId") String gameId, @Path("adId") String adId);

    @GET("/api/v2/{gameId}/shop")
    Call<List<ShopItem>> getShopItems(@Path("gameId") String gameId);

    @POST("/api/v2/{gameId}/shop/buy/{itemId}")
    Call<ShoppingResult> buyItem(@Path("gameId") String gameId, @Path("itemId") String itemId);
}
