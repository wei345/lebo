package com.lebo.rest;

import com.lebo.entity.Ad;
import com.lebo.rest.dto.HotDto;
import com.lebo.rest.dto.StatusDto;
import com.lebo.service.AdService;
import com.lebo.service.StatusService;
import com.lebo.service.param.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-10-11
 * Time: PM3:21
 */
@RequestMapping("/api/1/pages")
@Controller
public class PageRestController {
    @Autowired
    private AdService adService;
    @Autowired
    private StatusService statusService;

    /**
     * 热门页:用户列表，按红心数排序，有广告
     */
    @RequestMapping(value = "hot", method = RequestMethod.GET)
    @ResponseBody
    public Object hot(@Valid PageRequest pageRequest,
                      @RequestParam(value = "ads", defaultValue = "true") boolean ads) {
        HotDto dto = new HotDto();

        if (ads) {
            dto.setAds(adService.toDtos(adService.findAds(Ad.GROUP_HOT)));
        }

        List<StatusDto> statuses = statusService.hotPosts(pageRequest.getPage(), pageRequest.getSize());
        dto.setStatuses(statuses);

        return dto;
    }
}
