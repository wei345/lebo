package com.lebo.repository.mybatis;

import com.lebo.entity.PostGoods;
import com.lebo.repository.MyBatisRepository;

import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-11-5
 * Time: AM10:59
 */
@MyBatisRepository
public interface PostGoodsDao {
    List<PostGoods> getByPostId(String postId);

    PostGoods get(PostGoods postGoods);

    void insert(PostGoods postGoods);

    void updateQuantity(PostGoods postGoods);
}
