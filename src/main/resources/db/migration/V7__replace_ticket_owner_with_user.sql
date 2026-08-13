ALTER TABLE tickets
ADD COLUMN owner_id BIGINT;

UPDATE tickets
SET owner_id = (
    SELECT id
    FROM users
    WHERE email = 'admin@tickets.local'
    LIMIT 1
)
WHERE owner_id IS NULL;

ALTER TABLE tickets
ALTER COLUMN owner_id SET NOT NULL;

ALTER TABLE tickets
ADD CONSTRAINT fk_ticket_owner
FOREIGN KEY (owner_id)
REFERENCES users(id);

ALTER TABLE tickets
DROP COLUMN owner;