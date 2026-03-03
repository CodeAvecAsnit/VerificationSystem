CREATE TABLE item_table
(
    item_id   BINARY(16)  NOT NULL,
    item_name VARCHAR(30) NULL,
    CONSTRAINT pk_item_table PRIMARY KEY (item_id)
);