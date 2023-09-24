insert into shop
values (1, 'VALID', '집밥뚝딱 철산점');
insert into product
values (1, '부대찌개', 15800, 1);
insert into product
values (2, '존슨탕', 19600, 1);
insert into product
values (3, '소불고기 샤브샤브', 13200, 1);
insert into reservable_time
values (1, '06:30');
insert into reservable_time
values (1, '09:30');
insert into reservable_time
values (1, '12:30');
insert into reservable_time
values (1, '18:30');


insert into shop
values (2, 'VALID', '집밥뚝딱 안양점');
insert into product
values (4, '비비고 만두', 3200, 2);
insert into product
values (5, '토마토 훠궈', 22000, 2);
insert into product
values (6, '닭가슴살 샐러드', 6600, 2);
insert into reservable_time
values (2, '12:30');
insert into reservable_time
values (2, '19:30');

insert into shop
values (3, 'VALID', '집밥뚝딱 숭실대입구점');
insert into product
values (7, '왕만두', 4900, 3);
insert into product
values (8, '토마토 파스타', 8800, 3);
insert into product
values (9, '알리오올리오 파스타', 9900, 3);
insert into reservable_time
values (3, '16:30');
insert into reservable_time
values (3, '18:30');
insert into reservable_time
values (3, '20:00');
insert into reservable_time
values (3, '23:30');


insert into shop
values (4, 'VALID', '집밥뚝딱 다산점');
insert into product
values (10, '김치찌개', 15800, 4);
insert into reservable_time
values (4, '16:30');
insert into reservation (id, shop_id, reserve_at, reservation_status)
values ('c1e170e4-57e4-4d7d-8c10-6f4a8c658020', 4, '2023-09-22T16:30:00', 'NOTPAID');
