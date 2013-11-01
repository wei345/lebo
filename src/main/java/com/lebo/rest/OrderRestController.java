package com.lebo.rest;

import com.lebo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: Wei Liu
 * Date: 13-10-29
 * Time: PM6:16
 */
@Controller
public class OrderRestController {
    @Autowired
    private OrderService orderService;
    private static final String PREFIX_API_1_1_ALIPAY = "/api/1.1/ec/";

    @RequestMapping(value = PREFIX_API_1_1_ALIPAY + "createOrder.json", method = RequestMethod.POST)
    @ResponseBody
    public Object createOrder(@RequestParam("productId") Long productId) {
        /*String sign = alipayService.sign(stringToSign);

        Map<String, String> result = new HashMap<String, String>(1);
        result.put("sign", sign);

        return result;*/
        return null;
    }

    @RequestMapping(value = PREFIX_API_1_1_ALIPAY + "goldProducts.json", method = RequestMethod.GET)
    @ResponseBody
    public Object goldProducts() {
        return orderService.findProductByCategoryId(1L);
    }

}
