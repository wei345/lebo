drop table if exists vg_gold_order;
drop table if exists vg_gold_product;
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
    mongo_user_id varchar(24) not null,
    gold_product_id bigint not null,
    quantity int not null,
    discount decimal (5,2) not null,
    order_date datetime not null,
    status varchar(32),
    foreign key (gold_product_id) references vg_gold_product(id)
) engine=InnoDB;

create table vg_goods (
      id bigint not null auto_increment primary key,
    	name varchar(255) not null unique,
    	description varchar(1024),
    	price decimal (19,2) not null,
      discount decimal (5,2) not null,
    	image varchar(1024)
) engine=InnoDB;

insert into vg_gold_product(id, name, price, price_unit, discount, image, gold_quantity) values(1, '10个金币', 1, 'RMB', 0, 'images/products/gold-10.png', 10);
insert into vg_gold_product(id, name, price, price_unit, discount, image, gold_quantity) values(2, '50个金币', 5, 'RMB', 0, 'images/products/gold-50.png', 50);
insert into vg_gold_product(id, name, price, price_unit, discount, image, gold_quantity) values(3, '200个金币', 20, 'RMB', -2, 'images/products/gold-200.png', 200);
insert into vg_gold_product(id, name, price, price_unit, discount, image, gold_quantity) values(4, '500个金币', 50, 'RMB', -10, 'images/products/gold-500.png', 500);
insert into vg_gold_product(id, name, price, price_unit, discount, image, gold_quantity) values(5, '1000个金币', 100, 'RMB', -30, 'images/products/gold-1000.png', 1000);

insert into vg_goods(id, name, price, discount, image) values(1, '贝壳', 3, 0, 'images/products/shell.png');
insert into vg_goods(id, name, price, discount, image) values(2, '玫瑰', 10, 0, 'images/products/shell.png');
insert into vg_goods(id, name, price, discount, image) values(3, '钻石', 100, 0, 'images/products/diamond.png');


