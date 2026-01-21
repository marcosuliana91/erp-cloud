-- Create products table
CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY,
    code VARCHAR(60) NOT NULL UNIQUE,
    integration_code VARCHAR(60),
    description VARCHAR(200) NOT NULL,
    detailed_description TEXT,
    unit VARCHAR(6) NOT NULL,
    ncm_code VARCHAR(10) NOT NULL,
    ean_code VARCHAR(14),
    type VARCHAR(20) NOT NULL DEFAULT 'PRODUCT',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    brand VARCHAR(100),
    model VARCHAR(100),
    family VARCHAR(100),
    unit_price DECIMAL(19,4) DEFAULT 0,
    cost_price DECIMAL(19,4) DEFAULT 0,
    gross_weight DECIMAL(15,4) DEFAULT 0,
    net_weight DECIMAL(15,4) DEFAULT 0,
    height DECIMAL(10,2) DEFAULT 0,
    width DECIMAL(10,2) DEFAULT 0,
    depth DECIMAL(10,2) DEFAULT 0,
    internal_notes TEXT,
    stock_quantity INTEGER DEFAULT 0,
    minimum_stock INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_products_code ON products(code);
CREATE INDEX IF NOT EXISTS idx_products_integration_code ON products(integration_code);
CREATE INDEX IF NOT EXISTS idx_products_description ON products(description);
CREATE INDEX IF NOT EXISTS idx_products_family ON products(family);
CREATE INDEX IF NOT EXISTS idx_products_brand ON products(brand);
CREATE INDEX IF NOT EXISTS idx_products_status ON products(status);
CREATE INDEX IF NOT EXISTS idx_products_ncm_code ON products(ncm_code);
CREATE INDEX IF NOT EXISTS idx_products_type ON products(type);

-- Add check constraints
ALTER TABLE products ADD CONSTRAINT IF NOT EXISTS chk_products_type
    CHECK (type IN ('PRODUCT', 'SERVICE', 'RAW_MATERIAL', 'CONSUMABLE', 'ASSET'));

ALTER TABLE products ADD CONSTRAINT IF NOT EXISTS chk_products_status
    CHECK (status IN ('ACTIVE', 'INACTIVE', 'DISCONTINUED'));

ALTER TABLE products ADD CONSTRAINT IF NOT EXISTS chk_products_unit_price_non_negative
    CHECK (unit_price >= 0);

ALTER TABLE products ADD CONSTRAINT IF NOT EXISTS chk_products_cost_price_non_negative
    CHECK (cost_price >= 0);

ALTER TABLE products ADD CONSTRAINT IF NOT EXISTS chk_products_stock_non_negative
    CHECK (stock_quantity >= 0);

ALTER TABLE products ADD CONSTRAINT IF NOT EXISTS chk_products_min_stock_non_negative
    CHECK (minimum_stock >= 0);
