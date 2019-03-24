package ee.moog.mugloar;

import ee.moog.mugloar.comm.ApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String [] args) {
        log.info("Mugloar client starting");
        System.out.println("Mugloar client");

        try {
            ApiService apiService = new ApiService();
            Runner runner = new Runner(apiService);

            if (args.length == 2) {
                try {
                    runManyGames(Integer.valueOf(args[0]), Integer.valueOf(args[1]), runner);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid argument format");
                    return;
                }
            } else {
                System.out.println("Running single game");
                runGame(runner, Integer.MAX_VALUE);
                System.out.println("Game finished, score: " + runner.getScore());
            }
        } catch (Exception e) {
            System.out.println("Exception occurred");
            log.error("Exception occurred", e);
        }
        log.info("Mugloar client finished");
    }

    private static void runManyGames(int gameCount, int scoreTarget, Runner runner) {
        int wins = 0;
        System.out.println("Running " + gameCount + " games until score " + scoreTarget);
        for(int i = 1; i <= gameCount; i++) {
            System.out.println("Running game number: " + i);
            runGame(runner, scoreTarget);
            if(runner.getScore() >= scoreTarget) {
                wins++;
            }
            System.out.println("Games run: " + i + " wins: " + wins + " losses: " + (i-wins));
        }
    }

    private static void runGame(Runner runner, int scoreTarget) {
        runner.start();
        while(!runner.gameFinished()) {
            runner.runNextStep();
            if(runner.getScore() > scoreTarget) {
                return;
            }
        }
    }
}
