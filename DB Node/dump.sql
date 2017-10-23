--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.8
-- Dumped by pg_dump version 9.5.8

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = public, pg_catalog;

ALTER TABLE IF EXISTS ONLY public.user_channel DROP CONSTRAINT IF EXISTS user_channel_users_id_fk;
ALTER TABLE IF EXISTS ONLY public.user_channel DROP CONSTRAINT IF EXISTS user_channel_channels_id_fk;
ALTER TABLE IF EXISTS ONLY public.messages DROP CONSTRAINT IF EXISTS messages_users_id_fk;
ALTER TABLE IF EXISTS ONLY public.messages DROP CONSTRAINT IF EXISTS messages_channels_id_fk;
DROP INDEX IF EXISTS public.users_token_uindex;
DROP INDEX IF EXISTS public.user_channel_user_id_index;
DROP INDEX IF EXISTS public.user_channel_channel_id_index;
DROP INDEX IF EXISTS public.messages_user_id_index;
DROP INDEX IF EXISTS public.messages_channel_id_index;
ALTER TABLE IF EXISTS ONLY public.users DROP CONSTRAINT IF EXISTS users_source_id_auth_source_pk;
ALTER TABLE IF EXISTS ONLY public.users DROP CONSTRAINT IF EXISTS users_pkey;
ALTER TABLE IF EXISTS ONLY public.messages DROP CONSTRAINT IF EXISTS messages_pkey;
ALTER TABLE IF EXISTS ONLY public.channels DROP CONSTRAINT IF EXISTS channels_pkey1;
ALTER TABLE IF EXISTS public.users ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.messages ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.channels ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS public.users_id_seq1;
DROP TABLE IF EXISTS public.users;
DROP TABLE IF EXISTS public.user_channel;
DROP SEQUENCE IF EXISTS public.messages_id_seq;
DROP TABLE IF EXISTS public.messages;
DROP SEQUENCE IF EXISTS public.channels_id_seq1;
DROP TABLE IF EXISTS public.channels;
DROP SCHEMA IF EXISTS public;
--
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: channels; Type: TABLE; Schema: public; Owner: homestead
--

CREATE TABLE channels (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE channels OWNER TO homestead;

--
-- Name: channels_id_seq1; Type: SEQUENCE; Schema: public; Owner: homestead
--

CREATE SEQUENCE channels_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE channels_id_seq1 OWNER TO homestead;

--
-- Name: channels_id_seq1; Type: SEQUENCE OWNED BY; Schema: public; Owner: homestead
--

ALTER SEQUENCE channels_id_seq1 OWNED BY channels.id;


--
-- Name: messages; Type: TABLE; Schema: public; Owner: homestead
--

CREATE TABLE messages (
    id bigint NOT NULL,
    text text DEFAULT now() NOT NULL,
    created_at timestamp without time zone,
    channel_id bigint,
    user_id bigint
);


ALTER TABLE messages OWNER TO homestead;

--
-- Name: messages_id_seq; Type: SEQUENCE; Schema: public; Owner: homestead
--

CREATE SEQUENCE messages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE messages_id_seq OWNER TO homestead;

--
-- Name: messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: homestead
--

ALTER SEQUENCE messages_id_seq OWNED BY messages.id;


--
-- Name: user_channel; Type: TABLE; Schema: public; Owner: homestead
--

CREATE TABLE user_channel (
    user_id bigint,
    channel_id bigint
);


ALTER TABLE user_channel OWNER TO homestead;

--
-- Name: users; Type: TABLE; Schema: public; Owner: homestead
--

CREATE TABLE users (
    id integer NOT NULL,
    source_id character varying(255) NOT NULL,
    auth_source character varying(30) NOT NULL,
    login character varying(255),
    token character varying(255)
);


ALTER TABLE users OWNER TO homestead;

--
-- Name: users_id_seq1; Type: SEQUENCE; Schema: public; Owner: homestead
--

CREATE SEQUENCE users_id_seq1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users_id_seq1 OWNER TO homestead;

--
-- Name: users_id_seq1; Type: SEQUENCE OWNED BY; Schema: public; Owner: homestead
--

ALTER SEQUENCE users_id_seq1 OWNED BY users.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: homestead
--

ALTER TABLE ONLY channels ALTER COLUMN id SET DEFAULT nextval('channels_id_seq1'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: homestead
--

ALTER TABLE ONLY messages ALTER COLUMN id SET DEFAULT nextval('messages_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: homestead
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq1'::regclass);


--
-- Data for Name: channels; Type: TABLE DATA; Schema: public; Owner: homestead
--

COPY channels (id, name) FROM stdin;
\.


--
-- Name: channels_id_seq1; Type: SEQUENCE SET; Schema: public; Owner: homestead
--

SELECT pg_catalog.setval('channels_id_seq1', 9, true);


--
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: homestead
--

COPY messages (id, text, created_at, channel_id, user_id) FROM stdin;
\.


--
-- Name: messages_id_seq; Type: SEQUENCE SET; Schema: public; Owner: homestead
--

SELECT pg_catalog.setval('messages_id_seq', 1, true);


--
-- Data for Name: user_channel; Type: TABLE DATA; Schema: public; Owner: homestead
--

COPY user_channel (user_id, channel_id) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: homestead
--

COPY users (id, source_id, auth_source, login, token) FROM stdin;
\.


--
-- Name: users_id_seq1; Type: SEQUENCE SET; Schema: public; Owner: homestead
--

SELECT pg_catalog.setval('users_id_seq1', 17, true);


--
-- Name: channels_pkey1; Type: CONSTRAINT; Schema: public; Owner: homestead
--

ALTER TABLE ONLY channels
    ADD CONSTRAINT channels_pkey1 PRIMARY KEY (id);


--
-- Name: messages_pkey; Type: CONSTRAINT; Schema: public; Owner: homestead
--

ALTER TABLE ONLY messages
    ADD CONSTRAINT messages_pkey PRIMARY KEY (id);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: homestead
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: users_source_id_auth_source_pk; Type: CONSTRAINT; Schema: public; Owner: homestead
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_source_id_auth_source_pk UNIQUE (source_id, auth_source);


--
-- Name: messages_channel_id_index; Type: INDEX; Schema: public; Owner: homestead
--

CREATE INDEX messages_channel_id_index ON messages USING btree (channel_id);


--
-- Name: messages_user_id_index; Type: INDEX; Schema: public; Owner: homestead
--

CREATE INDEX messages_user_id_index ON messages USING btree (user_id);


--
-- Name: user_channel_channel_id_index; Type: INDEX; Schema: public; Owner: homestead
--

CREATE INDEX user_channel_channel_id_index ON user_channel USING btree (channel_id);


--
-- Name: user_channel_user_id_index; Type: INDEX; Schema: public; Owner: homestead
--

CREATE INDEX user_channel_user_id_index ON user_channel USING btree (user_id);


--
-- Name: users_token_uindex; Type: INDEX; Schema: public; Owner: homestead
--

CREATE UNIQUE INDEX users_token_uindex ON users USING btree (token);


--
-- Name: messages_channels_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: homestead
--

ALTER TABLE ONLY messages
    ADD CONSTRAINT messages_channels_id_fk FOREIGN KEY (channel_id) REFERENCES channels(id);


--
-- Name: messages_users_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: homestead
--

ALTER TABLE ONLY messages
    ADD CONSTRAINT messages_users_id_fk FOREIGN KEY (user_id) REFERENCES users(id);


--
-- Name: user_channel_channels_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: homestead
--

ALTER TABLE ONLY user_channel
    ADD CONSTRAINT user_channel_channels_id_fk FOREIGN KEY (channel_id) REFERENCES channels(id);


--
-- Name: user_channel_users_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: homestead
--

ALTER TABLE ONLY user_channel
    ADD CONSTRAINT user_channel_users_id_fk FOREIGN KEY (user_id) REFERENCES users(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

