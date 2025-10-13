--liquibase formatted sql
--changeset edwin.njeru:1730193622763-10
CREATE OR REPLACE FUNCTION set_created_at()
    RETURNS TRIGGER AS '
    BEGIN
        IF NEW.created_at IS NULL THEN
            NEW.created_at = current_timestamp;
        END IF;
        RETURN NEW;
    END;
' LANGUAGE plpgsql;

CREATE TRIGGER set_created_at
    BEFORE INSERT ON transaction_details
    FOR EACH ROW
EXECUTE FUNCTION set_created_at();
