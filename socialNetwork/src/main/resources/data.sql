--users
insert into USERS(email, first_name, last_name, password, phone_number, username) values ('marko.markovic@gmail.com', 'Marko', 'Markovic', 'marko123','0654236178', 'marko.markovic')
insert into USERS(email, first_name, last_name, password, phone_number, username) values ('jovan.jovanovic@gmail.com', 'Jovan', 'Jovanovic', 'jovan123','065445278', 'jovan.jovanovic')

--friend requests
insert into FRIEND_REQUESTS(from_user_id, to_user_id, status) values (1, 2, 'ACCEPTED')

--friendships
insert into FRIENDSHIPS(friend_id, user_id) values (1, 2)
insert into FRIENDSHIPS(friend_id, user_id) values (2, 1)

--posts
insert into POSTS(posted_at, posted_by, description, media) values ('2024-03-21T09:00:00', 1, 'Description :)', 'First post')

--comments
insert into COMMENTS(commented_at, commented_by, post_id) values ('2024-03-21T09:30:00', 2, 1)