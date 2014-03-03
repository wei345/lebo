package com.lebo.event;

import com.lebo.entity.GiveGoods;

/**
 * @author: Wei Liu
 * Date: 13-12-3
 * Time: PM3:53
 */
public class GiveGoodsNotificationEvent {
    private GiveGoods giveGoods;

    public GiveGoodsNotificationEvent(GiveGoods giveGoods) {
        this.giveGoods = giveGoods;
    }

    public GiveGoods getGiveGoods() {
        return giveGoods;
    }
}
