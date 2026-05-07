-- ============================================================
--  FEDERATION AGRICOLE — Version Épurée (2026)
-- ============================================================

-- 1. TYPES ENUM
CREATE TYPE gender AS ENUM ('MALE', 'FEMALE');
CREATE TYPE member_occupation AS ENUM (
    'JUNIOR', 'VICE_PRESIDENT', 'TREASURER', 'SECRETARY', 'SENIOR', 'PRESIDENT'
);

-- 2. TABLE COLLECTIVITE
-- Regroupe les infos de l'entité locale et sa santé financière
CREATE TABLE collectivite (
                              id                SERIAL PRIMARY KEY,
                              nom               VARCHAR(150) NOT NULL,
                              date_creation     DATE NOT NULL DEFAULT CURRENT_DATE,
                              activite          TEXT,
                              solde_compte      DECIMAL(15, 2) DEFAULT 0.00,
                              federation_approval BOOLEAN DEFAULT FALSE,
                              created_at        TIMESTAMP DEFAULT NOW()
);

-- 3. TABLE MEMBRE
-- Élagage : suppression de l'adresse et de la profession si non critiques
CREATE TABLE membre (
                        id                    VARCHAR,
                        collectivity_id       INTEGER REFERENCES collectivite(id) ON DELETE SET NULL,
                        first_name            VARCHAR(100) NOT NULL,
                        last_name             VARCHAR(100) NOT NULL,
                        birth_date            DATE,
                        gender                gender,
                        phone_number          VARCHAR(20),
                        email                 VARCHAR(150),
                        occupation            member_occupation NOT NULL,
    -- Statuts financiers regroupés ou simplifiés
                        has_paid_fees         BOOLEAN DEFAULT FALSE,
                        created_at            TIMESTAMP DEFAULT NOW()
);

CREATE TABLE membre_referees (
    -- On remplace INTEGER par VARCHAR(50) pour correspondre à la table membre
                                 member_id   VARCHAR(50) REFERENCES membre(id) ON DELETE CASCADE,
                                 referee_id  VARCHAR(50) REFERENCES membre(id) ON DELETE CASCADE,
                                 PRIMARY KEY (member_id, referee_id),
                                 CONSTRAINT no_self_referral CHECK (member_id <> referee_id)
);

CREATE TABLE collectivite_membres (
                                      collectivity_id  INTEGER REFERENCES collectivite(id) ON DELETE CASCADE,
                                      member_id        VARCHAR(50) REFERENCES membre(id) ON DELETE CASCADE,
                                      PRIMARY KEY (collectivity_id, member_id)
);

