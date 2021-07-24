insert into guest(id, name) values(null, 'Roger Federer');
insert into guest(id, name) values(null, 'Rafael Nadal');

insert into tennis_court(id, name) values(null, 'Roland Garros - Court Philippe-Chatrier');
insert into tennis_court(id, name) values(null, 'Roland Garros - Court Philippe-Trier');
insert into tennis_court(id, name) values(null, 'Roland Garros - Court Philippe-Doier');
insert into tennis_court(id, name) values(null, 'Roland Garros - Court Philippe-Cutrier');

insert
    into
        schedule
        (id, start_date_time, end_date_time, tennis_court_id)
    values
        (null, '2020-12-20T20:00:00.0', '2020-12-20T21:00:00.0', 1);
insert
    into
        schedule
        (id, start_date_time, end_date_time, tennis_court_id)
    values
        (null, '2020-12-04T19:00:00.0', '2020-12-04T20:00:00.0', 1);
insert
    into
        schedule
        (id, start_date_time, end_date_time, tennis_court_id)
    values
        (null, '2020-12-20T18:00:00.0', '2020-12-20T19:00:00.0', 2);
insert
    into
        schedule
        (id, start_date_time, end_date_time, tennis_court_id)
    values
        (null, '2020-12-20T21:00:00.0', '2020-12-20T22:00:00.0', 1);
insert
    into
        schedule
        (id, start_date_time, end_date_time, tennis_court_id)
    values
        (null, '2020-12-20T20:00:00.0', '2020-12-20T21:00:00.0', 2);