package com.standings.ui.page;

import java.io.Serializable;
import java.util.List;

public class League implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private List<Season> seasons;

    public League(String name, List<Season> seasons) {
        this.name = name;
        this.seasons = seasons;
    }

    // Getters y setters

    @Override
    public String toString() {
        return "League{" +
                "name='" + name + '\'' +
                ", seasons=" + seasons +
                '}';
    }
}