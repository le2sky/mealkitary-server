insert into shop
values ('e9e3fcaf-e665-4b95-8e52-958c8f6c8163', '경기도', '광명시', '철산동', '철산로', '1234567890', 30.03, 50.05, '123-12-12345',
        'VALID', '집밥뚝딱 철산점');
insert into product
values (1, '부대찌개', 15800, 'e9e3fcaf-e665-4b95-8e52-958c8f6c8163');
insert into product
values (2, '존슨탕', 19600, 'e9e3fcaf-e665-4b95-8e52-958c8f6c8163');
insert into product
values (3, '소불고기 샤브샤브', 13200, 'e9e3fcaf-e665-4b95-8e52-958c8f6c8163');
insert into reservable_time
values ('e9e3fcaf-e665-4b95-8e52-958c8f6c8163', '06:30');
insert into reservable_time
values ('e9e3fcaf-e665-4b95-8e52-958c8f6c8163', '09:30');
insert into reservable_time
values ('e9e3fcaf-e665-4b95-8e52-958c8f6c8163', '12:30');
insert into reservable_time
values ('e9e3fcaf-e665-4b95-8e52-958c8f6c8163', '18:30');


insert into shop
values ('f8445ab4-2ede-4db2-b9de-9fb21cffa2d2', '경기도', '안양시', '동안구', '경수대로', '1234567890', 30.03, 50.05, '123-12-12345',
        'VALID', '집밥뚝딱 안양점');
insert into product
values (4, '비비고 만두', 3200, 'f8445ab4-2ede-4db2-b9de-9fb21cffa2d2');
insert into product
values (5, '토마토 훠궈', 22000, 'f8445ab4-2ede-4db2-b9de-9fb21cffa2d2');
insert into product
values (6, '닭가슴살 샐러드', 6600, 'f8445ab4-2ede-4db2-b9de-9fb21cffa2d2');
insert into reservable_time
values ('f8445ab4-2ede-4db2-b9de-9fb21cffa2d2', '12:30');
insert into reservable_time
values ('f8445ab4-2ede-4db2-b9de-9fb21cffa2d2', '19:30');

insert into shop
values ('b1f1c9a0-20ac-43e9-9126-384a7fb19ff9', '서울시', '동작구', '상도동', '', '1234567890', 30.03, 50.05, '123-12-12345',
        'VALID', '집밥뚝딱 숭실대입구점');
insert into product
values (7, '왕만두', 4900, 'b1f1c9a0-20ac-43e9-9126-384a7fb19ff9');
insert into product
values (8, '토마토 파스타', 8800, 'b1f1c9a0-20ac-43e9-9126-384a7fb19ff9');
insert into product
values (9, '알리오올리오 파스타', 9900, 'b1f1c9a0-20ac-43e9-9126-384a7fb19ff9');
insert into reservable_time
values ('b1f1c9a0-20ac-43e9-9126-384a7fb19ff9', '16:30');
insert into reservable_time
values ('b1f1c9a0-20ac-43e9-9126-384a7fb19ff9', '18:30');
insert into reservable_time
values ('b1f1c9a0-20ac-43e9-9126-384a7fb19ff9', '20:00');
insert into reservable_time
values ('b1f1c9a0-20ac-43e9-9126-384a7fb19ff9', '23:30');


insert into shop
values ('e390fc56-1e06-435a-93a4-1157db489a9d', '경기도', '남양주시', '다산동', '', '1234567890', 30.03, 50.05, '123-12-12345',
        'VALID', '집밥뚝딱 다산점');
insert into product
values (10, '김치찌개', 15800, 'e390fc56-1e06-435a-93a4-1157db489a9d');
insert into reservable_time
values ('e390fc56-1e06-435a-93a4-1157db489a9d', '16:30');
insert into reservation (id, shop_id, reserve_at, reservation_status)
values ('c1e170e4-57e4-4d7d-8c10-6f4a8c658020', 'e390fc56-1e06-435a-93a4-1157db489a9d', '2023-09-22T16:30:00',
        'NOTPAID');
