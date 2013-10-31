drop table if exists product;
drop table if exists product_category;

create table product_category (
      product_category_id bigint not null auto_increment primary key,
    	name varchar(255) not null unique
) engine=InnoDB;

create table product (
      product_id bigint not null auto_increment primary key,
    	name varchar(255) not null unique,
    	description varchar(1024),
    	price decimal (19,2) not null,
    	price_unit varchar(100) not null,
      discount decimal (5,2) not null,
    	image varchar(1024),
    	product_category_id bigint not null,
      foreign key (product_category_id) references product_category(product_category_id)
) engine=InnoDB;

insert into product_category(product_category_id, name) values(1, '金币');
insert into product_category(product_category_id, name) values(2, '礼物');

insert into product(product_id, name, price, price_unit, discount, image, product_category_id) values(1, '10个金币', 1, 'RMB', 0, 'images/products/gold-10.png', 1);
insert into product(product_id, name, price, price_unit, discount, image, product_category_id) values(2, '50个金币', 5, 'RMB', 0, 'images/products/gold-50.png', 1);
insert into product(product_id, name, price, price_unit, discount, image, product_category_id) values(3, '200个金币', 20, 'RMB', 0.1, 'images/products/gold-200.png', 1);
insert into product(product_id, name, price, price_unit, discount, image, product_category_id) values(4, '500个金币', 50, 'RMB', 0.2, 'images/products/gold-500.png', 1);
insert into product(product_id, name, price, price_unit, discount, image, product_category_id) values(5, '1000个金币', 100, 'RMB', 0.3, 'images/products/gold-1000.png', 1);
insert into product(product_id, name, price, price_unit, discount, image, product_category_id) values(6, '贝壳', 3, 'GOLD', 0, 'images/products/shell.png', 2);
insert into product(product_id, name, price, price_unit, discount, image, product_category_id) values(7, '玫瑰', 10, 'GOLD', 0, 'images/products/shell.png', 2);
insert into product(product_id, name, price, price_unit, discount, image, product_category_id) values(8, '钻石', 100, 'GOLD', 0, 'images/products/diamond.png', 2);


