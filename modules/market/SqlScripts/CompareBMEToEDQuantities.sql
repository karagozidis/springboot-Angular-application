SELECT
s.id,
s.quantity as InitialQuantity ,
s.current_quantity as ED_current_quantity,
b.quantity as BME_current_quantity,
IFNULL(s.quantity,0) - IFNULL((SELECT SUM(m.quantity) from matched_order m  WHERE  m.single_order1_id = s.id or m.single_order2_id = s.id ),0)
as ED_calculated_quantity_by_db_matchings,
IFNULL((SELECT SUM(m.quantity) from matched_order m  WHERE  m.single_order1_id = s.id or m.single_order2_id = s.id ),0) as ED_quantity_in_matchings

FROM single_order s
LEFT OUTER JOIN bmeorder_book_entry b on b.order_id = s.id
/*WHERE s.id in (SELECT distinct m.single_order1_id from matched_order m )
or s.id in (SELECT distinct m.single_order2_id from matched_order m ) */
