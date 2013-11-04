package com.lebo.rest;

import com.lebo.service.VgService;
import com.lebo.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
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
    public Object order(@RequestParam("orderId") Long orderId) {

        return vgService.toGoldOrderDto(vgService.getOrderWithDetail(orderId));
    }
}
