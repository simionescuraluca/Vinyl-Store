create table role (
	id int not null auto_increment,
    rolename varchar(100) not null,
    
    primary key ( id ) 
);


create table user (
	id int not null auto_increment,
    firstname varchar(100) not null ,
    secondname varchar(100) not null, 
    email varchar(100) not null unique ,
    pass varchar(100) not null ,
    address varchar(250) not null ,
    role_id int ,
    
    primary key (id) ,
    foreign key (role_id) references role(id)
);


create table purchase (
	id int not null auto_increment,
    date_created date not null,
    user_id int ,
    status varchar(200) not null,
    
    primary key ( id ),
    foreign key ( user_id ) references user(id)
);
    

create table product(
	id int not null auto_increment,
    product_name varchar(200) not null unique,
    description varchar (400),
    price decimal(10,2) not null,
    stock int not null,
    artist varchar(100) not null,
    category varchar(200) not null,
    
    primary key ( id )
);
    

create table purchase_product (
	purchase_id int,
    product_id int,
    nr_items int not null,
    
    foreign key ( purchase_id ) references purchase(id),
    foreign key ( product_id ) references product(id)
    
);



create table cart (
	id int not null auto_increment,
    user_id int,
    
    primary key ( id ),
    foreign key ( user_id ) references user(id)
);
    


create table product_cart(
	cart_id int,
    product_id int,
    nr_items int not null,
    
    foreign key ( cart_id ) references cart(id),
    foreign key ( product_id ) references product(id)
);

    
	