-- Schema definition for the widgets and gadgets

-- Drop the tables if they exist
drop table widgets;
drop table gadgets;

-- Widgets
create table widgets (id integer not null primary key, description varchar(45), price numeric(6,2), gears integer, sprockets integer);

-- Gadgets
create table gadgets (id integer not null primary key, description varchar(45), price numeric(6,2), cylinders integer);

