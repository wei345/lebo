package com.lebo.service;

import com.lebo.entity.Option;
import com.lebo.repository.OptionDao;
import com.lebo.service.param.PaginationParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * @author: Wei Liu
 * Date: 13-7-18
 * Time: PM5:27
 */
@Service
public class OptionService extends AbstractMongoService {
    @Autowired
    private OptionDao optionDao;

    private Option option;

    public Option getOption() {
        //TODO getOption使用缓存，更新时通知所有节点，不要每次都reloadOption
        /*if (option == null) {
            reloadOption();
        }
        return option;*/

        return reloadOption();
    }

    public Option saveOption(Option option) {
        return optionDao.save(option);
    }

    public Option reloadOption() {
        PageRequest pageRequest = new PageRequest(0, 1, PaginationParam.DEFAULT_SORT);
        Page<Option> page = optionDao.findAll(pageRequest);
        if (page.getContent().size() == 0) {
            option = saveOption(new Option());
        } else {
            option = page.getContent().get(0);
        }
        return option;
    }
}
