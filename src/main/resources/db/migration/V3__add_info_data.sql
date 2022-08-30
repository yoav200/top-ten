
-- Add colleges to info
ALTER TABLE IF EXISTS public.player_info
    ADD COLUMN colleges character varying(255);

-- fix typo
ALTER TABLE IF EXISTS public.player_info
        RENAME wight TO weight;