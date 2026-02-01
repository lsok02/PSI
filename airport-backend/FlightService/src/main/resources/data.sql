-- Airline - Linie lotnicze
INSERT INTO airline (id, name, code) VALUES (1, 'LOT Polish Airlines', 'LO');
INSERT INTO airline (id, name, code) VALUES (2, 'Lufthansa', 'LH');
INSERT INTO airline (id, name, code) VALUES (3, 'Ryanair', 'FR');
INSERT INTO airline (id, name, code) VALUES (4, 'British Airways', 'BA');
INSERT INTO airline (id, name, code) VALUES (5, 'Air France', 'AF');

-- Aircraft - Samoloty
INSERT INTO aircraft (id, registration_number, model, seat_count, airline_id) VALUES (1, 'SP-LRA', 'Embraer E195', 120, 1);
INSERT INTO aircraft (id, registration_number, model, seat_count, airline_id) VALUES (2, 'SP-LRB', 'Embraer E195', 120, 1);
INSERT INTO aircraft (id, registration_number, model, seat_count, airline_id) VALUES (3, 'D-AIXP', 'Airbus A320', 180, 2);
INSERT INTO aircraft (id, registration_number, model, seat_count, airline_id) VALUES (4, 'EI-DWR', 'Boeing 737-800', 189, 3);
INSERT INTO aircraft (id, registration_number, model, seat_count, airline_id) VALUES (5, 'G-EUPD', 'Airbus A321', 220, 4);
INSERT INTO aircraft (id, registration_number, model, seat_count, airline_id) VALUES (6, 'F-GTAE', 'Airbus A320', 180, 5);
INSERT INTO aircraft (id, registration_number, model, seat_count, airline_id) VALUES (7, 'SP-LRC', 'Boeing 787-9', 294, 1);

-- Route - Trasy lotów
INSERT INTO route (id, departure_airport, destination_airport, route_code, type) VALUES (1, 'WAW', 'JFK', 'WAWJFK01', 'LONG_HAUL');
INSERT INTO route (id, departure_airport, destination_airport, route_code, type) VALUES (2, 'WAW', 'CDG', 'WAWCDG01', 'SHORT_HAUL');
INSERT INTO route (id, departure_airport, destination_airport, route_code, type) VALUES (3, 'WAW', 'FRA', 'WAWFRA01', 'SHORT_HAUL');
INSERT INTO route (id, departure_airport, destination_airport, route_code, type) VALUES (4, 'KRK', 'STN', 'KRKSTN01', 'SHORT_HAUL');
INSERT INTO route (id, departure_airport, destination_airport, route_code, type) VALUES (5, 'WAW', 'LHR', 'WAWLHR01', 'SHORT_HAUL');
INSERT INTO route (id, departure_airport, destination_airport, route_code, type) VALUES (6, 'WAW', 'ORD', 'WAWORD01', 'LONG_HAUL');
INSERT INTO route (id, departure_airport, destination_airport, route_code, type) VALUES (7, 'KTW', 'DUB', 'KTWDUB01', 'SHORT_HAUL');

-- FlightSchedule - Harmonogramy lotów
INSERT INTO flight_schedule (id, effective_date, version) VALUES (1, '2024-01-01', 'WINTER_2024');
INSERT INTO flight_schedule (id, effective_date, version) VALUES (2, '2024-03-31', 'SUMMER_2024');
INSERT INTO flight_schedule (id, effective_date, version) VALUES (3, '2024-10-27', 'WINTER_2024_2025');
INSERT INTO flight_schedule (id, effective_date, version) VALUES (4, '2024-06-01', 'SUMMER_PEAK_2024');

-- TimeSlot - Sloty czasowe
INSERT INTO time_slot (id, start_time, landing_time, confirmed) VALUES (1, '2024-06-15 06:00:00', '2024-06-15 08:30:00', true);
INSERT INTO time_slot (id, start_time, landing_time, confirmed) VALUES (2, '2024-06-15 10:15:00', '2024-06-15 11:45:00', true);
INSERT INTO time_slot (id, start_time, landing_time, confirmed) VALUES (3, '2024-06-15 14:30:00', '2024-06-15 17:00:00', true);
INSERT INTO time_slot (id, start_time, landing_time, confirmed) VALUES (4, '2024-06-15 19:45:00', '2024-06-16 06:30:00', true);
INSERT INTO time_slot (id, start_time, landing_time, confirmed) VALUES (5, '2024-06-16 07:20:00', '2024-06-16 08:50:00', false);
INSERT INTO time_slot (id, start_time, landing_time, confirmed) VALUES (6, '2024-06-16 12:10:00', '2024-06-16 14:40:00', true);

-- Runway - Pasy startowe
INSERT INTO runway (id, runway_number, length, is_available) VALUES (1, '11/29', 3690, true);
INSERT INTO runway (id, runway_number, length, is_available) VALUES (2, '15/33', 2800, true);
INSERT INTO runway (id, runway_number, length, is_available) VALUES (3, '09/27', 3200, false);
INSERT INTO runway (id, runway_number, length, is_available) VALUES (4, '12/30', 3500, true);
INSERT INTO runway (id, runway_number, length, is_available) VALUES (5, '07/25', 2900, true);

-- Gate - Bramki
INSERT INTO gate (id, gate_number, terminal, is_available) VALUES (1, 'A1', 'A', true);
INSERT INTO gate (id, gate_number, terminal, is_available) VALUES (2, 'A2', 'A', true);
INSERT INTO gate (id, gate_number, terminal, is_available) VALUES (3, 'A3', 'A', false);
INSERT INTO gate (id, gate_number, terminal, is_available) VALUES (4, 'B1', 'B', true);
INSERT INTO gate (id, gate_number, terminal, is_available) VALUES (5, 'B2', 'B', true);
INSERT INTO gate (id, gate_number, terminal, is_available) VALUES (6, 'C1', 'C', true);
INSERT INTO gate (id, gate_number, terminal, is_available) VALUES (7, 'C2', 'C', true);

-- ParkingStand - Miejsca postojowe
INSERT INTO parking_stand (id, stand_number, type, is_available) VALUES (1, 'P1', 'REMOTE', true);
INSERT INTO parking_stand (id, stand_number, type, is_available) VALUES (2, 'P2', 'REMOTE', true);
INSERT INTO parking_stand (id, stand_number, type, is_available) VALUES (3, 'P3', 'CONTACT', false);
INSERT INTO parking_stand (id, stand_number, type, is_available) VALUES (4, 'P4', 'CONTACT', true);
INSERT INTO parking_stand (id, stand_number, type, is_available) VALUES (5, 'P5', 'REMOTE', true);
INSERT INTO parking_stand (id, stand_number, type, is_available) VALUES (6, 'P6', 'CONTACT', true);

-- CrewMember - Członkowie załogi
INSERT INTO crew_member (id, first_name, last_name, position) VALUES (1, 'Jan', 'Kowalski', 'CAPTAIN');
INSERT INTO crew_member (id, first_name, last_name, position) VALUES (2, 'Anna', 'Nowak', 'FIRST_OFFICER');
INSERT INTO crew_member (id, first_name, last_name, position) VALUES (3, 'Piotr', 'Wiśniewski', 'CAPTAIN');
INSERT INTO crew_member (id, first_name, last_name, position) VALUES (4, 'Maria', 'Wójcik', 'FLIGHT_ATTENDANT');
INSERT INTO crew_member (id, first_name, last_name, position) VALUES (5, 'Krzysztof', 'Kowalczyk', 'FLIGHT_ATTENDANT');
INSERT INTO crew_member (id, first_name, last_name, position) VALUES (6, 'Agnieszka', 'Kamińska', 'FIRST_OFFICER');
INSERT INTO crew_member (id, first_name, last_name, position) VALUES (7, 'Tomasz', 'Lewandowski', 'CAPTAIN');
INSERT INTO crew_member (id, first_name, last_name, position) VALUES (8, 'Ewa', 'Zielińska', 'FLIGHT_ATTENDANT');
INSERT INTO crew_member (id, first_name, last_name, position) VALUES (9, 'Michał', 'Szymański', 'FLIGHT_ATTENDANT');
INSERT INTO crew_member (id, first_name, last_name, position) VALUES (10, 'Katarzyna', 'Woźniak', 'FIRST_OFFICER');

-- TurnaroundProcess - Procesy obsługi naziemnej
INSERT INTO turnaround_process (id, start_time, end_time, status) VALUES (1, '2024-06-15 08:45:00', '2024-06-15 10:00:00', 'FINISHED');
INSERT INTO turnaround_process (id, start_time, end_time, status) VALUES (2, '2024-06-15 12:00:00', '2024-06-15 13:30:00', 'IN_PROGRESS');
INSERT INTO turnaround_process (id, start_time, end_time, status) VALUES (3, '2024-06-15 17:15:00', NULL, 'PLANNED');
INSERT INTO turnaround_process (id, start_time, end_time, status) VALUES (4, '2024-06-16 07:00:00', NULL, 'PLANNED');
INSERT INTO turnaround_process (id, start_time, end_time, status) VALUES (5, '2024-06-15 11:50:00', '2024-06-15 12:45:00', 'FINISHED');

-- GroundHandling - Obsługa naziemna
INSERT INTO ground_handling (id, team_name, service_type, turnaround_process_id) VALUES (1, 'Team Alpha', 'CLEANING', 1);
INSERT INTO ground_handling (id, team_name, service_type, turnaround_process_id) VALUES (2, 'Team Bravo', 'FUELING', 1);
INSERT INTO ground_handling (id, team_name, service_type, turnaround_process_id) VALUES (3, 'Team Charlie', 'CATERING', 2);
INSERT INTO ground_handling (id, team_name, service_type, turnaround_process_id) VALUES (4, 'Team Delta', 'BAGGAGE', 2);
INSERT INTO ground_handling (id, team_name, service_type, turnaround_process_id) VALUES (5, 'Team Echo', 'CLEANING', 3);
INSERT INTO ground_handling (id, team_name, service_type, turnaround_process_id) VALUES (6, 'Team Foxtrot', 'FUELING', 3);
INSERT INTO ground_handling (id, team_name, service_type, turnaround_process_id) VALUES (7, 'Team Golf', 'CATERING', 4);
INSERT INTO ground_handling (id, team_name, service_type, turnaround_process_id) VALUES (8, 'Team Hotel', 'BAGGAGE', 5);

-- Flight - Loty
INSERT INTO flight (id, flight_number, scheduled_departure_time, actual_departure_time, scheduled_arrival_time, actual_arrival_time, status, estimated_delay, delay_reason, schedule_id, route_id, time_slot_id, aircraft_id, runway_id, gate_id, parking_stand_id, turnaround_process_id)
VALUES (1, 'LO001', '2024-06-15 06:00:00', '2024-06-15 06:05:00', '2024-06-15 08:30:00', '2024-06-15 08:25:00', 'LANDED', 5, 'Boarding delay', 2, 1, 1, 7, 1, 6, NULL, 1);

INSERT INTO flight (id, flight_number, scheduled_departure_time, actual_departure_time, scheduled_arrival_time, actual_arrival_time, status, estimated_delay, delay_reason, schedule_id, route_id, time_slot_id, aircraft_id, runway_id, gate_id, parking_stand_id, turnaround_process_id)
VALUES (2, 'LH1234', '2024-06-15 10:15:00', '2024-06-15 10:15:00', '2024-06-15 11:45:00', NULL, 'DEPARTED', 0, NULL, 2, 3, 2, 3, 2, 1, NULL, 2);

INSERT INTO flight (id, flight_number, scheduled_departure_time, actual_departure_time, scheduled_arrival_time, actual_arrival_time, status, estimated_delay, delay_reason, schedule_id, route_id, time_slot_id, aircraft_id, runway_id, gate_id, parking_stand_id, turnaround_process_id)
VALUES (3, 'FR5678', '2024-06-15 14:30:00', NULL, '2024-06-15 17:00:00', NULL, 'DELAYED', 45, 'Technical issues', 2, 4, 3, 4, 4, NULL, 2, 3);

INSERT INTO flight (id, flight_number, scheduled_departure_time, actual_departure_time, scheduled_arrival_time, actual_arrival_time, status, estimated_delay, delay_reason, schedule_id, route_id, time_slot_id, aircraft_id, runway_id, gate_id, parking_stand_id, turnaround_process_id)
VALUES (4, 'BA789', '2024-06-15 19:45:00', '2024-06-15 19:40:00', '2024-06-16 06:30:00', NULL, 'DEPARTED', -5, NULL, 2, 5, 4, 5, 1, 7, NULL, NULL);

INSERT INTO flight (id, flight_number, scheduled_departure_time, actual_departure_time, scheduled_arrival_time, actual_arrival_time, status, estimated_delay, delay_reason, schedule_id, route_id, time_slot_id, aircraft_id, runway_id, gate_id, parking_stand_id, turnaround_process_id)
VALUES (5, 'LO002', '2024-06-16 07:20:00', NULL, '2024-06-16 08:50:00', NULL, 'PLANNED', 0, NULL, 2, 2, 5, 1, 2, 2, NULL, 4);

INSERT INTO flight (id, flight_number, scheduled_departure_time, actual_departure_time, scheduled_arrival_time, actual_arrival_time, status, estimated_delay, delay_reason, schedule_id, route_id, time_slot_id, aircraft_id, runway_id, gate_id, parking_stand_id, turnaround_process_id)
VALUES (6, 'AF456', '2024-06-16 12:10:00', NULL, '2024-06-16 14:40:00', NULL, 'PLANNED', 0, NULL, 2, 6, 6, 6, 5, 4, NULL, NULL);

-- flight_crew - Tabela łącząca loty z załogą
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (1, 1);
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (1, 2);
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (1, 4);
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (1, 5);
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (2, 3);
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (2, 6);
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (2, 8);
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (3, 7);
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (3, 10);
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (3, 9);
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (4, 1);
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (4, 4);
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (4, 5);
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (5, 2);
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (5, 3);
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (5, 8);
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (6, 7);
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (6, 6);
INSERT INTO flight_crew (flight_id, crew_member_id) VALUES (6, 9);