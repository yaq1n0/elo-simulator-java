package com.company;

public class Player implements Comparable<Player> {
    private final String name;
    private final int hidden;
    private int elo;

    public Player(String name, int hidden, int elo) {
        this.name = name;
        this.hidden = hidden;
        this.elo = elo;
    }

    public String getName() {
        return name;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public int getHidden() {
        return hidden;
    }

    private double getExpectation(double p1_rating, double p2_rating) {
        if (p1_rating == p2_rating) {
            return 0.5;
        } else {
            return (1.0 / (1.0 + Math.pow(10.0, ((p2_rating - p1_rating) / 400.0))));
        }
    }

    public void compete(Player enemy, double K_Factor) {
        double win_chance = getExpectation(this.getElo(), enemy.getElo());

        int p1_score = (int) (this.getHidden() * (1 + FakeStats.getSimulatedBiasedRandomness(true)));
        int p2_score = (int) (enemy.getHidden() * (1 + FakeStats.getSimulatedBiasedRandomness(true)));

        if (this.getElo() > enemy.getElo()) {
            // player 1 is higher elo
            if (p1_score <= p2_score) {
                // player 1 didn't win (tie or loss)
                this.setElo((int) (this.getElo() * (1 - (K_Factor * win_chance))));
                enemy.setElo((int) (enemy.getElo() * (1 + (K_Factor * win_chance))));
            } else {
                this.setElo((int) (this.getElo() * (1 + (K_Factor * (1 - win_chance)))));
                enemy.setElo((int) (enemy.getElo() * (1 - (K_Factor * (1 - win_chance)))));
            }
        } else if (this.getElo() < enemy.getElo()) {
            // player 2 is higher elo
            if (p2_score <= p1_score) {
                // player 2 didn't win (tie or loss)
                this.setElo((int) (this.getElo() * (1 + (K_Factor * (1 - win_chance)))));
                enemy.setElo((int) (enemy.getElo() * (1 - (K_Factor * (1 - win_chance)))));
            } else {
                this.setElo((int) (this.getElo() * (1 - (K_Factor * win_chance))));
                enemy.setElo((int) (enemy.getElo() * (1 + (K_Factor * win_chance))));
            }
        } else {
            // players have equal elo
            if (p1_score != p2_score) {
                if (p1_score > p2_score) {
                    // player 1 won
                    this.setElo((int) (this.getElo() * (1 + (K_Factor * 0.5))));
                    enemy.setElo((int) (enemy.getElo() * (1 - (K_Factor * 0.5))));
                } else {
                    // player 2 won
                    this.setElo((int) (this.getElo() * (1 - (K_Factor * 0.5))));
                    enemy.setElo((int) (enemy.getElo() * (1 + (K_Factor * 0.5))));
                }
            } // equal elo && tie -> do nothing
        }
    }

    @Override
    public int compareTo(Player player) {
        return Integer.compare(this.getElo(), player.getElo());
    }
}
