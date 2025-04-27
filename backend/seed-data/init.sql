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

-- For full-text search
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX idx_products_name_trgm
ON products USING gin (name gin_trgm_ops);

CREATE INDEX idx_products_description_trgm
ON products USING gin (description gin_trgm_ops);

CREATE OR REPLACE FUNCTION category_to_text(product_category) 
RETURNS text AS $$
    SELECT $1::text;
$$ LANGUAGE SQL IMMUTABLE;

CREATE INDEX idx_products_category_trgm
ON products USING gin (category_to_text(category) gin_trgm_ops);

-- Insert electronics products
INSERT INTO products (name, price, inventory, category, description, images)
VALUES
    ('Samsung Galaxy S25 Ultra', 1299.99, 85, 'electronics', 'Flagship smartphone with 8K video recording, 200MP camera, and AI-enhanced features for photography and productivity. Comes with 1TB storage and 16GB RAM.', NULL),
    ('iPhone 17 Pro', 1199.99, 120, 'electronics', 'Latest Apple smartphone featuring improved battery life, enhanced cameras, and faster processing. Available in titanium finish with spatial computing capabilities.', NULL),
    ('Dell XPS 15 Laptop', 1799.99, 45, 'electronics', 'Powerful laptop with 13th Gen Intel i9 processor, 32GB RAM, 1TB SSD, and NVIDIA RTX 4070 graphics. Perfect for content creators and professionals.', NULL),
    ('Sony WH-1000XM6 Headphones', 349.99, 200, 'electronics', 'Premium wireless noise-cancelling headphones with 40-hour battery life and hi-res audio support. Features adaptive sound control and speak-to-chat technology.', NULL),
    ('Nintendo Switch Pro', 399.99, 85, 'electronics', 'Next-generation gaming console with 4K output capabilities, enhanced Joy-Cons, and expanded storage. Compatible with all Nintendo Switch games.', NULL),
    ('Dyson Airwrap Complete', 599.99, 30, 'electronics', 'Versatile styling tool that curls, waves, smooths and dries with no extreme heat. Includes multiple attachments for different hair types and styles.', NULL),
    ('Apple MacBook Air M3', 1099.99, 75, 'electronics', 'Ultra-thin laptop powered by Apple M3 chip with 16GB unified memory and 512GB SSD. Features all-day battery life and stunning Retina display.', NULL),
    ('Bose Smart Soundbar 900', 799.99, 50, 'electronics', 'Premium soundbar with Dolby Atmos, voice assistants, and Bluetooth connectivity. Features proprietary audio technologies for immersive sound.', NULL);

-- Insert clothing products
INSERT INTO products (name, price, inventory, category, description, images)
VALUES
    ('Nike Air Max 270', 150.00, 200, 'clothing', 'Iconic running shoes with visible Air unit in heel for all-day comfort. Breathable mesh upper and versatile style for casual wear.', NULL),
    ('Patagonia Better Sweater', 139.00, 150, 'clothing', 'Warm fleece jacket made with 100% recycled polyester. Features a stand-up collar and zippered pockets. Perfect for outdoor activities or everyday wear.', NULL),
    ('Levi''s 501 Original Fit Jeans', 69.50, 300, 'clothing', 'Classic straight leg jeans with button fly and signature Levi''s leather patch. Made with sustainable cotton and built to last.', NULL),
    ('The North Face Thermoball Eco Jacket', 199.00, 85, 'clothing', 'Lightweight insulated jacket with synthetic fill that retains warmth even when wet. Packable design with water-resistant finish.', NULL),
    ('Adidas Ultraboost 25 Running Shoes', 180.00, 120, 'clothing', 'High-performance running shoes featuring responsive Boost midsole and Primeknit+ upper. Adaptive support and energy return for runners of all levels.', NULL),
    ('Ray-Ban Wayfarer Sunglasses', 163.00, 75, 'clothing', 'Iconic sunglass design with polarized lenses and durable acetate frame. Offers 100% UV protection with timeless style.', NULL),
    ('Columbia Bugaboo Ski Pants', 110.00, 95, 'clothing', 'Waterproof and breathable ski pants with Omni-Heat thermal reflective lining. Adjustable waist and reinforced cuffs for durability on the slopes.', NULL),
    ('Uniqlo Ultra Light Down Jacket', 79.90, 250, 'clothing', 'Lightweight, packable down jacket that provides warmth without bulk. Water-repellent coating and comes with storage pouch for travel.', NULL);

-- Insert home appliances products
INSERT INTO products (name, price, inventory, category, description, images)
VALUES
    ('Instant Pot Duo Plus 9-in-1', 129.95, 150, 'home_appliances', 'Multi-functional pressure cooker that works as slow cooker, rice cooker, steamer, sauté pan, yogurt maker, warmer, and sterilizer. 6-quart capacity ideal for families.', NULL),
    ('Dyson V12 Detect Slim Cordless Vacuum', 649.99, 60, 'home_appliances', 'Lightweight cordless vacuum with laser dust detection and LCD screen showing particle count. Includes multiple attachments for whole-home cleaning.', NULL),
    ('Ninja Foodi 10-in-1 Air Fryer', 219.99, 100, 'home_appliances', 'Versatile kitchen appliance that air fries, roasts, bakes, broils, dehydrates, and more. Features smart cook system with thermometer for perfect results.', NULL),
    ('Samsung Smart Refrigerator with Family Hub', 2799.99, 25, 'home_appliances', 'Premium French door refrigerator with touchscreen interface, internal cameras, and smart home integration. Features include food management system and entertainment options.', NULL),
    ('KitchenAid Stand Mixer Professional 5 Plus', 429.99, 80, 'home_appliances', 'Powerful stand mixer with 5-quart stainless steel bowl and 10 speed settings. Includes dough hook, flat beater, and wire whip attachments.', NULL),
    ('Breville Smart Oven Air Fryer Pro', 399.95, 70, 'home_appliances', '13-in-1 countertop oven with convection, air frying, dehydrating, and roasting functions. Features Element IQ for precise cooking and large capacity for family meals.', NULL),
    ('Miele Complete C3 Canister Vacuum', 699.00, 40, 'home_appliances', 'High-end vacuum cleaner with HEPA filtration and quiet operation. Features foot controls, automatic cord rewind, and adjustable suction power.', NULL),
    ('LG Front Load Washer with TurboWash', 899.99, 30, 'home_appliances', 'Energy-efficient washing machine with steam technology and AI fabric sensing. Features include smart connectivity and allergen removal cycle.', NULL);

-- Insert books products
INSERT INTO products (name, price, inventory, category, description, images)
VALUES
    ('The Quantum Paradigm', 24.99, 150, 'books', 'Bestselling science book exploring the latest breakthroughs in quantum physics and their implications for our understanding of reality. Written by renowned physicist Dr. Sarah Chen.', NULL),
    ('Lost in the Echoes', 18.99, 200, 'books', 'Award-winning novel about a family spanning three generations dealing with secrets and reconciliation. Winner of the 2024 National Book Award for Fiction.', NULL),
    ('Culinary Journeys: Global Recipes and Stories', 35.00, 85, 'books', 'Beautifully illustrated cookbook featuring authentic recipes from 50 countries along with cultural context and personal stories from local chefs.', NULL),
    ('The Business of Tomorrow', 29.99, 120, 'books', 'Essential guide to emerging technologies and business models reshaping the global economy. Includes case studies and actionable strategies for entrepreneurs.', NULL),
    ('Mindful Leadership: Leading with Presence', 22.95, 100, 'books', 'Practical handbook for developing leadership skills through mindfulness practices. Features exercises, reflections, and real-world applications.', NULL),
    ('The Hidden History of Ancient Civilizations', 27.50, 75, 'books', 'Fascinating archaeological exploration of forgotten societies and their advanced technologies. Challenges conventional historical narratives with new evidence.', NULL),
    ('Coding for Kids: Python Adventures', 19.99, 150, 'books', 'Interactive programming book teaching children ages 10-14 the basics of Python through fun projects and games. Includes online resources and challenges.', NULL),
    ('The Garden Year: Month-by-Month Guide', 34.95, 60, 'books', 'Comprehensive gardening reference with seasonal tasks, plant recommendations, and sustainable practices. Features beautiful photography and expert advice for all climate zones.', NULL);

-- Insert toys products
INSERT INTO products (name, price, inventory, category, description, images)
VALUES
    ('LEGO Star Wars Ultimate Millennium Falcon', 849.99, 30, 'toys', 'Collector''s edition LEGO set with over 7,500 pieces. Features detailed interior, functioning landing gear, and minifigures from both the original and sequel trilogies.', NULL),
    ('Barbie Dreamhouse 2025 Edition', 199.99, 75, 'toys', 'Three-story smart dollhouse with interactive features, elevator, pool, and customizable lighting. Compatible with Barbie app for extended play experiences.', NULL),
    ('Nintendo Amiibo Collection Set', 139.99, 50, 'toys', 'Limited edition set featuring 10 classic Nintendo character figurines. Compatible with multiple Nintendo games for unlocking special content.', NULL),
    ('Monopoly Ultimate Banking Edition', 39.99, 200, 'toys', 'Modern version of the classic board game with electronic banking unit and instant property transactions. Features updated properties and game mechanics.', NULL),
    ('Nerf Ultra Pro Blaster', 59.99, 150, 'toys', 'High-performance dart blaster with motorized firing mechanism and 25-dart rotating drum. Includes tactical rail for accessories and improved range up to 120 feet.', NULL),
    ('Melissa & Doug Wooden Kitchen Set', 249.99, 60, 'toys', 'Premium wooden play kitchen with realistic features including ice maker, oven with clicking knobs, and microwave with turning plate. Includes 25-piece accessory set.', NULL),
    ('Magic: The Gathering Collector Booster Box', 249.99, 40, 'toys', 'Limited edition set with 12 premium booster packs containing rare and foil cards. Includes exclusive art treatments and potential chase cards.', NULL),
    ('Hot Wheels Ultimate Garage', 99.99, 80, 'toys', 'Massive playset featuring spiral ramps, parking levels, and car wash. Includes 4 die-cast cars and can hold over 100 vehicles in the collection.', NULL);

-- Insert misc products
INSERT INTO products (name, price, inventory, category, description, images)
VALUES
    ('Hydro Flask 32oz Water Bottle', 44.95, 300, 'misc', 'Vacuum-insulated stainless steel water bottle that keeps beverages cold for 24 hours or hot for 12 hours. Features durable powder coating and leak-proof cap.', NULL),
    ('Moleskine Classic Notebook', 19.95, 250, 'misc', 'Premium hard-cover notebook with acid-free paper, bookmark ribbon, and elastic closure. Perfect for journaling, sketching, and note-taking.', NULL),
    ('Himalayan Salt Lamp', 34.99, 120, 'misc', 'Natural salt crystal lamp with wooden base and dimmer switch. Emits a warm, amber glow and may help purify air through ionization.', NULL),
    ('Bonsai Tree Starter Kit', 49.99, 80, 'misc', 'Complete kit for growing 4 types of bonsai trees from seed. Includes biodegradable pots, soil discs, bamboo plant markers, and comprehensive guide.', NULL),
    ('Premium Yoga Mat', 78.99, 150, 'misc', 'Eco-friendly yoga mat made from natural rubber with excellent grip and cushioning. Features alignment markers and comes with carrying strap.', NULL),
    ('Smart Digital Picture Frame', 159.99, 60, 'misc', '10-inch HD display that connects to WiFi for sharing photos remotely. Features motion sensor, touchscreen controls, and unlimited cloud storage.', NULL),
    ('Gourmet Coffee Sampler', 45.00, 100, 'misc', 'Collection of single-origin coffee beans from 6 countries. Freshly roasted and packaged in resealable bags with tasting notes and brewing recommendations.', NULL),
    ('Wireless Charging Pad', 39.99, 200, 'misc', 'Fast-charging Qi-compatible pad that works with most modern smartphones. Features LED indicator and foreign object detection for safety.', NULL);
