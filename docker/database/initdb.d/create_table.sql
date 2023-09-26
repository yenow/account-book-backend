create table common_code
(
    code              varchar(100)                              not null comment '코드' primary key,
    parent_code       varchar(100)                              null comment '부모 코드',
    code_name         varchar(200)                              not null comment '코드명',
    description       varchar(1000)                             null comment '설명',
    is_use            tinyint(1)   default 1                    not null comment '사용여부',
    order_number      int                                       null comment '정렬 순번',
    creation_date     timestamp(6) default current_timestamp(6) not null comment '생성날짜',
    modification_date timestamp(6)                              null comment '수정날짜',
    is_delete         tinyint(1)   default 0                    null comment '삭제여부',
    constraint fk__common_code__parent_code__common_code__code foreign key (parent_code) references common_code (code)
);

create table bank_account
(
    bank_account_id     bigint auto_increment comment '은행 계좌 ID' primary key,
    bank_code           varchar(100)                              not null comment '은행 코드',
    bank_account_number varchar(100)                              null comment '계좌번호',
    creation_date       timestamp(6) default current_timestamp(6) not null comment '생성날짜',
    modification_date   timestamp(6)                              null comment '수정날짜',
    is_delete           tinyint(1)   default 0                    null comment '삭제여부',
    constraint fk__bank_account__bank_code__common_code__code foreign key (bank_code) references common_code (code)
);

create index bank_account_bank_code_index on bank_account (bank_code);

create table card
(
    card_id           bigint auto_increment comment '카드 ID' primary key,
    bank_account_id   bigint                                    not null comment '은행 계좌 ID',
    card_type         varchar(30)                               not null comment '카드타입(선불카드/신용카드)',
    card_number       varchar(100)                              null comment '카드번호',
    creation_date     timestamp(6) default current_timestamp(6) not null comment '생성날짜',
    modification_date timestamp(6)                              null comment '수정날짜',
    is_delete         tinyint(1)   default 0                    null comment '삭제여부',
    constraint fk__card__bank_account_id__bank_account__bank_account_id foreign key (bank_account_id) references bank_account (bank_account_id)
);

create index card_bank_account_id_index on card (bank_account_id);

create table message
(
    keyword varchar(500) not null comment '키워드' primary key,
    en      varchar(500) not null comment '영문',
    kr      varchar(500) not null comment '한글'
);

create table notify
(
    notify_id         bigint auto_increment comment '공지사항 ID' primary key,
    title             varchar(1000)                             null comment '제목',
    content           mediumtext                                null comment '내용',
    creation_date     timestamp(6) default current_timestamp(6) not null comment '생성날짜',
    modification_date timestamp(6)                              null comment '수정날짜',
    is_delete         tinyint(1)   default 0                    null comment '삭제여부'
);

create table trade_date
(
    trade_date varchar(8) not null comment '일자' primary key
);

create table user
(
    user_id           bigint auto_increment comment '사용자 ID' primary key,
    email             varchar(100)                              null comment '이메일',
    name              varchar(200)                              null comment '이름',
    role              varchar(100)                              null comment '권한',
    refresh_token     varchar(500)                              null,
    is_activated      tinyint(1)   default 1                    not null comment '이용가능여부',
    creation_date     timestamp(6) default current_timestamp(6) not null comment '생성날짜',
    modification_date timestamp(6)                              null comment '수정날짜',
    is_delete         tinyint(1)   default 0                    null comment '삭제여부',
    constraint user_email_uindex unique (email)
);

create table account
(
    account_id        bigint auto_increment comment '계정 ID' primary key,
    parent_account_id bigint                                    null comment '부모 계정 ID',
    user_id           bigint                                    not null comment '사용자 ID',
    account_type      varchar(10)                               not null comment '계정타입(자산,부채,수익,비용,자본)',
    account_name      varchar(255)                              not null comment '계정과목',
    description       varchar(3000)                             null comment '설명',
    level             int(2)                                    null comment '트리구조 깊이',
    is_leaf           tinyint(1)   default 0                    not null comment 'leaf 여부',
    can_be_deleted    tinyint(1)   default 1                    not null comment '계정 삭제 가능 여부',
    creation_date     timestamp(6) default current_timestamp(6) not null comment '생성날짜',
    modification_date timestamp(6)                              null comment '수정날짜',
    is_delete         tinyint(1)   default 0                    null comment '삭제여부',
    constraint fk__account__parent_account_id__account__account_id foreign key (parent_account_id) references account (account_id) on update cascade on delete cascade,
    constraint fk__account__user_id_references__user__user_id foreign key (user_id) references user (user_id) on update cascade on delete cascade
) comment '계정';

create index account_account_type_index on account (account_type);

create index account_user_id_index on account (user_id);

create table trade
(
    trade_id          bigint auto_increment comment '거래 ID' primary key,
    trade_date        varchar(8)                                not null comment '거래 날짜',
    user_id           bigint                                    not null comment '사용자 ID',
    trade_type        varchar(10)                               not null comment '거래유형',
    content           varchar(200)                              null comment '거래 내용',
    memo              varchar(3000)                             null comment '메모',
    creation_date     timestamp(6) default current_timestamp(6) not null comment '생성날짜',
    modification_date timestamp(6)                              null comment '수정날짜',
    is_delete         tinyint(1)   default 0                    null comment '삭제여부',
    constraint fk__trade__trade_date__trade_date__trade_date foreign key (trade_date) references trade_date (trade_date),
    constraint fk__trade__user_id__user__user_id foreign key (user_id) references user (user_id) on update cascade on delete cascade
);

create index trade_trade_date_index on trade (trade_date);

create index trade_user_id_index on trade (user_id);

create table trade_detail
(
    trade_detail_id   bigint auto_increment comment '거래 상세 ID' primary key,
    trade_id          bigint                                    not null comment '거래 ID',
    account_id        bigint                                    not null comment '계정 ID',
    debit_and_credit  varchar(10)                               not null comment '차변/대변',
    amount            bigint                                    not null comment '금액',
    memo              varchar(200) default ''                   null comment '적요(메모)',
    creation_date     timestamp(6) default current_timestamp(6) not null comment '생성날짜',
    modification_date timestamp(6)                              null comment '수정날짜',
    is_delete         tinyint(1)   default 0                    null comment '삭제여부',
    constraint fk__trade_detail__account_id__account__account_id foreign key (account_id) references account (account_id) on update cascade on delete cascade,
    constraint fk__trade_detail__trade_id__trade__trade_id foreign key (trade_id) references trade (trade_id)
);

create table trade_bank_account
(
    trade_detail_id   bigint                                    not null,
    bank_account_id   bigint                                    not null,
    creation_date     timestamp(6) default current_timestamp(6) not null comment '생성날짜',
    modification_date timestamp(6)                              null comment '수정날짜',
    is_delete         tinyint(1)   default 0                    null comment '삭제여부',
    primary key (trade_detail_id, bank_account_id),
    constraint fk__bank_account_id__bank_account__bank_account_id foreign key (bank_account_id) references bank_account (bank_account_id),
    constraint fk__trade_detail_id__bank_account__bank_account_id foreign key (trade_detail_id) references trade_detail (trade_detail_id)
);

create index trade_detail_account_id_index on trade_detail (account_id);

create index trade_detail_trade_id_index on trade_detail (trade_id);

create function convert_keyword(p_keyword varchar(200), language_type enum ('kr', 'en')) returns varchar(500)
begin

    declare exit handler for not found begin
        return '';
    end;

    return (
        select if(language_type = 'kr', kr, en)
        from message
        where keyword = p_keyword
    );

end;

create procedure expense(in p_user_id bigint, in p_amount bigint, in p_account_name varchar(255), in p_expense_account_name varchar(255), in p_trade_date varchar(255), in p_content varchar(255),
                         out p_result varchar(255))
body :
begin
    declare v_account_id bigint default 0;
    declare v_expense_account_id bigint default 0;
    declare v_last_insert_id bigint default 0;

    set v_account_id = (
        select account_id from account where user_id = p_user_id and account_name = p_account_name and account_type = 'asset'
    );
    set v_expense_account_id = (
        select account_id
        from account
        where user_id = p_user_id and account_name = p_expense_account_name and account_type = 'expense'
    );

    if v_account_id is null or v_expense_account_id is null then
        select '부모 계정과목 없음';
        set p_result = 'fail';
        leave body;
    end if;

    insert into trade (trade_date, user_id, content, trade_type) values (p_trade_date, p_user_id, p_content, 'expense');
    set v_last_insert_id = (
        select last_insert_id()
    );

    insert into trade_detail (trade_id, account_id, debit_and_credit, amount, memo) values (v_last_insert_id, v_account_id, 'credit', p_amount, '');

    insert into trade_detail (trade_id, account_id, debit_and_credit, amount, memo)
    values (v_last_insert_id, v_expense_account_id, 'debit', p_amount, '');

    set p_result = 'success';
end;

create procedure init_account(in p_user_id bigint)
begin

    declare last_insert_id bigint default 0;

    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'asset', '현금성 자산', 1, false);
    set last_insert_id = (
        select last_insert_id()
    );
    insert into account (user_id, parent_account_id, account_type, account_name, level, is_leaf) values (p_user_id, last_insert_id, 'asset', '현금', 2, true);
    insert into account (user_id, parent_account_id, account_type, account_name, level, is_leaf) values (p_user_id, last_insert_id, 'asset', '보통예금', 2, true);
    insert into account (user_id, parent_account_id, account_type, account_name, level, is_leaf) values (p_user_id, last_insert_id, 'asset', '상품권', 2, true);
    insert into account (user_id, parent_account_id, account_type, account_name, level, is_leaf) values (p_user_id, last_insert_id, 'asset', '선불카드', 2, true);


    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'asset', '투자자산', 1, false);
    set last_insert_id = (
        select last_insert_id()
    );
    insert into account (user_id, parent_account_id, account_type, account_name, level, is_leaf) values (p_user_id, last_insert_id, 'asset', '정기적금', 2, true);
    insert into account (user_id, parent_account_id, account_type, account_name, level, is_leaf) values (p_user_id, last_insert_id, 'asset', '정기예금', 2, true);
    insert into account (user_id, parent_account_id, account_type, account_name, level, is_leaf) values (p_user_id, last_insert_id, 'asset', '주식', 2, true);
    insert into account (user_id, parent_account_id, account_type, account_name, level, is_leaf) values (p_user_id, last_insert_id, 'asset', '펀드', 2, true);


    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'asset', '기타자산', 1, false);
    set last_insert_id = (
        select last_insert_id()
    );
    insert into account (user_id, parent_account_id, account_type, account_name, level, is_leaf) values (p_user_id, last_insert_id, 'asset', '건물', 2, true);
    insert into account (user_id, parent_account_id, account_type, account_name, level, is_leaf) values (p_user_id, last_insert_id, 'asset', '퇴직연금', 2, true);


    #     /* 자산 */
    # #     insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'asset', '현금성 자산',2, true);
    #     insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'asset', '현금',2, true);
    #     insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'asset', '보통예금',2, true);
    #     insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'asset', '상품권',2, true);
    #     insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'asset', '투자자산',2, true);
    #     insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'asset', '정기적금',2, true);
    #     insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'asset', '정기예금',2, true);
    #     insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'asset', '주식',2, true);
    #     insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'asset', '펀드',2, true);
    #     insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'asset', '기타자산',2, true);
    #     insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'asset', '건물',2, true);
    #     insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'asset', '퇴직연금',2, true);


    /* 부채 */
    #     insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'debt', '유동부채',2, true);
    #     insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'debt', '미지급금',2, true);
    #     insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'debt', '선수금',2, true);


    /* 수입 */
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'income', '주수입', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'income', '급여', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'income', '상여금', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'income', '퇴직금', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'income', '기타수입', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'income', '이자수익', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'income', '주식수익', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'income', '기타수익', 2, true);


    /* 지출 */
    #     insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '생활비(변동지출)',2, true);

    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '식비(집밥)', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '식비(외식)', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '생활용품비', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '교통유류비', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '의류미용비', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '병원의료비', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '취미활동비', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '교육비', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '기타', 2, true);

    #     insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '공과금(고정지출)',2, true);

    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '전기세', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '가스비', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '관리비', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '월세', 2, true);

    #     insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '돌발지출(비정기지출)',2, true);

    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '카드대금', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '세금', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '경조사', 2, true);
    insert into account (user_id, account_type, account_name, level, is_leaf) values (p_user_id, 'expense', '명절비', 2, true);

end;

create procedure insert_account(in p_user_id bigint, in p_parent_account_name varchar(255), in p_account_name varchar(255), out p_result varchar(255))
body :
begin
    declare parent_account_id bigint default 0;
    declare account_count bigint default 0;
    declare parent_account_type varchar(10) default '';

    set account_count = (select account_id from account where user_id = p_user_id and account_name = p_account_name);
    set parent_account_id = (select account_id from account where user_id = p_user_id and account_name = p_parent_account_name);
    set parent_account_type = (select account_type from account where user_id = p_user_id and account_name = p_parent_account_name);

    if account_count >= 1 then
        select '이미 존재하는 계정과목';
        set p_result = 'fail';
        leave body;
    end if;

    if parent_account_id is null then
        select '부모 계정과목 없음';
        set p_result = 'fail';
        leave body;
    end if;

    insert into account (user_id, parent_account_id, account_type, account_name)
    values (p_user_id, parent_account_id, parent_account_type, p_account_name);
    set p_result = 'true';
end;

create procedure insert_common_code(in p_parent_code varchar(255), in p_code varchar(255), in p_code_name varchar(255), out p_result varchar(255))
body :
begin
    declare parent_code bigint default 0;
    declare cnt bigint default 0;

    set cnt = (
        select count(*)
        from common_code
        where code_name = p_code_name
    );
    set parent_code = (
        select code
        from common_code
        where code = p_parent_code
    );

    if cnt >= 1 then
        select '이미 존재하는 공통코드';
        set p_result = 'fail';
        leave body;
    end if;

    if parent_code is null then
        select '부모코드 없음';
        set p_result = 'fail';
        leave body;
    end if;

    insert into common_code (code, parent_code, code_name, description) values (p_code, parent_code, p_code_name, '');
    set p_result = 'true';
end;

create procedure insert_trade_date()
begin
    declare i int default 0;
    declare j int default 1;

    declare count int default 10000;
    while (i <= count)
        do
            if (select count(*)
                from trade_date
                where trade_date = date_format( date_sub(now(), interval i day ), '%Y%m%d')) = 0 then

                insert into trade_date(trade_date)
                values (date_format(date_sub(now(), interval i day), '%Y%m%d'));
            end if;

            set i = i + 1;
        end while;

    while (j <= count)
        do
            if (select count(*)
                from trade_date
                where trade_date = date_format(date_add(now(), interval j day), '%Y%m%d')) = 0 then

                insert into trade_date(trade_date)
                values (date_format(date_add(now(), interval j day), '%Y%m%d'));

            end if;

            set j = j + 1;
        end while;
end;

create procedure revenue(in p_user_id bigint, in p_amount bigint, in p_account_name varchar(255), in p_revenue_account_name varchar(255), in p_trade_date varchar(255), in p_content varchar(255),
                         out p_result varchar(255))
body :
begin
    declare v_account_id bigint default 0;
    declare v_revenue_account_id bigint default 0;
    declare v_last_insert_id bigint default 0;

    set v_account_id = (
        select account_id from account where user_id = p_user_id and account_name = p_account_name and account_type = 'asset'
    );
    set v_revenue_account_id = (
        select account_id
        from account
        where user_id = p_user_id and account_name = p_revenue_account_name and account_type = 'revenue'
    );

    if v_account_id is null or v_revenue_account_id is null then
        select '부모 계정과목 없음';
        set p_result = 'fail';
        leave body;
    end if;

    insert into trade (trade_date, user_id, content) values (p_trade_date, p_user_id, p_content);
    set v_last_insert_id = (
        select last_insert_id()
    );

    insert into trade_detail (trade_id, account_id, debit_and_credit, amount, memo) values (v_last_insert_id, v_account_id, 'debit', p_amount, '');

    insert into trade_detail (trade_id, account_id, debit_and_credit, amount, memo)
    values (v_last_insert_id, v_revenue_account_id, 'credit', p_amount, '');

    set p_result = 'success';
end;


create
    function convert_keyword(p_keyword varchar(200), language_type enum ('kr', 'en')) returns varchar(500)
body :
begin

    declare exit handler for not found begin
        return '';
end;

return (
    select if(language_type = 'kr', kr, en)
    from message
    where keyword = p_keyword
);

end;