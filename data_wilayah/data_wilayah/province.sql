--
-- PostgreSQL database dump
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

SET default_tablespace = '';

SET default_table_access_method = heap;



CREATE TABLE public.province (
    province_id bigint NOT NULL,
    created_by bigint NOT NULL,
    created_datetime timestamp without time zone NOT NULL,
    updated_by bigint NOT NULL,
    updated_datetime timestamp without time zone NOT NULL,
    version bigint NOT NULL,
    province_code character varying(50) NOT NULL,
    province_name character varying(255) NOT NULL
);



--
-- Data for Name: province; Type: TABLE DATA; Schema: core_schema; Owner: core-ms
--

COPY    public.province (province_id, created_by, created_datetime, updated_by, updated_datetime, version, province_code, province_name) FROM stdin;
1	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0001	DKI Jakarta
2	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0002	Jawa Barat
4	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0004	Jawa Tengah
5	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0005	Jawa Timur
6	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0006	DIY
7	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0007	Sulawesi Selatan
8	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0008	Kalimantan Timur
9	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0009	Sumatera Utara
10	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0010	Banten
11	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0011	Lampung
12	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0012	Sumatera Selatan
13	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0013	Riau
14	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0014	Kepulauan Riau
15	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0015	Bangka Belitung
16	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0016	Bengkulu
17	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0017	Kotamobagu
18	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0018	Sulawesi Utara
19	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0019	Sumatera Barat
20	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0020	Banda Aceh
21	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0021	Nusa Tenggara Barat
22	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0022	Kalimantan Tengah
23	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0023	Kalimantan Barat
24	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0024	Sulawesi Tenggara
25	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0025	Kalimantan Selatan
26	1	2022-02-23 00:23:45.122669	1	2022-02-23 00:23:45.122669	1	STA0026	Bali
\.


--
-- Name: province province_pkey; Type: CONSTRAINT; Schema: core_schema; Owner: core-ms
--

ALTER TABLE ONLY public.province
    ADD CONSTRAINT province_pkey PRIMARY KEY (province_id);


--
-- PostgreSQL database dump complete
--

