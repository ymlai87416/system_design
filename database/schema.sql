create database testapp;

use testapp;

create table user (
  id integer auto_increment primary key,
  name varchar(255) not null,
  email varchar(255) not null,
  password varchar(255) not null,
  created_at timestamp default now()
);

create table message (
  id integer auto_increment primary key,
  body text not null,
  user_id integer not null,
  created_at timestamp default now(),
  updated_at timestamp default now(),
  foreign key (user_id) references user (id)
);

create table word_count(
    user_id integer, 
    word varchar(255) not null,
    count integer not null,
    created_at timestamp default now(),
    updated_at timestamp default now(),
    foreign key (user_id) references user (id), 
    primary key (user_id, word)
);

create table word_count_stage(
  user_id integer,
  word  varchar(255) not null,
  count integer not null,
  batch_id varchar(255) not null
)

insert into user (name, email, password) values ( "Test", "test@123.com", "password");

insert into message ( body, user_id) values ("Preserved defective offending he daughters on or. Rejoiced prospect yet material servants out answered men admitted. Sportsmen certainty prevailed suspected am as. Add stairs admire all answer the nearer yet length. Advantages prosperous remarkably my inhabiting so reasonably be if. Too any appearance announcing impossible one. Out mrs means heart ham tears shall power every.", 1);
insert into message (body, user_id) values ("As absolute is by amounted repeated entirely ye returned. These ready timed enjoy might sir yet one since. Years drift never if could forty being no. On estimable dependent as suffering on my. Rank it long have sure in room what as he. Possession travelling sufficient yet our. Talked vanity looked in to. Gay perceive led believed endeavor. Rapturous no of estimable oh therefore direction up. Sons the ever not fine like eyes all sure.", 1);
insert into message (body, user_id) values ("Sense child do state to defer mr of forty. Become latter but nor abroad wisdom waited. Was delivered gentleman acuteness but daughters. In as of whole as match asked. Pleasure exertion put add entrance distance drawings. In equally matters showing greatly it as. Want name any wise are able park when. Saw vicinity judgment remember finished men throwing.", 1);

insert into word_count (user_id, word, count) values (1, 'cloud', 10);
insert into word_count (user_id, word, count) values (1, 'apple', 12);
insert into word_count (user_id, word, count) values (1, 'tree', 15);
insert into word_count (user_id, word, count) values (1, 'cat', 20);

insert into word_count_stage (user_id, word, count) values (1, 'tree', 1, 'TOM_TEST');
insert into word_count_stage (user_id, word, count) values (1, 'tom', 2, 'TOM_TEST');


