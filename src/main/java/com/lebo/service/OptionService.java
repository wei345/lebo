package com.lebo.service;

import com.lebo.entity.Option;
import com.lebo.repository.OptionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-7-18
 * Time: PM5:27
 */
@Service
public class OptionService extends AbstractMongoService {
    @Autowired
    private OptionDao optionDao;

    public Option getOption(){
        List<Option> options = optionDao.findAll();
        if(options.size() > 0){
            return optionDao.findAll().get(0);
        }else{
            return new Option();
        }
    }

    public Option saveOption(Option option){
        return optionDao.save(option);
    }
}
