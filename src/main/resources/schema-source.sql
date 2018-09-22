create table user_profile
(userid varchar(50) not null,
profile varchar(50) not null,
PRIMARY kEY (userid)
);

create table profile_details
(profile varchar(50) not null,
role_name varchar(20) ,
role_id BIGINT not null,
PRIMARY kEY (profile)
);

--insert into user_profile values('test@gmail.com','admin');
--insert into profile_details values('admin','test_role',1);