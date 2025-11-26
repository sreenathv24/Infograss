USE asset_management_db;
SHOW TABLES;
SELECT * FROM asset_models WHERE model_id = 6010;
SELECT * FROM employee_roles WHERE employee_id = 1002;
SELECT model_name, JSON_EXTRACT(spec_json, '$.CPU') AS CPU FROM asset_models WHERE model_id = 6010;
