-- Add player reference for NBA stats
ALTER TABLE IF EXISTS public.player
    ADD COLUMN player_reference bigint;