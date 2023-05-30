ALTER TABLE tbl_cart
    ADD CONSTRAINT fk_delete_cart
        FOREIGN KEY (user_id)
            REFERENCES tbl_user(id)
            ON DELETE CASCADE;

ALTER TABLE tbl_user
    ADD CONSTRAINT fk_delete_user
        FOREIGN KEY (role_id)
            REFERENCES tbl_role (id)
            ON DELETE CASCADE;