create table dish
(
    id          bigserial
        primary key,
    description varchar(512) not null
        constraint uk_o4luoamsfc4omdwjld1yr39i5
            unique,
    name        varchar(255) not null
        constraint uk_r7g2l08wdh3uv3gvurli4s1bx
            unique
);

create table ingredients
(
    id          bigserial
        primary key,
    description varchar(255),
    name        varchar(255)
);

create table national_cuisine
(
    id      bigserial
        primary key,
    cuisine varchar(64) not null
        constraint uk_5wm4k31jc60bnubtrwq7xxfgy
            unique
);

create table roles
(
    id   bigserial
        primary key,
    name varchar(20)
);

create table tastes
(
    id    bigserial
        primary key,
    taste varchar(32) not null
        constraint uk_i86xmvvoq7fm3a6qxqnhdabak
            unique
);


create table users
(
    login    varchar(255) not null
        primary key,
    email    varchar(255),
    password varchar(255)
);

create table basic.public.user_roles
(
    user_id varchar(255) not null
        constraint fkhfh9dx7w3ubf1co1vdev94g3f
            references users,
    role_id bigint       not null
        constraint fkh8ciramu9cc9q3qcqiv4ue8a6
            references roles,
    primary key (user_id, role_id)
);

create table basic.public.recipe
(
    id            bigserial
        primary key,
    count_portion integer,
    description   varchar(4096) not null,
    dish_id       bigint        not null
        constraint fk6gd6sjjumfpbk8w03lv6ohwb5
            references dish,
    cuisine_id    bigint        not null
        constraint fkj4cru65f4lmfhyd8sv3hkiieu
            references national_cuisine,
    user_login    varchar(255)  not null
        constraint fkiwcy2cd7acsb9oheknkgl409
            references users
);

create table basic.public.recipe_ingredients
(
    recipe_id     bigint not null
        constraint fkarveb89dhxk087lbsoskneukg
            references recipe_on_reviews,
    ingredient_id bigint not null
        constraint fkgukrw6na9f61kb8djkkuvyxy8
            references ingredients
);

create table basic.public.recipe_tastes
(
    recipe_id bigint not null
        constraint fkd5tavyw42kmlwu1kosw2cb124
            references recipe_on_reviews,
    taste_id  bigint not null
        constraint fkbby7brghh3e52bpu6hut2sp2k
            references tastes
);

create table basic.public.recipe_on_reviews
(
    id            bigserial
        primary key,
    count_portion integer,
    description   varchar(4096) not null,
    update_recipe boolean       not null,
    dish_id       bigint        not null
        constraint fkmxtk273l1qmxs76klcbepwbsn
            references dish,
    cuisine_id    bigint        not null
        constraint fkmpely652jq6ak8m97sf67xwkp
            references national_cuisine,
    user_login    varchar(255)  not null
        constraint fk54yh8wcwdp8slrii1ecs9xsmq
            references users
);



