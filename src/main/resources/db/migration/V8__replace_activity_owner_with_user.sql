ALTER TABLE ticket_activities
ADD COLUMN old_owner_id BIGINT;

ALTER TABLE ticket_activities
ADD COLUMN new_owner_id BIGINT;

UPDATE ticket_activities a
SET old_owner_id = u.id
FROM users u
WHERE u.name = a.old_owner;

UPDATE ticket_activities a
SET new_owner_id = u.id
FROM users u
WHERE u.name = a.new_owner;

ALTER TABLE ticket_activities
ADD CONSTRAINT fk_activities_new_owner
FOREIGN KEY (new_owner_id)
REFERENCES users(id);

ALTER TABLE ticket_activities
ADD CONSTRAINT fk_activities_old_owner
FOREIGN KEY (old_owner_id)
REFERENCES users(id);

ALTER TABLE ticket_activities
DROP COLUMN old_owner;

ALTER TABLE ticket_activities
DROP COLUMN new_owner;