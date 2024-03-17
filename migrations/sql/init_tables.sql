create table if not exists chat
(
    id bigint primary key
);

create table if not exists link
(
    id           serial primary key,
    url          text                     not null,
    last_tracked timestamp with time zone not null
);

create table if not exists chat_link
(
    chat_id bigint references chat (id) on delete cascade,
    link_id integer references link (id) on delete restrict
);
