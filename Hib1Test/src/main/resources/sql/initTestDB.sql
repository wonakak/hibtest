CREATE TABLE category
(
    id BIGINT PRIMARY KEY NOT NULL,
    title VARCHAR(255),
    parent_category BIGINT
);
CREATE TABLE "order"
(
    id BIGINT PRIMARY KEY NOT NULL,
    status BOOLEAN,
    user_id BIGINT
);
CREATE TABLE order_item
(
    id BIGINT PRIMARY KEY NOT NULL,
    count INTEGER,
    sell_price NUMERIC(19,2),
    order_id BIGINT,
    product_id BIGINT
);
CREATE TABLE person
(
    id BIGINT PRIMARY KEY NOT NULL,
    age INTEGER,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    salary NUMERIC(19,2)
);
CREATE TABLE product
(
    id BIGINT PRIMARY KEY NOT NULL,
    description VARCHAR(255),
    price NUMERIC(19,2),
    title VARCHAR(255),
    product_category_id BIGINT
);
CREATE TABLE role
(
    id BIGINT PRIMARY KEY NOT NULL,
    title VARCHAR(255)
);
CREATE TABLE users
(
    id BIGINT PRIMARY KEY NOT NULL,
    person_id BIGINT
);
CREATE TABLE users_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT users_roles_pkey PRIMARY KEY (user_id, role_id)
);
ALTER TABLE category ADD FOREIGN KEY (parent_category) REFERENCES category (id);
ALTER TABLE "order" ADD FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE order_item ADD FOREIGN KEY (order_id) REFERENCES "order" (id);
ALTER TABLE order_item ADD FOREIGN KEY (product_id) REFERENCES product (id);
ALTER TABLE product ADD FOREIGN KEY (product_category_id) REFERENCES category (id);
ALTER TABLE users ADD FOREIGN KEY (person_id) REFERENCES person (id);
ALTER TABLE users_roles ADD FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE users_roles ADD FOREIGN KEY (role_id) REFERENCES role (id);
