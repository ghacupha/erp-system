--liquibase formatted sql
--changeset edwin.njeru:1730193622763-9
CREATE OR REPLACE FUNCTION update_modified_at()
RETURNS TRIGGER AS '
    BEGIN
        NEW.modified_at = current_timestamp;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE TRIGGER set_modified_at
    BEFORE UPDATE ON transaction_details
    FOR EACH ROW
EXECUTE FUNCTION update_modified_at();


