-- ============================================
-- POPRAWIONE INSERTY DLA ENUMÓW
-- ZGODNIE Z STRUKTURĄ WYGENEROWANĄ PRZEZ HIBERNATE
-- ============================================
CREATE SEQUENCE IF NOT EXISTS incidents_seq;
CREATE SEQUENCE IF NOT EXISTS log_entries_seq;
CREATE SEQUENCE IF NOT EXISTS audit_logs_seq;

-- 1. Location
INSERT INTO location (id, name, type, coordinates) VALUES
                                                       (1, 'Terminal A', 'TERMINAL', '51.4775, 0.4614'),
                                                       (2, 'Terminal B', 'TERMINAL', '51.4780, 0.4620'),
                                                       (3, 'Terminal C', 'TERMINAL', '51.4790, 0.4630'),
                                                       (4, 'Terminal D', 'TERMINAL', '51.4800, 0.4640'),
                                                       (5, 'Runway 09R/27L', 'RUNWAY', '51.4810, 0.4650'),
                                                       (6, 'Runway 18/36', 'RUNWAY', '51.4820, 0.4660'),
                                                       (7, 'Runway 05/23', 'RUNWAY', '51.4830, 0.4670'),
                                                       (8, 'Taxiway Alpha', 'RUNWAY', '51.4840, 0.4680'),
                                                       (9, 'Taxiway Bravo', 'RUNWAY', '51.4850, 0.4690'),
                                                       (10, 'Gate A1', 'GATE', '51.4860, 0.4700'),
                                                       (11, 'Gate A2', 'GATE', '51.4870, 0.4710'),
                                                       (12, 'Gate A3', 'GATE', '51.4880, 0.4720'),
                                                       (13, 'Gate B1', 'GATE', '51.4890, 0.4730'),
                                                       (14, 'Gate B2', 'GATE', '51.4900, 0.4740'),
                                                       (15, 'Gate B3', 'GATE', '51.4910, 0.4750'),
                                                       (16, 'Gate C1', 'GATE', '51.4920, 0.4760'),
                                                       (17, 'Gate C2', 'GATE', '51.4930, 0.4770'),
                                                       (18, 'Gate C3', 'GATE', '51.4940, 0.4780'),
                                                       (19, 'Gate D1', 'GATE', '51.4950, 0.4790'),
                                                       (20, 'Gate D2', 'GATE', '51.4960, 0.4800'),
                                                       (21, 'Gate G15', 'GATE', '51.4970, 0.4810'),
                                                       (22, 'Gate G20', 'GATE', '51.4980, 0.4820'),
                                                       (23, 'Gate G25', 'GATE', '51.4990, 0.4830'),
                                                       (24, 'Security Checkpoint 1', 'SECURITY_CHECKPOINT', '51.5000, 0.4840'),
                                                       (25, 'Security Checkpoint 2', 'SECURITY_CHECKPOINT', '51.5010, 0.4850'),
                                                       (26, 'Security Checkpoint 3', 'SECURITY_CHECKPOINT', '51.5020, 0.4860'),
                                                       (27, 'Security Checkpoint 4', 'SECURITY_CHECKPOINT', '51.5030, 0.4870'),
                                                       (28, 'Security Checkpoint North', 'SECURITY_CHECKPOINT', '51.5040, 0.4880'),
                                                       (29, 'Security Checkpoint Central', 'SECURITY_CHECKPOINT', '51.5050, 0.4890'),
                                                       (30, 'Security Checkpoint South', 'SECURITY_CHECKPOINT', '51.5060, 0.4900'),
                                                       (31, 'Baggage Claim 1', 'BAGGAGE', '51.5070, 0.4910'),
                                                       (32, 'Baggage Claim 2', 'BAGGAGE', '51.5080, 0.4920'),
                                                       (33, 'Baggage Claim 3', 'BAGGAGE', '51.5090, 0.4930'),
                                                       (34, 'Baggage Claim 4', 'BAGGAGE', '51.5100, 0.4940'),
                                                       (35, 'Baggage Claim International', 'BAGGAGE', '51.5110, 0.4950'),
                                                       (36, 'Baggage Claim Domestic', 'BAGGAGE', '51.5120, 0.4960'),
                                                       (37, 'Maintenance Bay 1', 'MAINTENANCE', '51.5130, 0.4970'),
                                                       (38, 'Maintenance Bay 2', 'MAINTENANCE', '51.5140, 0.4980'),
                                                       (39, 'Maintenance Bay 3', 'MAINTENANCE', '51.5150, 0.4990'),
                                                       (40, 'Maintenance Bay 4', 'MAINTENANCE', '51.5160, 0.5000'),
                                                       (41, 'Maintenance Bay 5', 'MAINTENANCE', '51.5170, 0.5010'),
                                                       (42, 'Maintenance Bay 6', 'MAINTENANCE', '51.5180, 0.5020'),
                                                       (43, 'Maintenance Bay 7', 'MAINTENANCE', '51.5190, 0.5030'),
                                                       (44, 'Maintenance Bay 8', 'MAINTENANCE', '51.5200, 0.5040'),
                                                       (45, 'Parking Lot P1', 'PARKING', '51.5210, 0.5050'),
                                                       (46, 'Parking Lot P2', 'PARKING', '51.5220, 0.5060'),
                                                       (47, 'Parking Lot P3', 'PARKING', '51.5230, 0.5070'),
                                                       (48, 'Parking Lot P4', 'PARKING', '51.5240, 0.5080'),
                                                       (49, 'Parking Lot P5', 'PARKING', '51.5250, 0.5090'),
                                                       (50, 'Parking Lot P6', 'PARKING', '51.5260, 0.5100'),
                                                       (51, 'Fuel Storage Area 1', 'FUEL_STORAGE', '51.5270, 0.5110'),
                                                       (52, 'Fuel Storage Area 2', 'FUEL_STORAGE', '51.5280, 0.5120'),
                                                       (53, 'Fuel Storage Area 3', 'FUEL_STORAGE', '51.5290, 0.5130'),
                                                       (54, 'Cargo Warehouse 1', 'CARGO_AREA', '51.5300, 0.5140'),
                                                       (55, 'Cargo Warehouse 2', 'CARGO_AREA', '51.5310, 0.5150'),
                                                       (56, 'Cargo Warehouse 3', 'CARGO_AREA', '51.5320, 0.5160'),
                                                       (57, 'Cargo Warehouse 4', 'CARGO_AREA', '51.5330, 0.5170'),
                                                       (58, 'Control Tower Main', 'CONTROL_TOWER', '51.5340, 0.5180'),
                                                       (59, 'Control Tower Backup', 'CONTROL_TOWER', '51.5350, 0.5190'),
                                                       (60, 'Fire Station 1', 'FIRE_STATION', '51.5360, 0.5200'),
                                                       (61, 'Fire Station 2', 'FIRE_STATION', '51.5370, 0.5210'),
                                                       (62, 'Medical Center Main', 'MEDICAL_CENTER', '51.5380, 0.5220'),
                                                       (63, 'Medical Center Satellite', 'MEDICAL_CENTER', '51.5390, 0.5230'),
                                                       (64, 'Security Office Main', 'SECURITY_OFFICE', '51.5400, 0.5240'),
                                                       (65, 'Security Office North', 'SECURITY_OFFICE', '51.5410, 0.5250'),
                                                       (66, 'Security Office South', 'SECURITY_OFFICE', '51.5420, 0.5260'),
                                                       (67, 'Operations Center', 'OPERATIONS_CENTER', '51.5430, 0.5270'),
                                                       (68, 'Emergency Command Center', 'COMMAND_CENTER', '51.5440, 0.5280');

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
                                                                      (1, 'Alpha Team', 'TECHNICAL_WATER_LEAKAGE', 'AVAILABLE'),
                                                                      (2, 'Bravo Team', 'MEDICAL_EMERGENCY', 'BUSY'),
                                                                      (3, 'Charlie Team', 'FIRE_ELECTRICAL', 'UNAVAILABLE'),
                                                                      (4, 'Delta Team', 'FIRE_CHEMICAL', 'AVAILABLE'),
                                                                      (5, 'Echo Team', 'EQUIPMENT_ELEVATOR_STUCK', 'BUSY');

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

-- 8. StandardOperatingProcedure_applicable_incident_types - POPRAWIONE!
INSERT INTO standard_operating_procedure_applicable_incident_types (standard_operating_procedure_id, applicable_incident_types) VALUES
                                                                                                                                    (1, 'FIRE_ELECTRICAL'),
                                                                                                                                    (1, 'FIRE_CHEMICAL'),
                                                                                                                                    (1, 'FIRE_STRUCTURAL'),
                                                                                                                                    (1, 'FIRE_VEHICLE'),
                                                                                                                                    (2, 'MEDICAL_EMERGENCY'),
                                                                                                                                    (2, 'MEDICAL_HEART_ATTACK'),
                                                                                                                                    (2, 'MEDICAL_STROKE'),
                                                                                                                                    (2, 'MEDICAL_ALLERGIC_REACTION'),
                                                                                                                                    (3, 'SECURITY_UNAUTHORIZED_ACCESS'),
                                                                                                                                    (3, 'SECURITY_SUSPICIOUS_PACKAGE'),
                                                                                                                                    (3, 'SECURITY_ASSAULT'),
                                                                                                                                    (3, 'SECURITY_THEFT'),
                                                                                                                                    (4, 'HAZARDOUS_MATERIAL_SPILL'),
                                                                                                                                    (4, 'FIRE_CHEMICAL'),
                                                                                                                                    (5, 'TECHNICAL_POWER_OUTAGE'),
                                                                                                                                    (5, 'TECHNICAL_EQUIPMENT_FAILURE');

-- 9. AirportResource
INSERT INTO airport_resource (id, resource_name, resource_type) VALUES
                                                                    (1, 'Fire Truck A1', 'VEHICLE'),
                                                                    (2, 'Medical Kit Station 3', 'EQUIPMENT'),
                                                                    (3, 'Security Camera T1-45', 'SURVEILLANCE'),
                                                                    (4, 'Emergency Generator B', 'INFRASTRUCTURE'),
                                                                    (5, 'Fuel Pipeline Valve 7', 'INFRASTRUCTURE'),
                                                                    (6, 'X-Ray Scanner A', 'SECURITY_EQUIPMENT');

-- 10. Incident - POPRAWIONE TYPY!
INSERT INTO incident (id, report_number, report_time, close_time, description, type, priority, status, source, location_id, dispatcher_id, team_id, manager_id, sop_id) VALUES
                                                                                                                                                                            (1, 'INC-2024-001', '2024-01-15 08:30:00', '2024-01-15 10:15:00', 'Small electrical fire in control panel', 'FIRE_ELECTRICAL', 'HIGH', 'CLOSED', 'MANUAL', 1, 1, 3, 3, 1),
                                                                                                                                                                            (2, 'INC-2024-002', '2024-01-15 14:45:00', NULL, 'Passenger collapsed near gate 12', 'MEDICAL_HEART_ATTACK', 'CRITICAL', 'IN_PROGRESS', 'MANUAL', 21, 2, 2, 4, 2),
                                                                                                                                                                            (3, 'INC-2024-003', '2024-01-16 03:20:00', '2024-01-16 05:30:00', 'Unauthorized person in restricted area', 'SECURITY_UNAUTHORIZED_ACCESS', 'NORMAL', 'CLOSED', 'ACCESS_CONTROL', 43, 1, 1, 3, 3),
                                                                                                                                                                            (4, 'INC-2024-004', '2024-01-16 11:10:00', NULL, 'Minor fuel spill during refueling', 'HAZARDOUS_MATERIAL_SPILL', 'HIGH', 'ASSIGNED', 'SYSTEM', 51, 2, 4, 4, 4),
                                                                                                                                                                            (5, 'INC-2024-005', '2024-01-17 19:45:00', NULL, 'Power outage in terminal B', 'TECHNICAL_POWER_OUTAGE', 'NORMAL', 'NEW', 'SYSTEM', 2, 1, 5, 3, 5);

-- 11. Incident_resources (tabela asocjacyjna)
INSERT INTO incident_resources (affected_resources_id, incidents_id) VALUES
                                                                         (1, 1),
                                                                         (4, 1),
                                                                         (2, 2),
                                                                         (3, 3),
                                                                         (5, 4),
                                                                         (4, 5);

-- 12. LogEntry
INSERT INTO log_entry (id, action_time, action_description, incident_id, employee_id) VALUES
                                                                                          (1, '2024-01-15 08:35:00', 'Dispatched Charlie Team (Fire) to location', 1, 1),
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

-- 18. Report_analyzed_incidents
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
INSERT INTO sensor_events (sensor_id, sensor_type, location_details, timestamp, location_id, incident_id, is_processed) VALUES
                                                                                                                            ('SENSOR-F001', 'FIRE', 'Control panel room', '2024-01-15 08:28:30', 1, 1, true),
                                                                                                                            ('SENSOR-S045', 'SMOKE', 'Near electrical room', '2024-01-15 08:29:00', 1, 1, true),
                                                                                                                            ('SENSOR-M201', 'MOTION', 'Restricted corridor', '2024-01-16 03:18:45', 43, 3, true),
                                                                                                                            ('SENSOR-T301', 'TEMPERATURE', 'Outdoor sensor', '2024-01-17 19:40:00', 1, NULL, false),
                                                                                                                            ('SENSOR-C101', 'CAMERA', 'Gate 12 camera detected fall', '2024-01-15 14:44:30', 21, 2, true),
                                                                                                                            ('SENSOR-P401', 'PRESSURE', 'Fuel pipeline pressure drop', '2024-01-16 11:08:20', 51, 4, true);

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
INSERT INTO employee (id, first_name, last_name, service_number, username) VALUES
                                                                     (9, 'James', 'White', 'SEC-009', 'admin'),
                                                                     (10, 'Maria', 'Garcia', 'SEC-010', 'admin1'),
                                                                     (11, 'Thomas', 'Lee', 'SEC-011', 'admin2'),
                                                                     (12, 'Emily', 'Clark', 'SEC-012', 'admin3');

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

-- Dodatkowe AirportResources
INSERT INTO airport_resource (id, resource_name, resource_type) VALUES
                                                                    (7, 'Ambulance Unit 1', 'VEHICLE'),
                                                                    (8, 'Defibrillator Station', 'EQUIPMENT'),
                                                                    (9, 'Thermal Camera X1', 'SURVEILLANCE'),
                                                                    (10, 'Communication Tower', 'INFRASTRUCTURE');

-- ============================================
-- KONIEC - USTAWIENIE SEKWENCJI
-- ============================================

-- Ustaw sekwencje na odpowiednie wartości
SELECT setval('incidents_seq', COALESCE((SELECT MAX(id) FROM incident), 0) + 1);
SELECT setval('log_entries_seq', COALESCE((SELECT MAX(id) FROM log_entry), 0) + 1);
SELECT setval('audit_logs_seq', COALESCE((SELECT MAX(id) FROM audit_logs), 0) + 1);