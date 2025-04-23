CREATE TYPE product_category AS ENUM (
    'electronics',
    'clothing',
    'home_appliances',
    'books',
    'toys',
    'misc'
);

CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name  VARCHAR(100) NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    inventory INTEGER NOT NULL,
    category product_category NOT NULL DEFAULT 'misc',
    description TEXT,
    images TEXT[],
    is_deleted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE 'plpgsql';

CREATE TRIGGER update_product_timestamp
    BEFORE UPDATE
    ON products
    FOR EACH ROW
EXECUTE PROCEDURE update_timestamp();

INSERT INTO products (name, price, inventory, category, description, images)
VALUES
    ('Product 1', 19.99, 100, 'electronics', 'Description for product 1', NULL),
    ('Product 2', 29.99, 200, 'clothing', 'Description for product 2', NULL),
    ('Product 3', 39.99, 300, 'home_appliances', 'Description for product 3', NULL),
    ('Product 4', 49.99, 400, 'books', 'Description for product 4', NULL),
    ('Product 5', 59.99, 500, 'toys', 'Description for product 5', NULL),
    ('Product 6', 69.99, 500, 'misc', 'Description for product 6', NULL);
