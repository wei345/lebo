DROP TABLE IF EXISTS vg_giver_value;
DROP TABLE IF EXISTS vg_gold_order;
DROP TABLE IF EXISTS vg_gold_product;
DROP TABLE IF EXISTS vg_user_info;
DROP TABLE IF EXISTS vg_user_goods;
DROP TABLE IF EXISTS vg_give_goods;
DROP TABLE IF EXISTS vg_goods;

CREATE TABLE vg_gold_product (
  id          BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name        VARCHAR(255)  NOT NULL UNIQUE,
  description VARCHAR(1024),
  price       DECIMAL(5, 2) NOT NULL,
  price_unit  VARCHAR(100)  NOT NULL,
  discount    DECIMAL(5, 2) NOT NULL,
  image       VARCHAR(1024),
  gold        INT           NOT NULL
)
  ENGINE =InnoDB;

CREATE TABLE vg_gold_order (
  id               BIGINT        NOT NULL PRIMARY KEY,
  user_id          VARCHAR(24)   NOT NULL,
  gold_product_id  BIGINT        NOT NULL,
  quantity         INT           NOT NULL,
  discount         DECIMAL(5, 2) NOT NULL,
  subject          VARCHAR(1024) NOT NULL,
  total_cost       DECIMAL(5, 2) NOT NULL,
  gold             INT           NOT NULL,
  order_date       DATETIME      NOT NULL,
  status           VARCHAR(32),
  payment_method   VARCHAR(32),
  alipay_status    VARCHAR(32),
  alipay_notify_id VARCHAR(255),
  FOREIGN KEY (gold_product_id) REFERENCES vg_gold_product (id)
)
  ENGINE =InnoDB;

CREATE TABLE vg_goods (
  id            BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name          VARCHAR(255) NOT NULL UNIQUE,
  description   VARCHAR(1024),
  price         INT          NOT NULL,
  image_normal  VARCHAR(1024),
  image_bigger  VARCHAR(1024),
  quantity_unit VARCHAR(8)
)
  ENGINE =InnoDB;

CREATE TABLE vg_user_info (
  user_id      VARCHAR(24) PRIMARY KEY,
  gold         INT           NOT NULL DEFAULT 0,
  consume_gold INT           NOT NULL DEFAULT 0,
  recharge     DECIMAL(9, 2) NOT NULL DEFAULT 0,
  popularity   INT           NOT NULL DEFAULT 0
)
  ENGINE =InnoDB;

CREATE TABLE vg_user_goods (
  user_id  VARCHAR(24) NOT NULL,
  goods_id BIGINT      NOT NULL,
  quantity INT         NOT NULL,
  FOREIGN KEY (goods_id) REFERENCES vg_goods (id),
  PRIMARY KEY (user_id, goods_id)
)
  ENGINE =InnoDB;

CREATE TABLE vg_give_goods (
  id           BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
  from_user_id VARCHAR(24) NOT NULL,
  to_user_id   VARCHAR(24) NOT NULL,
  post_id      VARCHAR(24) NOT NULL,
  goods_id     BIGINT      NOT NULL,
  quantity     INT         NOT NULL,
  give_date    DATETIME    NOT NULL,
  FOREIGN KEY (goods_id) REFERENCES vg_goods (id)
)
  ENGINE =InnoDB;

CREATE TABLE vg_giver_value (
  user_id    VARCHAR(24) NOT NULL,
  giver_id   VARCHAR(24) NOT NULL,
  give_value INT         NOT NULL,
  PRIMARY KEY (user_id, giver_id)
)
  ENGINE =InnoDB;

INSERT INTO vg_gold_product (id, name, description, price, price_unit, discount, image, gold) VALUES (1, '10个金币', '金币是一种全部或大部份由黄金制造的硬币。', 1, 'CNY', 0, 'images/gold/gold-10.png', 10);
INSERT INTO vg_gold_product (id, name, description, price, price_unit, discount, image, gold) VALUES (2, '50个金币', '黄金差不多在硬币发明之初，就因其价值被用来当作硬币。', 5, 'CNY', 0, 'images/gold/gold-50.png', 50);
INSERT INTO vg_gold_product (id, name, description, price, price_unit, discount, image, gold) VALUES (3, '200个金币', '黄金作为货币有很多理由。它的买卖价差低。', 20, 'CNY', -2, 'images/gold/gold-200.png', 200);
INSERT INTO vg_gold_product (id, name, description, price, price_unit, discount, image, gold) VALUES (4, '500个金币', '黄金可以分割成小单位，而不损害其价值；它也可以熔成锭，并且重铸成硬币。', 50, 'CNY', -10, 'images/gold/gold-500.png', 500);
INSERT INTO vg_gold_product (id, name, description, price, price_unit, discount, image, gold) VALUES (5, '1000个金币', '黄金的密度比大多数金属高，使假币很难流通。', 100, 'CNY', -30, 'images/gold/gold-1000.png', 1000);

INSERT INTO vg_goods (id, name, description, price, image_normal, image_bigger, quantity_unit) VALUES (1, '贝壳', '点缀海滩的可爱动物，据说每得到一个贝壳可以增加3点人气哦！', 3, 'images/vg/shell-118x118.png', 'images/vg/shell-220x222.png', '个');
INSERT INTO vg_goods (id, name, description, price, image_normal, image_bigger, quantity_unit) VALUES (2, '玫瑰', 'Rosa rugosa，浪漫的象征。如果你送一束玫瑰给你的好友，那么TA会很浪漫的增长10点人气', 10, 'images/vg/rose-118x118.png', 'images/vg/rose-220x222.png', '朵');
INSERT INTO vg_goods (id, name, description, price, image_normal, image_bigger, quantity_unit) VALUES (3, '钻石', '身为土豪的你每赠送出一颗钻石，得到的人都会增长100点人气。证明你身份的时候到了！', 100, 'images/vg/diamond-118x118.png', 'images/vg/diamond-220x222.png', '个');

/* 测试数据
update vg_gold_product set price = 0.01, discount = 0;
 */
