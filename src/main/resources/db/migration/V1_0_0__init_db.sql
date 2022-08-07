--
-- PostgreSQL database dump
--

-- Dumped from database version 14.4
-- Dumped by pg_dump version 14.4

-- Started on 2022-08-06 20:16:56
--
SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE "top-ten";


CREATE DATABASE "top-ten" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'English_Israel.1255';


\connect -reuse-previous=on "dbname='top-ten'"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;


CREATE TABLE public.comments (
    id bigint NOT NULL,
    content character varying(255) NOT NULL,
    create_date_time timestamp without time zone,
    parent_id bigint,
    update_date_time timestamp without time zone,
    user_id bigint NOT NULL,
    object character varying(255) NOT NULL,
    object_id bigint NOT NULL,
    active boolean DEFAULT true NOT NULL
);


CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE public.player (
    id bigint NOT NULL,
    create_date_time timestamp without time zone,
    unique_name character varying(255) NOT NULL,
    update_date_time timestamp without time zone
);


CREATE TABLE public.player_achievements (
    player_id bigint NOT NULL,
    all_defensive integer NOT NULL,
    all_nba integer NOT NULL,
    all_star integer NOT NULL,
    all_star_mvp integer NOT NULL,
    assist_champ integer NOT NULL,
    blocks_champ integer NOT NULL,
    championships integer NOT NULL,
    championships_aba integer NOT NULL,
    create_date_time timestamp without time zone,
    defensive_player_of_the_year integer NOT NULL,
    finals_mvp integer NOT NULL,
    league_mvp integer NOT NULL,
    most_improve integer NOT NULL,
    rebound_champ integer NOT NULL,
    rookie_of_the_year integer NOT NULL,
    scoring_champ integer NOT NULL,
    steals_champ integer NOT NULL,
    update_date_time timestamp without time zone
);


CREATE TABLE public.player_info (
    player_id bigint NOT NULL,
    dob character varying(255),
    nbadebut character varying(255),
    active boolean NOT NULL,
    country character varying(255),
    create_date_time timestamp without time zone,
    draft character varying(255),
    experience character varying(255),
    full_name character varying(255),
    height character varying(255),
    image_url character varying(255),
    "position" character varying(255),
    update_date_time timestamp without time zone,
    wight character varying(255),
    years_active character varying(255)
);


CREATE TABLE public.player_stats (
    id bigint NOT NULL,
    ast_per_game double precision NOT NULL,
    blk_per_game double precision NOT NULL,
    bpm double precision NOT NULL,
    create_date_time timestamp without time zone,
    efg_pct double precision NOT NULL,
    fg3pct double precision NOT NULL,
    fg_pct double precision NOT NULL,
    ft_pct double precision NOT NULL,
    games double precision NOT NULL,
    per double precision NOT NULL,
    pts_per_game double precision NOT NULL,
    stats_for character varying(255),
    stl_per_game double precision NOT NULL,
    tov_per_game double precision NOT NULL,
    trb_per_gme double precision NOT NULL,
    update_date_time timestamp without time zone,
    ws double precision NOT NULL,
    ws_per48 double precision NOT NULL,
    player_id bigint
);


CREATE TABLE public.rank_list (
    id bigint NOT NULL,
    create_date_time timestamp without time zone,
    title character varying(255),
    update_date_time timestamp without time zone,
    visibility character varying(255),
    user_id bigint NOT NULL
);


CREATE TABLE public.rank_list_item (
    id bigint NOT NULL,
    create_date_time timestamp without time zone,
    rank integer NOT NULL,
    update_date_time timestamp without time zone,
    player_id bigint NOT NULL,
    rank_list_id bigint
);


CREATE TABLE public.roles (
    role_id bigint NOT NULL,
    name character varying(255) NOT NULL
);


CREATE TABLE public.shedlock (
    name character varying(64) NOT NULL,
    lock_until timestamp without time zone NOT NULL,
    locked_at timestamp without time zone NOT NULL,
    locked_by character varying(255) NOT NULL
);


CREATE TABLE public.users (
    id bigint NOT NULL,
    create_date_time timestamp without time zone,
    email character varying(255) NOT NULL,
    enabled boolean NOT NULL,
    image_url character varying(255),
    locale character varying(255),
    name character varying(255) NOT NULL,
    provider character varying(255) NOT NULL,
    provider_id character varying(255),
    update_date_time timestamp without time zone
);


CREATE TABLE public.users_roles (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


CREATE TABLE public.votes (
    id bigint NOT NULL,
    create_date_time timestamp without time zone,
    update_date_time timestamp without time zone,
    user_id bigint NOT NULL,
    vote character varying(255),
    object character varying(255) NOT NULL,
    object_id bigint NOT NULL
);


ALTER TABLE ONLY public.comments
    ADD CONSTRAINT comments_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.player_achievements
    ADD CONSTRAINT player_achievements_pkey PRIMARY KEY (player_id);


ALTER TABLE ONLY public.player_info
    ADD CONSTRAINT player_info_pkey PRIMARY KEY (player_id);


ALTER TABLE ONLY public.player
    ADD CONSTRAINT player_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.player_stats
    ADD CONSTRAINT player_stats_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.rank_list_item
    ADD CONSTRAINT rank_list_item_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.rank_list
    ADD CONSTRAINT rank_list_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (role_id);


ALTER TABLE ONLY public.shedlock
    ADD CONSTRAINT shedlock_pkey PRIMARY KEY (name);


ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


ALTER TABLE ONLY public.votes
    ADD CONSTRAINT uk8sf3bv4xcdj9ynupvvpv84snh UNIQUE (user_id, object_id, object);


ALTER TABLE ONLY public.player
    ADD CONSTRAINT uk_ds7hcoq39teio2gvdxn6ge0ur UNIQUE (unique_name);


ALTER TABLE ONLY public.roles
    ADD CONSTRAINT ukofx66keruapi6vyqpv6f2or37 UNIQUE (name);


ALTER TABLE ONLY public.rank_list_item
    ADD CONSTRAINT ukoht6o0qrtdf7eehuk24o4o4qh UNIQUE (rank_list_id, player_id);


ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT users_roles_pkey PRIMARY KEY (user_id, role_id);


ALTER TABLE ONLY public.votes
    ADD CONSTRAINT votes_pkey PRIMARY KEY (id);


ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT fk2o0jvgh89lemvvo17cbqvdxaa FOREIGN KEY (user_id) REFERENCES public.users(id);


ALTER TABLE ONLY public.rank_list_item
    ADD CONSTRAINT fk4uoft9ep6ckkttvdb3p8512t2 FOREIGN KEY (rank_list_id) REFERENCES public.rank_list(id);


ALTER TABLE ONLY public.comments
    ADD CONSTRAINT fk8omq0tc18jd43bu5tjh6jvraq FOREIGN KEY (user_id) REFERENCES public.users(id);


ALTER TABLE ONLY public.player_info
    ADD CONSTRAINT fkfedyal64ud4fbae5tf2aykh4d FOREIGN KEY (player_id) REFERENCES public.player(id);


ALTER TABLE ONLY public.player_stats
    ADD CONSTRAINT fkfekdv3tvbrd0b8u6c2fuxk5fw FOREIGN KEY (player_id) REFERENCES public.player(id);


ALTER TABLE ONLY public.rank_list_item
    ADD CONSTRAINT fkfnd9pok7q0d64qtvhl2fb3cw3 FOREIGN KEY (player_id) REFERENCES public.player(id);


ALTER TABLE ONLY public.users_roles
    ADD CONSTRAINT fkj6m8fwv7oqv74fcehir1a9ffy FOREIGN KEY (role_id) REFERENCES public.roles(role_id);


ALTER TABLE ONLY public.rank_list
    ADD CONSTRAINT fkjemm8cgydoyfoemx1m66f7pc1 FOREIGN KEY (user_id) REFERENCES public.users(id);


ALTER TABLE ONLY public.player_achievements
    ADD CONSTRAINT fkjx3pmxw8hmbd72m9p72cdg1o6 FOREIGN KEY (player_id) REFERENCES public.player(id);


-- Completed on 2022-08-06 20:16:57

--
-- PostgreSQL database dump complete
--

