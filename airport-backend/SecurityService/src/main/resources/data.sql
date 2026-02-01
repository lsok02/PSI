-- ============================================
-- POPRAWIONE INSERTY DLA ENUMÓW
-- ZGODNIE Z STRUKTURĄ WYGENEROWANĄ PRZEZ HIBERNATE
-- ============================================
CREATE SEQUENCE IF NOT EXISTS incidents_seq;
CREATE SEQUENCE IF NOT EXISTS log_entries_seq;
CREATE SEQUENCE IF NOT EXISTS audit_logs_seq;

-- 1. Location
INSERT INTO location (id, name, type, coordinates) VALUES
                                                       (1, 'Terminal A - Security Checkpoint 1', 'SECURITY_CHECKPOINT', '51.4775, 0.4614'),
                                                       (2, 'Runway 09R/27L', 'RUNWAY', '51.4780, 0.4620'),
                                                       (3, 'Cargo Warehouse 4', 'CARGO_AREA', '51.4790, 0.4630'),
                                                       (4, 'Main Control Tower', 'CONTROL_TOWER', '51.4765, 0.4605'),
                                                       (5, 'Parking Lot P3 - Level 2', 'PARKING', '51.4750, 0.4590'),
                                                       (6, 'Fuel Storage Area', 'FUEL_STORAGE', '51.4800, 0.4640');

-- 2. Employee
INSERT INTO employee (id, first_name, last_name, service_number) VALUES
                                                                     (1, 'John', 'Smith', 'SEC-001'),
                                                                     (2, 'Emma', 'Johnson', 'SEC-002'),
                                                                     (3, 'Michael', 'Brown', 'SEC-003'),
                                                                     (4, 'Sarah', 'Davis', 'SEC-004'),
                                                                     (5, 'Robert', 'Wilson', 'SEC-005'),
                                                                     (6, 'Jennifer', 'Miller', 'SEC-006'),
                                                                     (7, 'David', 'Taylor', 'SEC-007'),
                                                                     (8, 'Lisa', 'Anderson', 'SEC-008');

-- 3. Dispatcher
INSERT INTO dispatcher (id) VALUES (1), (2);

-- 4. SecurityManager
INSERT INTO security_manager (id) VALUES (3), (4);

-- 5. IncidentTeam
INSERT INTO incident_team (id, team_name, specialization, status) VALUES
                                                                      (1, 'Alpha Team', 'FIRE', 'AVAILABLE'),
                                                                      (2, 'Bravo Team', 'MEDICAL_RESPONSE', 'BUSY'),
                                                                      (3, 'Charlie Team', 'SECURITY_THREAT', 'UNAVAILABLE'),
                                                                      (4, 'Delta Team', 'TECHNICAL_SUPPORT', 'AVAILABLE'),
                                                                      (5, 'Echo Team', 'K9_UNIT', 'BUSY');

-- 6. IncidentTeamMember
INSERT INTO incident_team_member (id, radio_id, team_id) VALUES
                                                             (5, 'RADIO-501', 1),
                                                             (6, 'RADIO-502', 1),
                                                             (7, 'RADIO-601', 2),
                                                             (8, 'RADIO-602', 2);

-- 7. StandardOperatingProcedure
INSERT INTO standard_operating_procedure (id, procedure_name, description) VALUES
                                                                               (1, 'Fire Emergency Response', 'Standard procedure for handling fire incidents'),
                                                                               (2, 'Medical Emergency Protocol', 'Protocol for medical emergencies and first aid'),
                                                                               (3, 'Security Breach Response', 'Procedure for unauthorized access incidents'),
                                                                               (4, 'Hazardous Material Spill', 'Protocol for chemical and hazardous material spills'),
                                                                               (5, 'Power Outage Response', 'Procedure for electrical power failures');

-- 8. StandardOperatingProcedure_applicable_incident_types
INSERT INTO standard_operating_procedure_applicable_incident_types (standard_operating_procedure_id, applicable_incident_types) VALUES
                                                                                                                                    (1, 'FIRE'),
                                                                                                                                    (2, 'MEDICAL'),
                                                                                                                                    (3, 'SECURITY_THREAT'),
                                                                                                                                    (4, 'TECHNICAL'),
                                                                                                                                    (5, 'TECHNICAL');

-- 9. AirportResource
INSERT INTO airport_resource (id, resource_name, resource_type) VALUES
                                                                    (1, 'Fire Truck A1', 'VEHICLE'),
                                                                    (2, 'Medical Kit Station 3', 'EQUIPMENT'),
                                                                    (3, 'Security Camera T1-45', 'SURVEILLANCE'),
                                                                    (4, 'Emergency Generator B', 'INFRASTRUCTURE'),
                                                                    (5, 'Fuel Pipeline Valve 7', 'INFRASTRUCTURE'),
                                                                    (6, 'X-Ray Scanner A', 'SECURITY_EQUIPMENT');

-- 10. Incident
INSERT INTO incident (id, report_number, report_time, close_time, description, type, priority, status, source, location_id, dispatcher_id, team_id, manager_id, sop_id) VALUES
                                                                                                                                                                            (1, 'INC-2024-001', '2024-01-15 08:30:00', '2024-01-15 10:15:00', 'Small electrical fire in control panel', 'FIRE', 'HIGH', 'CLOSED', 'MANUAL', 1, 1, 1, 3, 1),
                                                                                                                                                                            (2, 'INC-2024-002', '2024-01-15 14:45:00', NULL, 'Passenger collapsed near gate 12', 'MEDICAL', 'CRITICAL', 'IN_PROGRESS', 'MANUAL', 2, 2, 2, 4, 2),
                                                                                                                                                                            (3, 'INC-2024-003', '2024-01-16 03:20:00', '2024-01-16 05:30:00', 'Unauthorized person in restricted area', 'SECURITY_THREAT', 'NORMAL', 'CLOSED', 'ACCESS_CONTROL', 3, 1, 3, 3, 3),
                                                                                                                                                                            (4, 'INC-2024-004', '2024-01-16 11:10:00', NULL, 'Minor fuel spill during refueling', 'TECHNICAL', 'HIGH', 'ASSIGNED', 'SYSTEM', 6, 2, 1, 4, 4),
                                                                                                                                                                            (5, 'INC-2024-005', '2024-01-17 19:45:00', NULL, 'Power outage in terminal B', 'TECHNICAL', 'NORMAL', 'NEW', 'SYSTEM', 1, 1, 4, 3, 5);

-- 11. Incident_resources (tabela asocjacyjna) - POPRAWIONE NAZWY KOLUMN!
-- Hibernate wygenerował: affected_resources_id i incidents_id
INSERT INTO incident_resources (affected_resources_id, incidents_id) VALUES
                                                                         (1, 1),
                                                                         (4, 1),
                                                                         (2, 2),
                                                                         (3, 3),
                                                                         (5, 4),
                                                                         (4, 5);

-- 12. LogEntry
INSERT INTO log_entry (id, action_time, action_description, incident_id, employee_id) VALUES
                                                                                          (1, '2024-01-15 08:35:00', 'Dispatched Alpha Team to location', 1, 1),
                                                                                          (2, '2024-01-15 09:15:00', 'Fire extinguished, area secured', 1, 5),
                                                                                          (3, '2024-01-15 14:50:00', 'Medical team dispatched with defibrillator', 2, 2),
                                                                                          (4, '2024-01-16 03:25:00', 'Security breach detected, initiating lockdown', 3, 1),
                                                                                          (5, '2024-01-16 04:45:00', 'Suspect apprehended and escorted out', 3, 7),
                                                                                          (6, '2024-01-16 11:15:00', 'Hazmat team notified, spill contained', 4, 2);

-- 13. Attachment
INSERT INTO attachment (id, file_name, file_type, url, log_entry_id) VALUES
                                                                         (1, 'fire_photo_001.jpg', 'image/jpeg', 'https://storage.example.com/incidents/fire_001.jpg', 2),
                                                                         (2, 'medical_report_002.pdf', 'application/pdf', 'https://storage.example.com/medical/report_002.pdf', 3),
                                                                         (3, 'security_footage_003.mp4', 'video/mp4', 'https://storage.example.com/security/cam_003.mp4', 5),
                                                                         (4, 'spill_report_004.pdf', 'application/pdf', 'https://storage.example.com/hazmat/report_004.pdf', 6),
                                                                         (5, 'incident_summary_001.docx', 'application/docx', 'https://storage.example.com/reports/summary_001.docx', 1),
                                                                         (6, 'team_photo_002.jpg', 'image/jpeg', 'https://storage.example.com/teams/photo_002.jpg', 2);

-- 14. AuditLog
INSERT INTO audit_logs (id, incident_id, employee_id, action_type, action_details, timestamp, ip_address, user_agent) VALUES
                                                                                                                          (1, 1, 1, 'INCIDENT_CREATED', 'New fire incident reported via sensor', '2024-01-15 08:30:05', '192.168.1.100', 'Mozilla/5.0'),
                                                                                                                          (2, 1, 5, 'STATUS_CHANGED', 'Status changed from OPEN to IN_PROGRESS', '2024-01-15 08:40:00', '10.0.1.15', 'Chrome/120.0'),
                                                                                                                          (3, 1, 3, 'ESCALATION', 'Incident escalated to Security Manager', '2024-01-15 09:00:00', '192.168.1.50', 'Safari/17.0'),
                                                                                                                          (4, 2, 2, 'INCIDENT_CREATED', 'Medical emergency reported by staff', '2024-01-15 14:45:10', '192.168.1.101', 'Firefox/121.0'),
                                                                                                                          (5, 3, 1, 'INCIDENT_CREATED', 'Security breach detected by guard', '2024-01-16 03:20:15', '192.168.1.100', 'Mozilla/5.0'),
                                                                                                                          (6, 3, 3, 'INCIDENT_CLOSED', 'Incident resolved and closed', '2024-01-16 05:30:00', '192.168.1.50', 'Safari/17.0');

-- 15. Certificate
INSERT INTO certificate (id, certificate_name, expiry_date, member_id) VALUES
                                                                           (1, 'Advanced Firefighting', '2025-06-30 00:00:00', 5),
                                                                           (2, 'Hazardous Materials Handling', '2024-12-31 00:00:00', 5),
                                                                           (3, 'Advanced First Aid & CPR', '2025-03-15 00:00:00', 7),
                                                                           (4, 'Emergency Medical Technician', '2026-01-01 00:00:00', 7),
                                                                           (5, 'Security Threat Assessment', '2024-09-30 00:00:00', 6),
                                                                           (6, 'K9 Unit Handling', '2025-08-20 00:00:00', 8);

-- 16. ProcedureStep
INSERT INTO procedure_step (id, step_order, step_description, sop_id) VALUES
                                                                          (1, 1, 'Activate alarm and notify control room', 1),
                                                                          (2, 2, 'Evacuate immediate area', 1),
                                                                          (3, 3, 'Deploy fire extinguishers if safe', 1),
                                                                          (4, 4, 'Await fire department arrival', 1),
                                                                          (5, 1, 'Assess patient consciousness', 2),
                                                                          (6, 2, 'Call for medical backup', 2),
                                                                          (7, 3, 'Perform CPR if needed', 2),
                                                                          (8, 4, 'Prepare for ambulance arrival', 2),
                                                                          (9, 1, 'Initiate area lockdown', 3),
                                                                          (10, 2, 'Deploy security team', 3),
                                                                          (11, 3, 'Coordinate with police', 3),
                                                                          (12, 4, 'Document incident details', 3);

-- 17. Report
INSERT INTO report (id, generated_at, report_type, date_range, manager_id) VALUES
                                                                               (1, '2024-01-31 10:00:00', 'MONTHLY_SUMMARY', '2024-01-01 to 2024-01-31', 3),
                                                                               (2, '2024-01-31 14:30:00', 'TEAM_PERFORMANCE', '2024-01-01 to 2024-01-31', 4),
                                                                               (3, '2024-01-20 09:15:00', 'INCIDENT_ANALYSIS', '2024-01-15 to 2024-01-20', 3),
                                                                               (4, '2024-01-10 16:45:00', 'RESOURCE_USAGE', '2024-01-01 to 2024-01-10', 4);

-- 18. Report_analyzed_incidents - POPRAWIONE NAZWY KOLUMN!
-- Hibernate wygenerował: analyzed_incidents_id i report_id
INSERT INTO report_analyzed_incidents (report_id, analyzed_incidents_id) VALUES
                                                                             (1, 1),
                                                                             (1, 2),
                                                                             (1, 3),
                                                                             (1, 4),
                                                                             (2, 1),
                                                                             (2, 2),
                                                                             (3, 1),
                                                                             (3, 3),
                                                                             (4, 4),
                                                                             (4, 5);

-- 19. SensorEvent
INSERT INTO sensor_events (id, sensor_id, sensor_type, reading_value, unit, severity, location_details, timestamp, location_id, incident_id, is_processed, processed_at) VALUES
                                                                                                                                                                             (1, 'SENSOR-F001', 'FIRE', 85.5, '°C', 0.8, 'Control panel room', '2024-01-15 08:28:30', 1, 1, true, '2024-01-15 08:30:00'),
                                                                                                                                                                             (2, 'SENSOR-S045', 'SMOKE', 15.2, 'μg/m³', 0.6, 'Near electrical room', '2024-01-15 08:29:00', 1, 1, true, '2024-01-15 08:30:00'),
                                                                                                                                                                             (3, 'SENSOR-M201', 'MOTION', 1.0, 'count', 0.3, 'Restricted corridor', '2024-01-16 03:18:45', 3, 3, true, '2024-01-16 03:20:00'),
                                                                                                                                                                             (4, 'SENSOR-T301', 'TEMPERATURE', -5.0, '°C', 0.4, 'Outdoor sensor', '2024-01-17 19:40:00', 1, NULL, false, NULL),
                                                                                                                                                                             (5, 'SENSOR-C101', 'CAMERA', NULL, NULL, 0.7, 'Gate 12 camera detected fall', '2024-01-15 14:44:30', 2, 2, true, '2024-01-15 14:45:00'),
                                                                                                                                                                             (6, 'SENSOR-P401', 'PRESSURE', 0.0, 'bar', 0.9, 'Fuel pipeline pressure drop', '2024-01-16 11:08:20', 6, 4, true, '2024-01-16 11:10:00');

-- 20. Shift
INSERT INTO shift (id, name, start_time, end_time) VALUES
                                                       (1, 'Morning Shift', '2024-01-15 06:00:00', '2024-01-15 14:00:00'),
                                                       (2, 'Afternoon Shift', '2024-01-15 14:00:00', '2024-01-15 22:00:00'),
                                                       (3, 'Night Shift', '2024-01-15 22:00:00', '2024-01-16 06:00:00'),
                                                       (4, 'Morning Shift', '2024-01-16 06:00:00', '2024-01-16 14:00:00'),
                                                       (5, 'Afternoon Shift', '2024-01-16 14:00:00', '2024-01-16 22:00:00');

-- 21. ShiftAssignment
INSERT INTO shift_assignment (id, status, actual_check_in, actual_check_out, notes, employee_id, shift_id) VALUES
                                                                                                               (1, 'PRESENT', '2024-01-15 05:55:00', '2024-01-15 14:05:00', 'On time', 1, 1),
                                                                                                               (2, 'PRESENT', '2024-01-15 13:58:00', '2024-01-15 22:10:00', 'Late checkout due to incident', 2, 2),
                                                                                                               (3, 'ABSENT', NULL, NULL, 'Sick leave', 3, 3),
                                                                                                               (4, 'PRESENT', '2024-01-16 05:50:00', '2024-01-16 14:00:00', 'Early arrival', 4, 4),
                                                                                                               (5, 'PRESENT', '2024-01-16 14:15:00', '2024-01-16 22:00:00', 'Traffic delay', 5, 5),
                                                                                                               (6, 'SICK_LEAVE', NULL, NULL, 'Medical appointment', 6, 1);

-- ============================================
-- DODATKOWE INSERTY DLA RELACJI MANY-TO-ONE
-- ============================================

-- Dodatkowe Employee i IncidentTeamMember
INSERT INTO employee (id, first_name, last_name, service_number) VALUES
                                                                     (9, 'James', 'White', 'SEC-009'),
                                                                     (10, 'Maria', 'Garcia', 'SEC-010'),
                                                                     (11, 'Thomas', 'Lee', 'SEC-011'),
                                                                     (12, 'Emily', 'Clark', 'SEC-012');

INSERT INTO incident_team_member (id, radio_id, team_id) VALUES
                                                             (9, 'RADIO-701', 3),
                                                             (10, 'RADIO-702', 3),
                                                             (11, 'RADIO-801', 4),
                                                             (12, 'RADIO-802', 5);

-- Dodatkowe Audits
INSERT INTO audit_logs (id, incident_id, employee_id, action_type, action_details, timestamp, ip_address, user_agent) VALUES
                                                                                                                          (7, 4, 4, 'ESCALATION', 'Hazmat incident requires manager review', '2024-01-16 11:20:00', '192.168.1.50', 'Safari/17.0'),
                                                                                                                          (8, 5, 3, 'PRIORITY_CHANGED', 'Priority upgraded due to passenger impact', '2024-01-17 19:50:00', '192.168.1.50', 'Safari/17.0');

-- Dodatkowe LogEntries
INSERT INTO log_entry (id, action_time, action_description, incident_id, employee_id) VALUES
                                                                                          (7, '2024-01-17 19:50:00', 'Power backup systems activated', 5, 11),
                                                                                          (8, '2024-01-17 20:15:00', 'Technicians dispatched to repair', 5, 4);

-- Dodatkowe Attachments
INSERT INTO attachment (id, file_name, file_type, url, log_entry_id) VALUES
                                                                         (7, 'power_outage_diagram.pdf', 'application/pdf', 'https://storage.example.com/electrical/diagram_005.pdf', 7),
                                                                         (8, 'repair_photos_005.jpg', 'image/jpeg', 'https://storage.example.com/repairs/photo_005.jpg', 8);

-- Dodatkowe Certificates
INSERT INTO certificate (id, certificate_name, expiry_date, member_id) VALUES
                                                                           (7, 'Electrical Safety', '2025-11-30 00:00:00', 11),
                                                                           (8, 'Power Systems', '2024-10-15 00:00:00', 11),
                                                                           (9, 'K9 Handler Advanced', '2025-05-20 00:00:00', 12),
                                                                           (10, 'Explosive Detection', '2026-02-28 00:00:00', 12);

-- Dodatkowe SensorEvents
INSERT INTO sensor_events (id, sensor_id, sensor_type, reading_value, unit, severity, location_details, timestamp, location_id, incident_id, is_processed, processed_at) VALUES
                                                                                                                                                                             (7, 'SENSOR-H001', 'HUMIDITY', 65.5, '%', 0.1, 'Normal operation', '2024-01-18 10:00:00', 2, NULL, true, '2024-01-18 10:00:05'),
                                                                                                                                                                             (8, 'SENSOR-V001', 'VIBRATION', 2.1, 'm/s²', 0.05, 'Near runway', '2024-01-18 12:30:00', 2, NULL, true, '2024-01-18 12:30:10');

-- Dodatkowe AirportResources
INSERT INTO airport_resource (id, resource_name, resource_type) VALUES
                                                                    (7, 'Ambulance Unit 1', 'VEHICLE'),
                                                                    (8, 'Defibrillator Station', 'EQUIPMENT'),
                                                                    (9, 'Thermal Camera X1', 'SURVEILLANCE'),
                                                                    (10, 'Communication Tower', 'INFRASTRUCTURE');

-- Dodatkowe StandardOperatingProcedure_applicable_incident_types
INSERT INTO standard_operating_procedure_applicable_incident_types (standard_operating_procedure_id, applicable_incident_types) VALUES
                                                                                                                                    (1, 'TECHNICAL'),
                                                                                                                                    (3, 'OTHER'),
                                                                                                                 (4, 'FIRE');

SELECT setval('incidents_seq', COALESCE((SELECT MAX(id) FROM incident), 0) + 1);
SELECT setval('log_entries_seq', COALESCE((SELECT MAX(id) FROM log_entry), 0) + 1);
SELECT setval('audit_logs_seq', COALESCE((SELECT MAX(id) FROM audit_logs), 0) + 1);