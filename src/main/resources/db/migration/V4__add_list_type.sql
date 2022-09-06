
-- Add list_type to rank_list
ALTER TABLE IF EXISTS public.rank_list
    ADD COLUMN list_type character varying(255) NOT NULL DEFAULT 'ALL_TIME_GREATEST';