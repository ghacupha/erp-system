-- WITH AssetDetails AS (
--    SELECT
--    ar.asset_cost AS asset_cost,
--    cat.depreciation_rate_yearly AS depreciation_rate_basis_points,
--    1000000.0 / cat.depreciation_rate_yearly AS inferred_useful_life
--  FROM
--    asset_registration ar
--   LEFT JOIN asset_category cat ON ar.asset_category_id = cat.id
--  WHERE
--    ar.id = :asset_id
--    ),
--    DepreciationPerPeriod AS (
--  SELECT
--    generate_series(1, inferred_useful_life) AS period,
--    asset_cost / inferred_useful_life AS depreciation_per_period
--  FROM
--    AssetDetails
--     ),
--    AccumulatedDepreciation AS (
--  SELECT
--    sum(depreciation_per_period) AS accrued_depreciation_at_period_N
--  FROM
--    DepreciationPerPeriod
--  WHERE
--    period <= :period_n
--   ),
--   NetBookValue AS (
--  SELECT
--    asset_cost - accrued_depreciation_at_period_N AS net_book_value_at_period_N
--  FROM
--    AssetDetails, AccumulatedDepreciation
--   )
--  SELECT
--     net_book_value_at_period_N
--  FROM
--    NetBookValue;

WITH AssetDetails AS (
    SELECT
        ar.asset_cost AS asset_cost,
        cat.depreciation_rate_yearly / (10000 * 12) AS monthly_depreciation_rate_decimal
    FROM
        asset_registration ar
            LEFT JOIN asset_category cat ON ar.asset_category_id = cat.id
    WHERE
            ar.id = '69726'
)
SELECT
        asset_cost - (monthly_depreciation_rate_decimal * asset_cost * 19) AS net_book_value_at_period_N
FROM
    AssetDetails;
