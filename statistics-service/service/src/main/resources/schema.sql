CREATE TABLE IF NOT EXISTS STAT_HIT
(
    HIT_ID  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    APP     CHARACTER VARYING(100) NOT NULL,
    URI     CHARACTER VARYING(100) NOT NULL,
    IP      CHARACTER VARYING(100) NOT NULL,
    CREATED TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT "STAT_HIT_PK" PRIMARY KEY (HIT_ID)
);