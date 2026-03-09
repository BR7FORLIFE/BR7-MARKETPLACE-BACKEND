-- =========================
-- EXTENSIONES
-- =========================
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- =========================
-- TABLAS BASE
-- =========================

CREATE TABLE IF NOT EXISTS public.users (
    user_id uuid NOT NULL,
    username varchar(30) NOT NULL,
    email varchar(30) NOT NULL,
    password_hash text NOT NULL,
    auth_status text NOT NULL DEFAULT 'PENDING',
    CONSTRAINT users_pkey PRIMARY KEY (user_id),
    CONSTRAINT user_email_unique UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS public.permissions (
    permission_id uuid NOT NULL,
    permission_name varchar(50) NOT NULL,
    CONSTRAINT permissions_pkey PRIMARY KEY (permission_id)
);

CREATE TABLE IF NOT EXISTS public.rols (
    rol_id uuid NOT NULL,
    rol varchar(20) NOT NULL,
    CONSTRAINT rols_pkey PRIMARY KEY (rol_id)
);

CREATE TABLE IF NOT EXISTS public.catalogs (
    catalog_id uuid NOT NULL DEFAULT gen_random_uuid(),
    code text NOT NULL,
    slug text NOT NULL,
    name_catalog varchar(30) NOT NULL,
    type_catalog varchar(10) NOT NULL,
    status varchar(10) NOT NULL,
    visible boolean DEFAULT true,
    CONSTRAINT catalogs_pkey PRIMARY KEY (catalog_id)
);

-- =========================
-- TABLAS DEPENDIENTES
-- =========================

CREATE TABLE IF NOT EXISTS public.products (
    product_id uuid NOT NULL,
    name_product varchar(20) NOT NULL,
    short_description text NOT NULL,
    long_description text,
    model varchar(30) NOT NULL,
    sku text NOT NULL,
    user_id uuid NOT NULL,
    CONSTRAINT products_pkey PRIMARY KEY (product_id),
    CONSTRAINT products_sku_unique_key UNIQUE (user_id, sku),
    CONSTRAINT fk_products_user
        FOREIGN KEY (user_id)
        REFERENCES public.users (user_id)
);

CREATE TABLE IF NOT EXISTS public.listings (
    listing_id uuid NOT NULL,
    product_id uuid NOT NULL,
    price numeric(10,2),
    currency varchar(3) NOT NULL DEFAULT 'USD',
    is_active boolean DEFAULT true,
    create_at timestamp DEFAULT now(),
    update_at timestamp DEFAULT now(),
    status_review varchar(10) NOT NULL,
    status_publication varchar(10) NOT NULL,
    CONSTRAINT listings_pkey PRIMARY KEY (listing_id),
    CONSTRAINT fk_listings_products
        FOREIGN KEY (product_id)
        REFERENCES public.products (product_id)
);

CREATE TABLE IF NOT EXISTS public.orders (
    order_id uuid NOT NULL DEFAULT gen_random_uuid(),
    user_id uuid NOT NULL,
    order_date timestamp DEFAULT now(),
    status text DEFAULT 'pending',
    total_amount numeric(10,2) NOT NULL,
    CONSTRAINT orders_pkey PRIMARY KEY (order_id),
    CONSTRAINT fk_users_orders
        FOREIGN KEY (user_id)
        REFERENCES public.users (user_id)
);

CREATE TABLE IF NOT EXISTS public.payments (
    payment_id uuid NOT NULL DEFAULT gen_random_uuid(),
    order_id uuid NOT NULL,
    amount numeric(10,2) NOT NULL,
    currency text DEFAULT 'USD',
    method_pay text,
    status text DEFAULT 'pending',
    paid_at timestamp DEFAULT now(),
    CONSTRAINT payments_pkey PRIMARY KEY (payment_id),
    CONSTRAINT fk_payments_orders
        FOREIGN KEY (order_id)
        REFERENCES public.orders (order_id)
);

CREATE TABLE IF NOT EXISTS public.order_items (
    order_item_id uuid NOT NULL DEFAULT gen_random_uuid(),
    order_id uuid NOT NULL,
    listing_id uuid NOT NULL,
    quantity integer NOT NULL,
    unit_price numeric(10,2) NOT NULL,
    CONSTRAINT order_items_pkey PRIMARY KEY (order_item_id),
    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id)
        REFERENCES public.orders (order_id),
    CONSTRAINT fk_order_items_listing
        FOREIGN KEY (listing_id)
        REFERENCES public.listings (listing_id)
);

-- =========================
-- TABLAS RELACIONALES
-- =========================

CREATE TABLE IF NOT EXISTS public.users_rols (
    user_rol_id serial,
    user_id uuid NOT NULL,
    rol_id uuid NOT NULL,
    CONSTRAINT users_rols_pkey PRIMARY KEY (user_id, rol_id),
    CONSTRAINT fk_user_rols
        FOREIGN KEY (user_id)
        REFERENCES public.users (user_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_rol_users
        FOREIGN KEY (rol_id)
        REFERENCES public.rols (rol_id)
);

CREATE TABLE IF NOT EXISTS public.rol_permissions (
    rol_permission_id serial,
    rol_id uuid NOT NULL,
    permission_id uuid NOT NULL,
    CONSTRAINT rol_permissions_pkey PRIMARY KEY (rol_id, permission_id),
    CONSTRAINT fk_rol_rols
        FOREIGN KEY (rol_id)
        REFERENCES public.rols (rol_id)
        ON DELETE CASCADE,
    CONSTRAINT fk_permission_permissions
        FOREIGN KEY (permission_id)
        REFERENCES public.permissions (permission_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.catalog_items (
    catalog_id uuid NOT NULL,
    product_id uuid NOT NULL,
    custom_label text,
    create_at timestamp DEFAULT now(),
    CONSTRAINT catalog_items_pk PRIMARY KEY (catalog_id, product_id),
    CONSTRAINT fk_catalog_items_catalog
        FOREIGN KEY (catalog_id)
        REFERENCES public.catalogs (catalog_id),
    CONSTRAINT fk_catalog_items_product
        FOREIGN KEY (product_id)
        REFERENCES public.products (product_id)
);

-- =========================
-- TOKENS
-- =========================

CREATE TABLE IF NOT EXISTS public.refresh_token (
    refresh_token_id uuid NOT NULL,
    user_id uuid NOT NULL,
    token_hash text NOT NULL,
    expired_at timestamp NOT NULL,
    create_at timestamp NOT NULL,
    revoked boolean NOT NULL,
    CONSTRAINT refresh_token_pkey PRIMARY KEY (refresh_token_id),
    CONSTRAINT refresh_token_hash_key UNIQUE (token_hash),
    CONSTRAINT fk_user_refreshtoken
        FOREIGN KEY (user_id)
        REFERENCES public.users (user_id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.email_verification_token (
    email_verification_token_id uuid NOT NULL,
    user_id uuid NOT NULL,
    token_hash text NOT NULL,
    create_at timestamp NOT NULL DEFAULT now(),
    consumed_at timestamp,
    expired_at timestamp,
    CONSTRAINT email_verification_token_pkey PRIMARY KEY (email_verification_token_id),
    CONSTRAINT fk_user_email_verification_token
        FOREIGN KEY (user_id)
        REFERENCES public.users (user_id)
        ON DELETE CASCADE
);

-- =========================
-- MEDIA
-- =========================

CREATE TABLE IF NOT EXISTS public.asset_media (
    id uuid NOT NULL DEFAULT gen_random_uuid(),
    owner_id uuid NOT NULL,
    owner_type varchar(30) NOT NULL,
    cloud_public_id varchar(255) NOT NULL,
    format varchar(10) NOT NULL,
    resource_type varchar(20) NOT NULL,
    storage_type varchar(20) NOT NULL,
    bytes bigint NOT NULL,
    width integer,
    height integer,
    url text NOT NULL,
    secure_url text NOT NULL,
    "position" integer DEFAULT 0,
    is_cover boolean NOT NULL DEFAULT false,
    created_at timestamp NOT NULL DEFAULT now(),
    deleted_at timestamp,
    CONSTRAINT asset_media_pkey PRIMARY KEY (id)
);
