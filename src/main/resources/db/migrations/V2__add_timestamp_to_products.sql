ALTER TABLE
    public.products
ADD
    COLUMN create_at timestamp,
ADD
    COLUMN update_at timestamp;

UPDATE
    public.products
SET
    create_at = NOW(),
    update_at = NOW()
WHERE
    create_at IS NULL;

ALTER TABLE
    public.products
ALTER COLUMN
    create_at
SET
    DEFAULT NOW(),
ALTER COLUMN
    update_at
SET
    DEFAULT NOW();