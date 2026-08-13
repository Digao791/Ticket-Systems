ALTER TABLE tickets
ADD COLUMN status VARCHAR(30);

UPDATE tickets
SET status = 'OPEN'
WHERE status is NULL;

ALTER TABLE tickets
ALTER COLUMN status SET NOT NULL;

ALTER TABLE tickets
ADD CONSTRAINT chk_ticket_status
CHECK (
    status in ('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED')
)
