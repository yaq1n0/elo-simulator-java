package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Sim {
    public Sim(int player_count, int iter_count, int maxElo, double K_Factor, double normalizeThreshold, double normalizeFactor, boolean verbose) {
        List<Player> playerList = generateTestPopulation(player_count, maxElo);

        System.out.println("Starting conditions!!");
        printPlayerStatus(playerList);

        for (int i = 0; i < iter_count; i++) {
            Collections.sort(playerList);

            if (playerList.get(playerList.size() - 1).getElo() > maxElo * normalizeThreshold)
                normalize(playerList, maxElo, normalizeFactor, verbose);

            playRound(playerList, K_Factor);

            if (verbose) {
                printPlayerStatus(playerList);
            }
        }

        if (!verbose) {
            System.out.println("Ending conditions!!");
            printPlayerStatus(playerList);
        }
    }

    public static void main(String[] args) {
        new Sim(10, 10, 1000, 0.5, 1.1, 0.5, false);
    }

    private List<Player> generateTestPopulation(int size, int maxElo) {
        List<Player> returnList = new ArrayList<>();

        for (int i = 0; i <= size; i++) returnList.add(new Player("player" + i, (maxElo / size) * i, maxElo / 2));

        Collections.shuffle(returnList);

        return returnList;
    }

    private void playRound(List<Player> playerList, double K_Factor) {
        // playerList will always be sorted by ascending Elo
        int player1_index = new Random().nextInt(playerList.size());
        int player2_index;

        boolean valid = false;

        do {
            player2_index = (int) (player1_index + (10 * FakeStats.getSimulatedBiasedRandomness(true)));
            if ((0 < player2_index && player2_index < playerList.size() && player1_index != player2_index))
                valid = true;
        } while (!valid);

        /*
        // test if the matchmaking system is working, player1_index and player2_index should be similar
        System.out.println("\nplayer1 index" + player1_index);
        System.out.println("player2 index" + player2_index + "\n");
        */

        playerList.get(player1_index).compete(playerList.get(player2_index), K_Factor);
    }

    private void normalize(List<Player> playerList, int MaxElo, double normalizeFactor, boolean verbose) {
        // steepness reduction, shift towards median
        // normalizeFactor inversely proportional to amount of change
        // playerList will always be sorted by ascending Elo

        if (verbose) {
            System.out.println("\nNORMALIZE START!!");
            printPlayerStatus(playerList);
        }

        for (Player player : playerList) {
            if (player.getElo() > MaxElo / 2) {
                player.setElo((MaxElo / 2) + (int) (normalizeFactor * (player.getElo() - (MaxElo / 2))));
            } else {
                player.setElo((MaxElo / 2) - (int) (normalizeFactor * ((MaxElo / 2) - player.getElo())));
            }
        }

        if (verbose) {
            printPlayerStatus(playerList);
            System.out.println("NORMALIZE DONE!!\n");
        }
    }

    private void printPlayerStatus(List<Player> playerList) {
        System.out.println("================\nname elo hidden");
        for (Player player : playerList) {
            System.out.printf("%s %d %d\n", player.getName(), player.getElo(), player.getHidden());
        }
        System.out.println("================");
    }
}

