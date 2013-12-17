package com.lebo.rest;

import com.lebo.service.AlipayService;
import com.lebo.service.VgService;
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Wei Liu
 * Date: 13-11-4
 * Time: PM3:41
 */
@Controller
public class AlipayRestController {

    @Autowired
    private AlipayService alipayService;
    @Autowired
    private VgService vgService;

    private Logger logger = LoggerFactory.getLogger(AlipayRestController.class);

    /**
     * 支付宝支付回调接口。
     * <p/>
     * 详见《移动快捷支付应用集成接入包支付接口.pdf - v1.1》 第15页
     */
    @RequestMapping(value = "/api/1/alipay/notify", method = RequestMethod.POST)
    @ResponseBody
    public Object alipayNotify(@RequestParam("notify_time") String notifyTime,
                               @RequestParam("notify_type") String notifyType,
                               @RequestParam("notify_id") String notifyId,
                               @RequestParam("sign_type") String signType,
                               @RequestParam("sign") String sign,
                               @RequestParam(value = "out_trade_no", required = false) Long outTradeNo,
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

        logger.debug("正在验证是否支付宝请求..");
        if (alipayService.checkIfAlipayRequest(notifyId)) {
            logger.debug("是支付宝请求");
        } else {
            logger.debug("不是支付宝请求, 退出");
            return "error";
        }

        logger.debug("支付宝回调参数:");
        Enumeration names = request.getParameterNames();
        Map<String, String> params = new HashMap<String, String>();
        while (names.hasMoreElements()) {
            String name = names.nextElement().toString();
            String value = request.getParameter(name);

            logger.info("    {} : {}", name, value);
            params.put(name, value);
        }

        logger.debug("正在验证签名..");
        if (alipayService.rsaCheckV1(params)) {
            logger.debug("通过");
        } else {
            logger.debug("未通过, 退出");
            return "error";
        }

        try {

            vgService.handleAlipayNotify(outTradeNo, AlipayService.AlipayStatus.valueOf(tradeStatus), notifyId);
            logger.debug("处理支付宝通知完成");

            return "success";

        } catch (Exception e) {

            logger.warn("处理支付宝通知发生错误", e);
            return "error";
        }
    }
}
