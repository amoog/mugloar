package ee.moog.mugloar;

import ee.moog.mugloar.comm.ApiService;
import ee.moog.mugloar.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

class Runner {
    private static final String HEALTHPOT = "hpot";

    private static final Logger log = LoggerFactory.getLogger(Runner.class);

    private final ApiService api;
    private final Random random;
    private final Set<String> inventory;

    private GameStart game;

    Runner(ApiService apiService) {
        api = apiService;
        random = new Random();
        inventory = new HashSet<>();
    }

    int getScore() {
        return game.score;
    }

    void start() {
        inventory.clear();
        game = api.startGame();
    }

    boolean gameFinished() {
        return game.lives <= 0;
    }

    void runNextStep() {
        visitShop();
        AdList ads = api.getAds(game.gameId);

        ads.collectProbabilities();

        AdMessage adToSolve = selectAd(ads);
        if(adToSolve != null) {
            log.info("Solving: " + adToSolve.describe());
            SolveResult solveResult = api.solveAd(game.gameId, adToSolve);
            updateGame(solveResult);
            if(solveResult.success) {
                System.out.println(game.turn + ": Solved " + adToSolve.adId + ". " + game.describe());
            } else {
                System.out.println(game.turn + ": Failed to solve " + adToSolve.adId + ". " + game.describe());
            }
            log.info("Solve result: " + solveResult.describe());
        } else {
            System.out.println("Out of ads");
            throw new RuntimeException("Out of ads");
        }
    }

    private AdMessage selectAd(AdList ads) {
        if(ads.allEncrypted()) {
            return null;
        }
        List<AdMessage> candidates = new ArrayList<>();
        for(AdMessage ad : ads) {
            if(!ad.encrypted) {
                candidates.add(ad);
            }
        }
        if(candidates.size() == 0) {
            return null;
        }
        int randomAd = random.nextInt(candidates.size());
        return candidates.get(randomAd);
    }

    private void visitShop() {
        List<ShopItem> shopItems = api.getShopItems(game.gameId);
        if(game.lives == 1) {
            improveHealth(shopItems);
        }
        for(ShopItem shopItem : shopItems) {
            if(!HEALTHPOT.equals(shopItem.id) && game.gold > shopItem.cost + 100 && !inventory.contains(shopItem.id)) {
                buyItem(shopItem.id);
                return;
            }
        }
    }

    Reputation investigate() {
        Reputation reputation = api.investigate(game.gameId);
        log.info(reputation.describe());
        return  reputation;
    }

    private void improveHealth(List<ShopItem> shopItems) {
        if(hasItem(HEALTHPOT, shopItems)) {
            buyItem(HEALTHPOT);
        }
    }

    private void buyItem(String itemId) {
        ShoppingResult shoppingResult = api.buyItem(game.gameId, itemId);
        if(shoppingResult.shoppingSuccess) {
            inventory.add(itemId);
            updateGame(shoppingResult);
            System.out.println(game.turn + ": Bought item " + itemId + ". " + game.describe());
        } else {
            System.out.println(game.turn + ": Tried to buy " + itemId + ". " + game.describe());
        }
    }

    private void updateGame(SolveResult solveResult) {
        game.lives = solveResult.lives;
        game.gold = solveResult.gold;
        game.score = solveResult.score;
        game.turn = solveResult.turn;
    }

    private void updateGame(ShoppingResult shoppingResult) {
        game.lives = shoppingResult.lives;
        game.gold = shoppingResult.gold;
        game.level = shoppingResult.level;
        game.turn = shoppingResult.turn;
    }

    private boolean hasItem(String itemId, List<ShopItem> shopItems) {
        for(ShopItem item : shopItems) {
            if(itemId.equals(item.id)) {
                return true;
            }
        }
        return false;
    }
}
