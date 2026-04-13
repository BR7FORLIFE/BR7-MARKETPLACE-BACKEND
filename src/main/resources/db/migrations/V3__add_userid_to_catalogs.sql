ALTER TABLE catalogs ADD COLUMN user_id UUID;

ALTER TABLE
    catalogs
ADD
    CONSTRAINT fk_catalog_user FOREIGN KEY (user_id) REFERENCES users(user_id);