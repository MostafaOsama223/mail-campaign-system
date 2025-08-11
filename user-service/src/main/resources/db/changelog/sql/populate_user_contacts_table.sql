INSERT INTO public.user_contacts(email)
    SELECT 'user-' || gen_random_uuid() || '@eg.com'
    FROM generate_series(1, 2000000) AS s(i);