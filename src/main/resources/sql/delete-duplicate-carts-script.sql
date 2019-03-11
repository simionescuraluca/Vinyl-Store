            DELETE
            t1.*
            FROM
            cart t1,
            cart t2
            WHERE
            t1.id < t2.id
                    AND t1.user_id = t2.user_id
                    AND t1.id != t2.id;