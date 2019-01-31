CREATE TABLE role (
    id INT NOT NULL AUTO_INCREMENT,
    role_name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE user (
    id INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    second_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    pass VARCHAR(100) NOT NULL,
    address VARCHAR(250) NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (role_id)
        REFERENCES role (id)
);

CREATE TABLE purchase (
    id INT NOT NULL AUTO_INCREMENT,
    date_created DATE NOT NULL,
    user_id INT NOT NULL,
    status VARCHAR(200) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id)
        REFERENCES user (id)
);

CREATE TABLE product (
    id INT NOT NULL AUTO_INCREMENT,
    product_name VARCHAR(200) NOT NULL UNIQUE,
    description VARCHAR(400),
    price DECIMAL(10 , 2) NOT NULL,
    stock INT NOT NULL,
    artist VARCHAR(100) NOT NULL,
    category VARCHAR(200) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE purchase_product (
    purchase_id INT NOT NULL,
    product_id INT NOT NULL,
    nr_items INT NOT NULL,
    FOREIGN KEY (purchase_id)
        REFERENCES purchase (id),
    FOREIGN KEY (product_id)
        REFERENCES product (id)
);

CREATE TABLE cart (
    id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id)
        REFERENCES user (id)
);

CREATE TABLE product_cart (
    cart_id INT NOT NULL,
    product_id INT NOT NULL,
    nr_items INT NOT NULL,
    FOREIGN KEY (cart_id)
        REFERENCES cart (id),
    FOREIGN KEY (product_id)
        REFERENCES product (id)
);