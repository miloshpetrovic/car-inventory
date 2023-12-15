INSERT INTO public.manufacturer (id, name, country, from_year, to_year) VALUES (1, 'Volvo', 'Sweden', 1927, null);
INSERT INTO public.manufacturer (id, name, country, from_year, to_year) VALUES (2, 'Toyota', 'Japan', 1936, null);
INSERT INTO public.manufacturer (id, name, country, from_year, to_year) VALUES (3, 'Ford', 'USA', 1903, null);
INSERT INTO public.manufacturer (id, name, country, from_year, to_year) VALUES (4, 'Renault', 'France', 1898, null);
INSERT INTO public.manufacturer (id, name, country, from_year, to_year) VALUES (5, 'BMW', 'Germany', 1927, null);

INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (1, 1, 'S40', 'Pickup', 'Diesel', 105);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (2, 1, 'S60', 'Sedan', 'Petrol', 115);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (3, 1, 'S70', 'SUV', 'Diesel', 125);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (4, 1, 'S80', 'Hatchback', 'Petrol', 135);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (5, 1, 'S90', 'Sedan', 'Diesel', 145);

INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (6, 2, 'Avensis', 'Sedan', 'Petrol', 105);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (7, 2, 'Corolla', 'Sedan', 'Diesel', 115);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (8, 2, 'Yaris', 'Hatchback', 'Petrol', 125);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (9, 2, 'Camry', 'Sedan', 'Diesel', 135);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (10, 2, 'RAV 4', 'SUV', 'Petrol', 145);

INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (11, 3, 'Fiesta', 'Sedan', 'Petrol', 105);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (12, 3, 'Focus', 'Hatchback', 'Diesel', 115);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (13, 3, 'Escort', 'Hatchback', 'Petrol', 125);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (14, 3, 'Orion', 'Sedan', 'Diesel', 135);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (15, 3, 'Capri', 'SUV', 'Petrol', 145);

INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (16, 4, 'Clio', 'Sedan', 'Petrol', 105);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (17, 4, 'Duster', 'Sedan', 'Diesel', 115);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (18, 4, 'Espace', 'Hatchback', 'Petrol', 125);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (19, 4, 'Laguna', 'Sedan', 'Diesel', 135);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (20, 4, 'Logan', 'SUV', 'Petrol', 145);

INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (21, 5, '518', 'Sedan', 'Petrol', 105);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (22, 5, '520', 'Hatchback', 'Diesel', 115);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (23, 5, '523', 'Hatchback', 'Petrol', 125);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (24, 5, '524', 'Sedan', 'Diesel', 135);
INSERT INTO public.model (id, manufacturer_id, name, body_type, fuel_type, power) VALUES (25, 5, '525', 'SUV', 'Petrol', 145);