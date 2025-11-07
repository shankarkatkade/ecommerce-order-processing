-- Insert sample orders with PENDING status
INSERT INTO orders (order_number, customer_name, customer_email, status, total_amount, created_at, updated_at) VALUES
('ORD-20251107-00001', 'John Doe', 'john.doe@example.com', 'PENDING', 1899.97, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ORD-20251107-00002', 'Jane Smith', 'jane.smith@example.com', 'PENDING', 2549.98, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ORD-20251107-00003', 'Bob Johnson', 'bob.johnson@example.com', 'PROCESSING', 899.99, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample orders with PROCESSING status
INSERT INTO orders (order_number, customer_name, customer_email, status, total_amount, created_at, updated_at) VALUES
('ORD-20251107-00004', 'Alice Williams', 'alice.w@example.com', 'PROCESSING', 3299.95, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ORD-20251107-00005', 'Charlie Brown', 'charlie.b@example.com', 'PROCESSING', 459.98, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample orders with SHIPPED status
INSERT INTO orders (order_number, customer_name, customer_email, status, total_amount, created_at, updated_at) VALUES
('ORD-20251106-00001', 'David Miller', 'david.m@example.com', 'SHIPPED', 1299.99, CURRENT_TIMESTAMP - 1 DAY, CURRENT_TIMESTAMP),
('ORD-20251106-00002', 'Eva Davis', 'eva.d@example.com', 'SHIPPED', 2199.97, CURRENT_TIMESTAMP - 1 DAY, CURRENT_TIMESTAMP);

-- Insert sample orders with DELIVERED status
INSERT INTO orders (order_number, customer_name, customer_email, status, total_amount, created_at, updated_at) VALUES
('ORD-20251105-00001', 'Frank Wilson', 'frank.w@example.com', 'DELIVERED', 649.99, CURRENT_TIMESTAMP - 2 DAY, CURRENT_TIMESTAMP - 1 DAY),
('ORD-20251105-00002', 'Grace Lee', 'grace.l@example.com', 'DELIVERED', 3899.95, CURRENT_TIMESTAMP - 2 DAY, CURRENT_TIMESTAMP - 1 DAY),
('ORD-20251105-00003', 'Henry Taylor', 'henry.t@example.com', 'DELIVERED', 1549.98, CURRENT_TIMESTAMP - 2 DAY, CURRENT_TIMESTAMP - 1 DAY);

-- Insert order items for Order 1
INSERT INTO order_items (order_id, product_id, product_name, quantity, price, created_at) VALUES
(1, 101, 'Dell XPS 15 Laptop', 1, 1299.99, CURRENT_TIMESTAMP),
(1, 102, 'Logitech MX Master 3 Mouse', 2, 99.99, CURRENT_TIMESTAMP),
(1, 103, 'USB-C Hub Adapter', 1, 49.99, CURRENT_TIMESTAMP);

-- Insert order items for Order 2
INSERT INTO order_items (order_id, product_id, product_name, quantity, price, created_at) VALUES
(2, 104, 'MacBook Pro 16-inch', 1, 2499.99, CURRENT_TIMESTAMP),
(2, 105, 'Magic Mouse', 1, 79.99, CURRENT_TIMESTAMP);

-- Insert order items for Order 3
INSERT INTO order_items (order_id, product_id, product_name, quantity, price, created_at) VALUES
(3, 106, 'iPad Air', 1, 599.99, CURRENT_TIMESTAMP),
(3, 107, 'Apple Pencil', 1, 129.99, CURRENT_TIMESTAMP),
(3, 108, 'iPad Case', 1, 49.99, CURRENT_TIMESTAMP);

-- Insert order items for Order 4
INSERT INTO order_items (order_id, product_id, product_name, quantity, price, created_at) VALUES
(4, 109, 'Gaming Desktop PC', 1, 1999.99, CURRENT_TIMESTAMP),
(4, 110, 'Gaming Monitor 27-inch', 1, 399.99, CURRENT_TIMESTAMP),
(4, 111, 'Mechanical Keyboard', 1, 149.99, CURRENT_TIMESTAMP),
(4, 112, 'Gaming Mouse', 1, 79.99, CURRENT_TIMESTAMP);

-- Insert order items for Order 5
INSERT INTO order_items (order_id, product_id, product_name, quantity, price, created_at) VALUES
(5, 113, 'Wireless Headphones', 2, 199.99, CURRENT_TIMESTAMP),
(5, 114, 'Phone Case', 1, 29.99, CURRENT_TIMESTAMP);

-- Insert order items for Order 6
INSERT INTO order_items (order_id, product_id, product_name, quantity, price, created_at) VALUES
(6, 115, 'HP LaserJet Printer', 1, 299.99, CURRENT_TIMESTAMP),
(6, 116, 'Printer Paper (5 Reams)', 5, 9.99, CURRENT_TIMESTAMP);

-- Insert order items for Order 7
INSERT INTO order_items (order_id, product_id, product_name, quantity, price, created_at) VALUES
(7, 117, 'Samsung 4K TV 55-inch', 1, 899.99, CURRENT_TIMESTAMP),
(7, 118, 'HDMI Cable', 2, 19.99, CURRENT_TIMESTAMP),
(7, 119, 'TV Wall Mount', 1, 49.99, CURRENT_TIMESTAMP);

-- Insert order items for Order 8
INSERT INTO order_items (order_id, product_id, product_name, quantity, price, created_at) VALUES
(8, 120, 'Ergonomic Office Chair', 1, 399.99, CURRENT_TIMESTAMP),
(8, 121, 'Standing Desk', 1, 599.99, CURRENT_TIMESTAMP);

-- Insert order items for Order 9
INSERT INTO order_items (order_id, product_id, product_name, quantity, price, created_at) VALUES
(9, 122, 'Sony A7 III Camera', 1, 1999.99, CURRENT_TIMESTAMP),
(9, 123, '50mm Lens', 1, 799.99, CURRENT_TIMESTAMP),
(9, 124, 'Camera Bag', 1, 89.99, CURRENT_TIMESTAMP),
(9, 125, 'SD Card 128GB', 2, 49.99, CURRENT_TIMESTAMP);

-- Insert order items for Order 10
INSERT INTO order_items (order_id, product_id, product_name, quantity, price, created_at) VALUES
(10, 126, 'External SSD 1TB', 2, 149.99, CURRENT_TIMESTAMP),
(10, 127, 'USB Flash Drive 64GB', 3, 19.99, CURRENT_TIMESTAMP);