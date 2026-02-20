-- SQL script to create the 'pokemon' database and tables for Hibernate
CREATE DATABASE IF NOT EXISTS pokemon DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pokemon;

-- Table for Trainer
CREATE TABLE IF NOT EXISTS trainer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    money DOUBLE,
    activePokemon_id BIGINT,
    currentArea_id BIGINT
);

-- Table for Badge
CREATE TABLE IF NOT EXISTS badge (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255)
);

-- Table for Inventory
CREATE TABLE IF NOT EXISTS inventory (
    id BIGINT PRIMARY KEY AUTO_INCREMENT
    -- Add more fields as needed for inventory items
);

-- Table for Pokemon
CREATE TABLE IF NOT EXISTS pokemon (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    owner_id BIGINT,
    currentHp INT,
    maxHp INT,
    level INT,
    xpToNext INT,
    currentXp INT
    -- Add more fields as needed for PokemonData, etc.
);

-- Table for Area
CREATE TABLE IF NOT EXISTS area (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    isUnlocked BOOLEAN,
    gymLeader_id BIGINT,
    nextArea_id BIGINT
);

-- Foreign keys (add as needed)
ALTER TABLE trainer ADD CONSTRAINT fk_trainer_activePokemon FOREIGN KEY (activePokemon_id) REFERENCES pokemon(id);
ALTER TABLE trainer ADD CONSTRAINT fk_trainer_currentArea FOREIGN KEY (currentArea_id) REFERENCES area(id);
ALTER TABLE pokemon ADD CONSTRAINT fk_pokemon_owner FOREIGN KEY (owner_id) REFERENCES trainer(id);
ALTER TABLE area ADD CONSTRAINT fk_area_gymLeader FOREIGN KEY (gymLeader_id) REFERENCES trainer(id);
ALTER TABLE area ADD CONSTRAINT fk_area_nextArea FOREIGN KEY (nextArea_id) REFERENCES area(id);

