package com.mehrani;

public class Rating {
    private String username;
    private int commodityId;
    private double score;

    public void setData(String username, int commodityId, double score) {
        this.username = username;
        this.commodityId = commodityId;
        this.score = score;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setCommodityId(int commodityId) {
        this.commodityId = commodityId;
    }
    public void setScore(double score) {
        this.score = score;
    }
    public String getUsername() {
        return username;
    }
    public double getScore() {
        return score;
    }
    public int getCommodityId() {
        return commodityId;
    }
}
