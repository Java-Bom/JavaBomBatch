#------------------------------------------------------------------------------
#-- Table 명 : customer_jdbc (고객)
#------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS customer_jdbc
(
    id            bigint AUTO_INCREMENT NOT NULL COMMENT 'ID',

    firstName     varchar(200),
    middleInitial varchar(200),
    lastName      varchar(200),
    address       varchar(200),
    street        varchar(200),
    city          varchar(200),
    state         varchar(200),
    zipCode       varchar(200),
    CONSTRAINT pk_customer PRIMARY KEY (id)
) ENGINE = InnoDB COMMENT ='고객';
