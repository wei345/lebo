package com.lebo.rest;

import com.lebo.service.AlipayService;
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
public class AlipayRestController {
    @Autowired
    private AlipayService alipayService;
    private static final String PREFIX_API_1_1_ALIPAY = "/api/1.1/alipay/";

    @RequestMapping(value = PREFIX_API_1_1_ALIPAY + "sign.json", method = RequestMethod.GET)
    @ResponseBody
    public Object sign(@RequestParam("stringToSign") String stringToSign){
        String sign = alipayService.sign(stringToSign);

        Map<String, String> result = new HashMap<String, String>(1);
        result.put("sign", sign);

        return sign;
    }

}
