-- WITH RECURSIVE DepreciationSeries AS (
--     SELECT generate_series(1, 35) AS period
-- ),
-- AssetDetails AS (
--     SELECT
--         ar.asset_cost as asset_cost,
--         cat.depreciation_rate_yearly / (10000 * 12) as monthly_depreciation_rate_decimal
--     FROM
--         public.asset_registration ar
--     LEFT JOIN asset_category cat ON ar.asset_category_id = cat.id
--     WHERE
--         ar.id = '65595'
--     ),
-- InitialDepreciation AS (
--    SELECT
--       asset_cost * (1 - AssetDetails.monthly_depreciation_rate_decimal) AS net_book_value
--       FROM AssetDetails
-- ),
-- DepreciationPerPeriod AS (
--     SELECT
--         depSer.period,
--         -- asset_cost * (1 - POWER((1 - yearly_depreciation_rate_decimal), generate_series(1, 35) )) AS depreciation_per_period
--         CASE
--             WHEN period = 1 THEN (SELECT net_book_value FROM InitialDepreciation)
--             ELSE LAG(net_book_value) OVER (ORDER BY period) * (1 - AssetDetails.monthly_depreciation_rate_decimal)
--             END AS depreciation_this_period,
--         net_book_value
--     FROM
--         AssetDetails, DepreciationSeries depSer
--     ),
-- AccruedDepreciation AS (
-- SELECT
--     sum(depreciation_this_period) AS accrued_depreciation_at_period_N
-- FROM
--     DepreciationPerPeriod
-- )
-- SELECT
--     asset_cost - accrued_depreciation_at_period_N AS net_book_value_at_period_N
-- FROM
--    AssetDetails, AccruedDepreciation;


-- WITH RECURSIVE DepreciationSeries AS (
--     SELECT generate_series(1, 35) AS period
-- ),
--    AssetDetails AS (
--        SELECT
--            ar.asset_cost as asset_cost,
--            cat.depreciation_rate_yearly / (10000 * 12) as monthly_depreciation_rate_decimal
--        FROM
--            public.asset_registration ar
--                LEFT JOIN asset_category cat ON ar.asset_category_id = cat.id
--        WHERE
--                ar.id = '65595'
--    ),
--    InitialDepreciation AS (
--        SELECT
--                asset_cost * (1 - monthly_depreciation_rate_decimal) AS net_book_value
--        FROM
--            AssetDetails
--    ),
--    DepreciationPerPeriod AS (
--        SELECT
--            depSer.period,
--            CASE
--                WHEN depSer.period = 1 THEN (SELECT net_book_value FROM InitialDepreciation)
--                ELSE LAG(net_book_value) OVER (ORDER BY depSer.period) * (1 - AssetDetails.monthly_depreciation_rate_decimal)
--                END AS depreciation_this_period,
--            CASE
--                WHEN depSer.period = 1 THEN (SELECT net_book_value FROM InitialDepreciation)
--                ELSE LAG(net_book_value) OVER (ORDER BY depSer.period) - (LAG(net_book_value) OVER (ORDER BY depSer.period) * AssetDetails.monthly_depreciation_rate_decimal)
--                END AS net_book_value
--        FROM
--            AssetDetails, DepreciationSeries depSer
--    )
-- SELECT
--   asset_cost - SUM(depreciation_this_period) AS net_book_value_at_period_N
-- FROM
--   AssetDetails, DepreciationPerPeriod
-- WHERE
--   AssetDetails.asset_cost IS NOT NULL;

-- WITH RECURSIVE DepreciationSeries AS (
--     SELECT generate_series(1, 35) AS period
-- ),
--                AssetDetails AS (
--                    SELECT
--                        ar.asset_cost as asset_cost,
--                        cat.depreciation_rate_yearly / (10000 * 12) as monthly_depreciation_rate_decimal
--                    FROM
--                        public.asset_registration ar
--                            LEFT JOIN public.asset_category cat ON ar.asset_category_id = cat.id
--                    WHERE
--                            ar.id = '65595'
--                ),
--                InitialDepreciation AS (
--                    SELECT
--                            asset_cost * (1 - monthly_depreciation_rate_decimal) AS net_book_value
--                    FROM
--                        AssetDetails
--                ),
--                DepreciationPerPeriod AS (
--                    SELECT
--                        depSer.period,
--                        CASE
--                            WHEN depSer.period = 1 THEN (SELECT net_book_value FROM InitialDepreciation)
--                            ELSE LAG(net_book_value) OVER (ORDER BY depSer.period) * (1 - AssetDetails.monthly_depreciation_rate_decimal)
--                            END AS depreciation_this_period,
--                        CASE
--                            WHEN depSer.period = 1 THEN (SELECT net_book_value FROM InitialDepreciation)
--                            ELSE LAG(net_book_value) OVER (ORDER BY depSer.period) - (LAG(net_book_value) OVER (ORDER BY depSer.period) * AssetDetails.monthly_depreciation_rate_decimal)
--                            END AS net_book_value
--                    FROM
--                        AssetDetails, DepreciationSeries depSer
--                )
-- SELECT
--         asset_cost - SUM(depreciation_this_period) AS net_book_value_at_period_N
-- FROM
--     AssetDetails, DepreciationPerPeriod
-- WHERE
--     AssetDetails.asset_cost IS NOT NULL;


-- WITH RECURSIVE DepreciationSeries AS (
--     SELECT generate_series(1, 35) AS period
-- ),
--    AssetDetails AS (
--        SELECT
--            ar.asset_cost as asset_cost,
--            cat.depreciation_rate_yearly / (10000 * 12) as monthly_depreciation_rate_decimal
--        FROM
--            public.asset_registration ar
--                LEFT JOIN public.asset_category cat ON ar.asset_category_id = cat.id
--        WHERE
--                ar.id = '65595'
--    ),
--    InitialDepreciation AS (
--        SELECT
--                asset_cost * (1 - monthly_depreciation_rate_decimal) AS net_book_value,
--                1 AS period
--        FROM
--            AssetDetails
--    ) ,
--    DepreciationPerPeriod AS (
--        SELECT
--            d.period,
--            CASE
--                WHEN d.period = 1 THEN InitialDepreciation.net_book_value
--                ELSE LAG(d.net_book_value) OVER (ORDER BY d.period) * (1 - AssetDetails.monthly_depreciation_rate_decimal)
--                END AS depreciation_this_period,
--            CASE
--                WHEN d.period = 1 THEN InitialDepreciation.net_book_value
--                ELSE LAG(d.net_book_value) OVER (ORDER BY d.period) - (LAG(d.net_book_value) OVER (ORDER BY d.period) * AssetDetails.monthly_depreciation_rate_decimal)
--                END AS net_book_value
--        FROM
--            (SELECT * FROM InitialDepreciation
--             UNION ALL
--             SELECT d.period + 1, d.net_book_value FROM DepreciationPerPeriod d
--                                                            JOIN AssetDetails a ON d.period < 35
--            ) d
--                JOIN AssetDetails a ON a.asset_cost IS NOT NULL
--    )
-- SELECT
--         asset_cost - SUM(depreciation_this_period) AS net_book_value_at_period_N
-- FROM
--     AssetDetails, DepreciationPerPeriod

WITH RECURSIVE DepreciationSeries AS (
    SELECT generate_series(1, 35) AS period
),
               AssetDetails AS (
                   SELECT
                       ar.asset_cost AS asset_cost,
                       cat.depreciation_rate_yearly / (10000 * 12) AS monthly_depreciation_rate_decimal
                   FROM
                       public.asset_registration ar
                           LEFT JOIN public.asset_category cat ON ar.asset_category_id = cat.id
                   WHERE
                           ar.id = '65595'
               ),
               InitialDepreciation AS (
                   SELECT
                           asset_cost * (1 - monthly_depreciation_rate_decimal) AS net_book_value,
                           1 AS period
                   FROM
                       AssetDetails
               ),
               DepreciationPerPeriod AS (
                   SELECT
                       period,
                       net_book_value
                   FROM
                       InitialDepreciation

                   UNION ALL

                   SELECT
                           d.period + 1,
                           d.net_book_value * (1 - a.monthly_depreciation_rate_decimal)
                   FROM
                       DepreciationPerPeriod d
                           JOIN
                       AssetDetails a ON d.period < 35
               )
SELECT
       period,
        asset_cost - SUM(net_book_value) AS net_book_value_at_period_N
FROM
    AssetDetails ad
        JOIN
    DepreciationPerPeriod dp ON ad.asset_cost IS NOT NULL



