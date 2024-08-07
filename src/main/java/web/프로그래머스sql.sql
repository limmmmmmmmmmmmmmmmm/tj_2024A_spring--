/*프로그래머스 sql 문제*/
#1. 인기있는 아이스크림
SELECT FLAVOR from FIRST_HALF order by TOTAL_ORDER desc, SHIPMENT_ID asc;
#2. 모든 레코드 조회하기
SELECT * from ANIMAL_INS order by ANIMAL_ID ;
#3. 역순 정렬하기
SELECT NAME, DATETIME from ANIMAL_INS order by ANIMAL_ID desc;
#4. 동물의 아이디와 이름
SELECT ANIMAL_ID, NAME from ANIMAL_INS order by ANIMAL_ID;
#5. 여러 기준으로 정렬하기
SELECT ANIMAL_ID , NAME, DATETIME from ANIMAL_INS order by NAME asc, DATETIME desc ;
#6. 과일로 만든 아이스크림 고르기
SELECT FIRST_HALF.FLAVOR FROM FIRST_HALF
JOIN ICECREAM_INFO ON FIRST_HALF.FLAVOR = ICECREAM_INFO.FLAVOR
WHERE FIRST_HALF.TOTAL_ORDER > 3000 AND ICECREAM_INFO.INGREDIENT_TYPE = 'fruit_based';

#7. 조건에 부합하는 중고거래 댓글 조회하기 - X

#8. 강원도에 위치한 생산공장 목록 출력하기
SELECT  FACTORY_ID, FACTORY_NAME, ADDRESS from FOOD_FACTORY
WHERE ADDRESS LIKE '강원%' order by FACTORY_ID;
#9. 아픈 동물 찾기
SELECT ANIMAL_ID , NAME from ANIMAL_INS where INTAKE_CONDITION = 'Sick' order by ANIMAL_ID ;
#10. 어린 동물 찾기
SELECT ANIMAL_ID , NAME from ANIMAL_INS where INTAKE_CONDITION != 'Aged' order by ANIMAL_ID;
#11. 상위 n개 레코드
SELECT NAME from ANIMAL_INS order by DATETIME limit 1;
#12. Python 개발자 찾기
select ID,	EMAIL,	FIRST_NAME	,LAST_NAME from DEVELOPER_INFOS where  SKILL_1 = 'Python' or  SKILL_2 = 'Python' or SKILL_3 = 'Python' order by ID;

========================================================================================================================
#1. 가장 비싼 상품 구하기
SELECT max(PRICE) as MAX_PRICE from PRODUCT
#2.최댓값 구하기
SELECT max(DATETIME) as 시간 from ANIMAL_INS
#3. 가격이 제일 비싼 식품의 정보 출력하기
SELECT PRODUCT_ID,PRODUCT_NAME,PRODUCT_CD,CATEGORY,PRICE from FOOD_PRODUCT order by PRICE desc limit 1;
#4.최솟값 구하기
SELECT min(DATETIME) as 시간 from ANIMAL_INS
#5. 동물 수 구하기
SELECT count(*) from ANIMAL_INS ;
#6.중복 제거하기
SELECT count(distinct name) as count from ANIMAL_INS where NAME is not null ;
# 7.[ SUM, MAX, MIN ] 조건에 맞는 아이템들의 가격의 총합 구하기
select sum(PRICE) as TOTAL_PRICE from ITEM_INFO where RARITY = 'LEGEND';

# 8. [SELECT] 조건에 맞는 회원수 구하기
SELECT count(*) as user from USER_INFO where JOINED
between '2021-01-01' and '2021-12-31' and AGE>=20 and AGE<=29;

# 9. [SELECT] 잔챙이 잡은 수 구하기
select count(*) as FISH_COUNT from FISH_INFO where LENGTH is null ;

# 10. [SELECT] 특정 형질을 가지는 대장균 찾기
select count(bin(GENOTYPE)) as COUNT from ECOLI_DATA
where  (bin(GENOTYPE)&4 = 4  or bin(GENOTYPE)&1 = 1) and bin(GENOTYPE)&2 != 2

# 11. [GROUP BY]자동차 종류 별 특정 옵션이 포함된 자동차 수 구하기
SELECT CAR_TYPE , count(CAR_TYPE) as CARS from CAR_RENTAL_COMPANY_CAR
where OPTIONS like '%통풍시트%' or OPTIONS like'%열선시트%'or OPTIONS like'%가죽시트%' group by CAR_TYPE order by CAR_TYPE asc ;

# 12. [GROUP BY]성분으로 구분한 아이스크림 총 주문량
SELECT i.INGREDIENT_TYPE , sum(f.TOTAL_ORDER) as TOTAL_ORDER from FIRST_HALF f
inner join ICECREAM_INFO i
on f.FLAVOR = i.FLAVOR
group by i.INGREDIENT_TYPE;

# 13. [GROUP BY]진료과별 총 예약 횟수 출력하기
SELECT MCDP_CD as 진료과코드, count(APNT_YMD) as 5월예약건수 from APPOINTMENT
where APNT_YMD like '2022-05-%' group by MCDP_CD order by count(APNT_YMD) asc , MCDP_CD asc;

# 14. [GROUP BY]고양이와 개는 몇 마리 있을까
SELECT ANIMAL_TYPE, count(*) from ANIMAL_INS group by ANIMAL_TYPE order by ANIMAL_TYPE;

# 15. [GROUP BY]동명 동물 수 찾기
SELECT NAME , count(NAME) as COUNT from ANIMAL_INS  group by NAME having count(NAME) >= 2 order by NAME asc ;

# 16. [GROUP BY]조건에 맞는 사원 정보 조회하기
select  sum(g.SCORE) as SCORE, g.EMP_NO , e.EMP_NAME, e.POSITION, e.EMAIL
from HR_GRADE g inner join HR_EMPLOYEES e on g.EMP_NO = e.EMP_NO
group by g.EMP_NO order by sum(g.SCORE) desc limit 1;

# 17. [GROUP BY]물고기 종류 별 잡은 수 구하기
select count(*) as FISH_COUNT, month(TIME) as MONTH from FISH_INFO
group by MONTH order by MONTH

# 18. [JOIN]상품 별 오프라인 매출 구하기
SELECT p.PRODUCT_CODE, sum(p.PRICE * o.SALES_AMOUNT) as SALES from PRODUCT p
inner join OFFLINE_SALE o on p.PRODUCT_ID = o.PRODUCT_ID
group by p.PRODUCT_CODE order by SALES desc , p.PRODUCT_CODE asc

