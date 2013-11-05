package com.lebo.rest;

import com.lebo.entity.Goods;
import com.lebo.entity.User;
import com.lebo.entity.UserGoods;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.rest.dto.UserGoodsDto;
import com.lebo.rest.dto.UserVgDto;
import com.lebo.service.VgService;
import com.lebo.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.BeanMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Wei Liu
 * Date: 13-10-29
 * Time: PM6:16
 */
@Controller
public class VgRestController {
    @Autowired
    private VgService vgService;
    @Autowired
    private AccountService accountService;

    private static final String API_1_1_VG = "/api/1.1/vg/";

    @RequestMapping(value = API_1_1_VG + "goldProducts.json", method = RequestMethod.GET)
    @ResponseBody
    public Object goldProducts() {
        return vgService.toProductDtos(vgService.findAllGoldProducts());
    }

    @RequestMapping(value = API_1_1_VG + "alipaySigedParams.json", method = RequestMethod.GET)
    @ResponseBody
    public Object alipaySigedParams(@RequestParam("productId") Long productId,
                                    @RequestParam("service") String service,
                                    @RequestParam("paymentType") String paymentType) {

        String signed = vgService.getAlipayParams(accountService.getCurrentUserId(), productId, service, paymentType);

        Map<String, String> result = new HashMap<String, String>(1);
        result.put("signed", signed);
        return result;
    }

    @RequestMapping(value = API_1_1_VG + "goldOrders/detail.json", method = RequestMethod.GET)
    @ResponseBody
    public Object goldOrdersDetail(@RequestParam("orderId") Long orderId) {

        return vgService.toGoldOrderDto(vgService.getOrderWithDetail(orderId));
    }

    @RequestMapping(value = API_1_1_VG + "userVg.json", method = RequestMethod.GET)
    @ResponseBody
    public Object userVg(@RequestParam(value = "userId", required = false) String userId,
                         @RequestParam(value = "screenName", required = false) String screenName) {

        userId = accountService.getUserId(userId, screenName);

        User user = accountService.getUser(userId);
        if (user == null) {
            ErrorDto.badRequest("userId[" + userId + "]不存在");
        }

        //用户名字、头像
        UserVgDto userVgDto = new UserVgDto();
        BeanMapper.copy(user, userVgDto);
        userVgDto.setUserId(user.getId());
        //用户金币数
        userVgDto.setGoldQuantity(vgService.getUserGoldQuantity(userId));
        //用户物品
        Long goodsTotalPrice = 0L;
        List<UserGoods> userGoodsList = vgService.getUserGoodsByUserId(userId);
        List<UserGoodsDto> userGoodsDtos = new ArrayList<UserGoodsDto>(userGoodsList.size());
        for (UserGoods userGoods : userGoodsList) {
            UserGoodsDto userGoodsDto = new UserGoodsDto();
            Goods goods = vgService.getGoodsById(userGoods.getGoodsId());
            BeanMapper.copy(goods, userGoodsDto);

            userGoodsDto.setQuantity(userGoods.getQuantity());

            userGoodsDtos.add(userGoodsDto);
            goodsTotalPrice += goods.getPrice() * userGoods.getQuantity();
        }
        userVgDto.setGoods(userGoodsDtos);
        userVgDto.setGoodsTotalPrice(goodsTotalPrice);

        return userVgDto;
    }

    @RequestMapping(value = API_1_1_VG + "goods.json", method = RequestMethod.GET)
    @ResponseBody
    public Object getAllGoods() {
        return vgService.toGoodsDtos(vgService.getAllGoods());
    }
}
