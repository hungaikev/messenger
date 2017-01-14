# --- !Ups

CREATE TABLE "messages" (
  "message_id" SERIAL NOT NULL PRIMARY KEY,
  "from" VARCHAR NOT NULL,
  "description" VARCHAR NOT NULL,
  "to" VARCHAR NOT NULL
);


# --- !Downs
DROP TABLE messages