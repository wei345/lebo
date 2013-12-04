package com.lebo.rest;

import com.lebo.entity.*;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.rest.dto.UserGoodsDto;
import com.lebo.rest.dto.UserVgDto;
import com.lebo.service.StatusService;
import com.lebo.service.VgService;
import com.lebo.service.account.AccountService;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private StatusService statusService;

    private static final String API_1_1_VG = "/api/1.1/vg/";

    @RequestMapping(value = API_1_1_VG + "goldProducts.json", method = RequestMethod.GET)
    @ResponseBody
    public Object goldProducts() {
        return vgService.toProductDtos(vgService.findAllGoldProducts());
    }

    @RequestMapping(value = API_1_1_VG + "buyGold.json", method = RequestMethod.POST)
    @ResponseBody
    public Object buyGold(@RequestParam("productId") Long productId,
                          @RequestParam("paymentMethod") GoldOrder.PaymentMethod paymentMethod,
                          @RequestParam("alipayService") String alipayService,
                          @RequestParam("alipayPaymentType") String alipayPaymentType) {

        if (GoldOrder.PaymentMethod.ALIPAY == paymentMethod) {

            String params = vgService.getAlipayParams(
                    accountService.getCurrentUserId(),
                    productId,
                    alipayService,
                    alipayPaymentType);

            Map<String, String> result = new HashMap<String, String>(1);
            result.put("alipaySignedParams", params);

            return result;
        } else {
            return ErrorDto.badRequest("不支持该付款方式: [" + paymentMethod + "]");
        }

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

    @RequestMapping(value = API_1_1_VG + "giveGoods.json", method = RequestMethod.POST)
    @ResponseBody
    public Object giveGoods(@RequestParam(value = "postId") String postId,
                            @RequestParam("goodsId") long goodsId,
                            @RequestParam("quantity") int quantity) {

        if (StringUtils.isBlank(postId)) {
            return ErrorDto.badRequest("postId不能为空");
        }

        Post post = statusService.getPost(postId);

        if (post == null) {
            return ErrorDto.badRequest("帖子不存在(postId=" + postId + ")");
        }

        if (post.getOriginPostId() != null) {
            return ErrorDto.badRequest("postId不能为转发贴");
        }

        String toUserId = post.getUserId();

        vgService.giveGoods(accountService.getCurrentUserId(), toUserId, postId, goodsId, quantity);

        return ErrorDto.OK;
    }

}
