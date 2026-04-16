ALTER TABLE
    catalogs
ADD
    CONSTRAINT unique_code UNIQUE(code);

ALTER TABLE
    catalogs
ADD
    CONSTRAINT unique_slug UNIQUE(slug);