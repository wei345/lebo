package com.lebo.rest;

import com.lebo.service.AlipayService;
import com.lebo.service.EcService;
import com.lebo.service.account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Wei Liu
 * Date: 13-10-29
 * Time: PM6:16
 */
@Controller
public class EcRestController {
    @Autowired
    private EcService ecService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AlipayService alipayService;
    private static final String API_1_1_EC = "/api/1.1/ec/";

    private Logger logger = LoggerFactory.getLogger(EcRestController.class);

    @RequestMapping(value = API_1_1_EC + "createOrder.json", method = RequestMethod.POST)
    @ResponseBody
    public Object createOrder(@RequestParam("productId") Long productId) {
        return ecService.toOrderDto(ecService.createOrder(productId, accountService.getCurrentUserId()));
    }

    @RequestMapping(value = API_1_1_EC + "goldProducts.json", method = RequestMethod.GET)
    @ResponseBody
    public Object goldProducts() {
        return ecService.findProductByCategoryId(1L);
    }

    @RequestMapping(value = API_1_1_EC + "alipaySigedParams.json", method = RequestMethod.GET)
    @ResponseBody
    public Object alipaySigedParams(@RequestParam("productId") Long productId,
                                    @RequestParam("service") String service,
                                    @RequestParam("paymentType") String paymentType
    ) {

        String signed = ecService.getAlipayParams(accountService.getCurrentUserId(), productId, service, paymentType);

        Map<String, String> result = new HashMap<String, String>(1);
        result.put("signed", signed);
        return result;
    }

    /**
     * 支付宝支付回调接口。
     * <p/>
     * 详见《移动快捷支付应用集成接入包支付接口.pdf - v1.1》 第15页
     */
    @RequestMapping(value = API_1_1_EC + "alipayNotify", method = RequestMethod.POST)
    @ResponseBody
    public Object alipayNotify(@RequestParam("notify_time") String notifyTime,
                               @RequestParam("notify_type") String notifyType,
                               @RequestParam("notify_id") String notifyId,
                               @RequestParam("sign_type") String signType,
                               @RequestParam("sign") String sign,
                               @RequestParam(value = "out_trade_no", required = false) String outTradeNo,
                               @RequestParam(value = "subject", required = false) String subject,
                               @RequestParam(value = "payment_type", required = false) String paymentType,
                               @RequestParam(value = "trade_no", required = false) String tradeNo,
                               @RequestParam(value = "trade_status", required = false) String tradeStatus,
                               @RequestParam(value = "seller_id", required = false) String sellerId,
                               @RequestParam(value = "seller_email", required = false) String sellerEmail,
                               @RequestParam(value = "buyer_id", required = false) String buyerId,
                               @RequestParam(value = "buyer_email", required = false) String buyerEmail,
                               @RequestParam(value = "total_fee", required = false) BigDecimal totalFee,
                               @RequestParam(value = "quantity", required = false) Integer quantity,
                               @RequestParam(value = "price", required = false) BigDecimal price,
                               @RequestParam(value = "body", required = false) String body,
                               @RequestParam(value = "gmt_create", required = false) String gmtCreate,
                               @RequestParam(value = "gmt_payment", required = false) String gmtPayment,
                               @RequestParam(value = "is_total_fee_adjust", required = false) String isTotalFeeAdjust,
                               @RequestParam(value = "use_coupon", required = false) String useCoupon,
                               @RequestParam(value = "discount", required = false) String discount,
                               HttpServletRequest request) {

        logger.debug("支付宝回调参数:");
        Map<String, String> params = request.getParameterMap();
        for (Map.Entry entry : params.entrySet()) {
            logger.debug("    {} : {}", entry.getKey(), entry.getValue());
        }

        logger.debug("正在验证签名..");
        if (alipayService.rsaCheckV1(params)) {
            logger.debug("通过");
        } else {
            logger.debug("未通过");
            return "error";
        }

        return "success";
    }

}
