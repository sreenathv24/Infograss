

-- low complexity
USE asset_management_db;
-- 1.Add Asset Model → Insert an asset model with manufacturer and specs JSO
INSERT INTO asset_models (model_id, org_id, category_id, model_code, manufacturer, model_name, spec_json, created_at, updated_at)
VALUES (
    6010,                                -- new model_id
    1,                                   -- org_id (Acme)
    5001,                                -- category_id (Laptop)
    'LAT5430',                           -- model_code
    'Dell',                              -- manufacturer
    'Dell Latitude 5430',                -- model_name
    JSON_OBJECT('CPU','Intel i5','RAM','8GB','Storage','256GB SSD','OS','Windows 11'),
    NOW(), NOW()
);
-- 2.Assign Role → Link employee to a role in user_roles.
INSERT INTO employee_roles (employee_id, role_id, assigned_at)
VALUES
(1002, 201, NOW());   -- Jane Smith → IT_ADMIN role

-- 3.Update Asset Status → Change asset status to 'ASSIGNED'
UPDATE assets
SET current_status = 'ASSIGNED', updated_at = NOW()
WHERE asset_id = 7001;

---------------------
-- medium complexity
USE asset_management_db;
-- 1.License Allocation → Allocate license seats to employees, decrement available seats
-- Decrement seats_in_use for the license
UPDATE software_licenses
SET seats_in_use = seats_in_use + 1
WHERE license_id = 17001
  AND seats_in_use < seats_purchased;
  
  -- Update received quantity (simulate receiving all items)
UPDATE purchase_order_items
SET qty_received = qty_ordered
WHERE po_id = 15001;

-- 2.Close PO → Update PO status to CLOSED after receipt
-- Close the PO if all items are received
UPDATE purchase_orders p
SET status = 'CLOSED'
WHERE p.po_id = 15001
  AND NOT EXISTS (
    SELECT 1
    FROM purchase_order_items i
    WHERE i.po_id = p.po_id
      AND i.qty_received < i.qty_ordered
  );
  
  -- 3.Work Order Aging → List work orders pending > 30 days
  SELECT 
    wo.wo_id,
    wo.title,
    wo.status,
    wo.priority,
    wo.requested_at,
    DATEDIFF(NOW(), wo.requested_at) AS days_open,
    e.full_name AS requested_by_name
FROM work_orders wo
JOIN employees e ON e.employee_id = wo.requested_by
WHERE wo.status = 'OPEN'
  AND wo.requested_at BETWEEN '2025-08-01' AND '2025-08-10';

-- from date to date (date rage)august 1 and aug 10
---------------------
-- high complexity
USE asset_management_db;

ALTER TABLE assets 
MODIFY COLUMN model_id BIGINT NULL;


-- =====================================================
-- 1) Asset Utilization View
-- =====================================================
-- 1.Asset Utilization View → View showing assets per department and employee usage.

CREATE OR REPLACE VIEW v_asset_utilization AS
SELECT 
    aa.assignment_id,
    a.asset_tag,
    a.serial_number,
    a.current_status,
    e.employee_id,
    e.full_name AS employee_name,
    d.dept_id,
    d.dept_name,
    aa.assigned_at,
    aa.unassigned_at
FROM asset_assignments aa
JOIN assets a ON a.asset_id = aa.asset_id
LEFT JOIN employees e ON e.employee_id = aa.employee_id
LEFT JOIN departments d ON d.dept_id = e.dept_id;

-- Insert unique departments
INSERT INTO departments (org_id, dept_code, dept_name, parent_dept_id)
VALUES 
  (1, 'HR01', 'Human Resources', NULL),
  (1, 'OPS01', 'Operations', NULL),
  (1, 'MKT01', 'Marketing', NULL),
  (1, 'SALES01', 'Sales', NULL),
  (1, 'QA01', 'Quality Assurance', NULL),
  (1, 'RND01', 'Research & Development', NULL)
ON DUPLICATE KEY UPDATE dept_id = dept_id;

-- get dept ids
SET @dept_hr = (SELECT dept_id FROM departments WHERE org_id=1 AND dept_code='HR01');
SET @dept_ops = (SELECT dept_id FROM departments WHERE org_id=1 AND dept_code='OPS01');

-- Insert employees safely
INSERT INTO employees (org_id, dept_id, employee_code, full_name, email, status)
VALUES 
  (1, @dept_hr, 'EMP001', 'John Doe', 'john.doe@test.com','ACTIVE'),
  (1, @dept_ops,'EMP002', 'Jane Smith','jane.smith@test.com','ACTIVE')
ON DUPLICATE KEY UPDATE employee_id=employee_id;

SET @emp_jd = (SELECT employee_id FROM employees WHERE employee_code='EMP001' AND org_id=1);
SET @emp_js = (SELECT employee_id FROM employees WHERE employee_code='EMP002' AND org_id=1);

-- Insert asset safely
INSERT INTO assets (org_id, model_id, supplier_id, serial_number, asset_tag, current_status)
VALUES 
  (1, NULL, NULL, 'SN12345', 'LAPTOP-001', 'IN_STOCK')
ON DUPLICATE KEY UPDATE asset_id=asset_id;

SET @asset1 = (SELECT asset_id FROM assets WHERE asset_tag='LAPTOP-001');

-- Assign asset
INSERT INTO asset_assignments (asset_id, employee_id, assigned_at)
VALUES (@asset1, @emp_jd, NOW());

-- Run the view
SELECT * FROM v_asset_utilization;


-- =====================================================
-- 2) Spare Capacity Planning  Report bins with capacity > 80% used
-- =====================================================
INSERT INTO storage_bins (location_id, bin_code, capacity_units, type)
VALUES (1, 'BIN-A1', 100, 'GENERAL'),
       (1, 'BIN-B1', 50,  'GENERAL')
ON DUPLICATE KEY UPDATE bin_id = bin_id;

SET @bin_a = (SELECT bin_id FROM storage_bins WHERE bin_code='BIN-A1');
SET @bin_b = (SELECT bin_id FROM storage_bins WHERE bin_code='BIN-B1');

-- inventory transactions
INSERT INTO inventory_txns (org_id, bin_id_to, txn_type, qty, reference)
VALUES 
  (1, @bin_a, 'RECEIPT', 85, 'TXN-BIN-A'),
  (1, @bin_b, 'RECEIPT', 20, 'TXN-BIN-B');

-- Spare Capacity Query
SELECT 
    sb.bin_id,
    sb.bin_code,
    sb.capacity_units,
    IFNULL(SUM(it.qty),0) AS used_units,
    ROUND(IFNULL(SUM(it.qty)/NULLIF(sb.capacity_units,0)*100,0),2) AS used_pct
FROM storage_bins sb
LEFT JOIN inventory_txns it ON sb.bin_id = it.bin_id_to
GROUP BY sb.bin_id, sb.bin_code, sb.capacity_units
HAVING used_pct >= 80;


-- =====================================================
-- 3) Multi-Level Audit Procedure → Stored procedure logging user + role + action + before/after snapshot.
-- =====================================================
DROP PROCEDURE IF EXISTS sp_audit_log;

DELIMITER $$
CREATE PROCEDURE sp_audit_log(
  IN p_user_id BIGINT,
  IN p_action VARCHAR(100),
  IN p_table_name VARCHAR(100),
  IN p_record_id BIGINT,
  IN p_before_json JSON,
  IN p_after_json JSON
)
BEGIN
  DECLARE v_role_id INT;
  SELECT role_id INTO v_role_id
  FROM employee_roles
  WHERE employee_id = p_user_id
  ORDER BY assigned_at LIMIT 1;

  INSERT INTO audit_logs (
    user_id, role_id, action, table_name, record_id,
    before_snapshot, after_snapshot, action_time
  )
  VALUES (
    p_user_id, v_role_id, p_action, p_table_name, p_record_id,
    p_before_json, p_after_json, NOW()
  );
END $$
DELIMITER ;

-- Create audit_logs table
DROP TABLE IF EXISTS audit_logs;
CREATE TABLE audit_logs (
  log_id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  role_id INT UNSIGNED NULL,
  action VARCHAR(100) NOT NULL,
  table_name VARCHAR(100) NOT NULL,
  record_id BIGINT NOT NULL,
  before_snapshot JSON,
  after_snapshot JSON,
  action_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_audit_user FOREIGN KEY (user_id) REFERENCES employees(employee_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Ensure a role exists
INSERT INTO roles (org_id, role_code, role_name)
VALUES (1, 'IT_ADMIN','IT Administrator')
ON DUPLICATE KEY UPDATE role_id=role_id;

SET @role_it = (SELECT role_id FROM roles WHERE role_code='IT_ADMIN' AND org_id=1);

-- Map role to employee
INSERT IGNORE INTO employee_roles (role_id, employee_id, assigned_at)
VALUES (@role_it, @emp_jd, NOW());

-- Call procedure
CALL sp_audit_log(
  @emp_jd,
  'UPDATE',
  'assets',
  @asset1,
  JSON_OBJECT('current_status','IN_STOCK'),
  JSON_OBJECT('current_status','ASSIGNED')
);

-- Verify
SELECT * FROM audit_logs;

