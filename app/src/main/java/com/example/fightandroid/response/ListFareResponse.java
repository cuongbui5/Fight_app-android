package com.example.fightandroid.response;


import com.example.fightandroid.model.Fare;

import java.util.List;

public class ListFareResponse {
    private List<Fare> fares;
    private int page;
    private int totalPage;

    public List<Fare> getFares() {
        return fares;
    }

    public void setFares(List<Fare> fares) {
        this.fares = fares;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
