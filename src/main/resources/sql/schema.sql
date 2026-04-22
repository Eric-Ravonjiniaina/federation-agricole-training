-- ============================================================
--  DONNÉES DE TEST (Séquentiel)
-- ============================================================

-- 1. Insertion des Collectivités
-- Les IDs seront automatiquement 1, 2 et 3
INSERT INTO collectivite (nom, date_creation, activite, solde_compte, federation_approval)
VALUES
    ('Union de l''Analamanga', '2025-01-10', 'Culture maraîchère et distribution', 1500000.00, TRUE),
    ('Coopérative du Vakinankaratra', '2025-03-15', 'Production laitière et élevage', 2800000.00, TRUE),
    ('Association de l''Est', '2026-02-01', 'Collecte de girofle et vanille', 0.00, FALSE);

-- 2. Insertion des Membres
-- On les rattache aux IDs des collectivités créées ci-dessus
INSERT INTO membre (collectivity_id, first_name, last_name, birth_date, gender, phone_number, email, occupation, has_paid_fees)
VALUES
    -- Membres de la Collectivité 1 (Analamanga)
    (1, 'Jean', 'Rakoto', '1980-05-20', 'MALE', '0341122233', 'jean.rakoto@mail.mg', 'PRESIDENT', TRUE),
    (1, 'Lova', 'Andrianina', '1992-11-12', 'FEMALE', '0324455566', 'lova.a@mail.mg', 'SECRETARY', TRUE),
    (1, 'Marc', 'Randria', '2005-06-30', 'MALE', '0337788899', NULL, 'JUNIOR', FALSE),

    -- Membres de la Collectivité 2 (Vakinankaratra)
    (2, 'Soa', 'Razafindrabe', '1975-08-14', 'FEMALE', '0348899900', 'soa.raza@mail.mg', 'PRESIDENT', TRUE),
    (2, 'Faly', 'Rasolofo', '1988-01-05', 'MALE', '0321122233', 'faly.ras@mail.mg', 'TREASURER', TRUE),

    -- Membre de la Collectivité 3 (Est)
    (3, 'Tiana', 'Raharo', '1995-12-25', 'MALE', '0339900011', 'tiana.raharo@mail.mg', 'PRESIDENT', FALSE);

-- 3. Insertion des Parrainages (Table membre_referees)
-- Supposons que Jean (ID 1) et Lova (ID 2) parrainent le jeune Marc (ID 3)
INSERT INTO membre_referees (member_id, referee_id)
VALUES
    (3, 1),
    (3, 2);

-- 4. Insertion dans la table de jointure collectivite_membres
-- (Optionnel si vous utilisez déjà la FK collectivity_id dans 'membre')
INSERT INTO collectivite_membres (collectivity_id, member_id)
VALUES
    (1, 1), (1, 2), (1, 3),
    (2, 4), (2, 5),
    (3, 6);