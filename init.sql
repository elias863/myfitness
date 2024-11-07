CREATE TABLE IF NOT EXISTS esercizi (
    id SERIAL PRIMARY KEY,
    data DATE NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    durata TIME NOT NULL,
    calorie INTEGER NOT NULL,
    distanza FLOAT
);

CREATE TABLE IF NOT EXISTS parametri (
    id SERIAL PRIMARY KEY,
    data DATE NOT NULL,
    peso DECIMAL(5, 2) NOT NULL,
    sonno TIME NOT NULL,
    bpm INTEGER NOT NULL,
    ossigenazione INTEGER NOT NULL
);