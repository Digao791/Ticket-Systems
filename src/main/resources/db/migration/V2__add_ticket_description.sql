ALTER TABLE tickets
ADD COLUMN description TEXT;

UPDATE tickets
SET description = 'Descrição ainda não informada.'
WHERE description IS NULL;

ALTER TABLE tickets
ALTER COLUMN description SET NOT NULL;