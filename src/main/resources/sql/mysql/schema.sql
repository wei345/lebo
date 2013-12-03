drop table if exists vg_gold_order;
drop table if exists vg_gold_product;
drop table if exists vg_user_gold;
drop table if exists vg_user_goods;
drop table if exists vg_give_goods;
drop table if exists vg_goods;

create table vg_gold_product (
      id bigint not null auto_increment primary key,
    	name varchar(255) not null unique,
    	description varchar(1024),
    	price decimal (19,2) not null,
    	price_unit varchar(100) not null,
      discount decimal (5,2) not null,
    	image varchar(1024),
    	gold_quantity int not null
) engine=InnoDB;

create table vg_gold_order (
    id bigint not null auto_increment primary key,
    user_id varchar(24) not null,
    gold_product_id bigint not null,
    quantity int not null,
    discount decimal (5,2) not null,
    order_date datetime not null,
    status varchar(32),
    alipay_status varchar(32),
    foreign key (gold_product_id) references vg_gold_product(id)
) engine=InnoDB;

create table vg_goods (
    id bigint not null auto_increment primary key,
    name varchar(255) not null unique,
    description varchar(1024),
    price int not null,
    image_normal varchar(1024),
    image_bigger varchar(1024),
    quantity_unit varchar(8)
) engine=InnoDB;

create table vg_user_gold (
    user_id varchar(24) primary key,
    gold_quantity bigint not null
) engine=InnoDB;

create table vg_user_goods (
    user_id varchar(24) not null,
    goods_id bigint not null,
    quantity int not null,
    foreign key (goods_id) references vg_goods(id),
    primary key(user_id, goods_id)
) engine=InnoDB;

create table vg_give_goods (
    id bigint not null auto_increment primary key,
    from_user_id varchar(24) not null,
    to_user_id varchar(24) not null,
    goods_id bigint not null,
    quantity int not null,
    give_date datetime not null,
    foreign key (goods_id) references vg_goods(id)
) engine=InnoDB;

insert into vg_gold_product(id, name, description, price, price_unit, discount, image, gold_quantity) values(1, '10个金币', '金币是一种全部或大部份由黄金制造的硬币。', 1, 'CNY', 0, 'images/gold/gold-10.png', 10);
insert into vg_gold_product(id, name, description, price, price_unit, discount, image, gold_quantity) values(2, '50个金币', '黄金差不多在硬币发明之初，就因其价值被用来当作硬币。', 5, 'CNY', 0, 'images/gold/gold-50.png', 50);
insert into vg_gold_product(id, name, description, price, price_unit, discount, image, gold_quantity) values(3, '200个金币', '黄金作为货币有很多理由。它的买卖价差低。', 20, 'CNY', -2, 'images/gold/gold-200.png', 200);
insert into vg_gold_product(id, name, description, price, price_unit, discount, image, gold_quantity) values(4, '500个金币', '黄金可以分割成小单位，而不损害其价值；它也可以熔成锭，并且重铸成硬币。', 50, 'CNY', -10, 'images/gold/gold-500.png', 500);
insert into vg_gold_product(id, name, description, price, price_unit, discount, image, gold_quantity) values(5, '1000个金币', '黄金的密度比大多数金属高，使假币很难流通。', 100, 'CNY', -30, 'images/gold/gold-1000.png', 1000);

insert into vg_goods(id, name, description, price, image_normal, image_bigger, quantity_unit) values(1, '贝壳', '贝壳，泛指软体动物的外壳。贝壳通常可以在海滩发现到，内里的生物通常已在冲上岸前消失。由于部份贝壳外形漂亮，有人会有收集贝壳的嗜好。', 3, 'images/goods/shell.png', 'images/goods/shell-bigger.png', '个');
insert into vg_goods(id, name, description, price, image_normal, image_bigger, quantity_unit) values(2, '玫瑰', '玫瑰（学名：Rosa rugosa）是蔷薇科蔷薇属植物，在日常生活中是蔷薇属一系列花大艳丽的栽培品种的统称。', 10, 'images/goods/rose.png', 'images/goods/rose-bigger.png', '朵');
insert into vg_goods(id, name, description, price, image_normal, image_bigger, quantity_unit) values(3, '钻石', '钻石（英文：Diamond），化学中一般称为金刚石，钻石为经过琢磨的金刚石，金刚石则是指钻石的原石。', 100, 'images/goods/diamond.png', 'images/goods/diamond-bigger.png', '个');


