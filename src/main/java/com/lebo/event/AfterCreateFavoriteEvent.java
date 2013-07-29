package com.lebo.event;

import com.lebo.entity.Favorite;

/**
 * @author: Wei Liu
 * Date: 13-7-29
 * Time: AM11:09
 */
public class AfterCreateFavoriteEvent {
    private Favorite favorite;

    public AfterCreateFavoriteEvent(Favorite favorite) {
        this.favorite = favorite;
    }

    public Favorite getFavorite() {
        return favorite;
    }
}
