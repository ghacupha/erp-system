"use client";
import React from "react";
import { Package, ShoppingCart, TrendingDown, TrendingUp, Truck, Users, Warehouse } from "lucide-react";
import { useSheet } from "@/hooks/useSheet";
import {
  Supplier, Customer, Product,
  InventoryAdjustment, SupplierTransaction, CustomerTransaction,
  calcProductStock, calcAvgCost, fmtCurrency, fmtDate,
} from "@/lib/api";
import { LoadingState, StatCard } from "@/components/ui";

export default function DashboardModule() {
  const { data: suppliers }                            = useSheet<Supplier>("Suppliers");
  const { data: customers }                            = useSheet<Customer>("Customers");
  const { data: products }                             = useSheet<Product>("Products");
  const { data: adjustments }                          = useSheet<InventoryAdjustment>("Inventory_Adjustments");
  const { data: sTxns,  isLoading: stL }               = useSheet<SupplierTransaction>("Supplier_Transactions");
  const { data: cTxns,  isLoading: ctL }               = useSheet<CustomerTransaction>("Customer_Transactions");

  const totalPurchases = sTxns.reduce((s, t) => s + Number(t.Total_Price ?? 0), 0);
  const totalSales     = cTxns.reduce((s, t) => s + Number(t.Total_Price ?? 0), 0);

  // ── Actual profit: per sale line, (selling price - avg cost) × qty ────────
  const actualProfit = cTxns.reduce((sum, t) => {
    const avgCost = calcAvgCost(t.Product_ID, products, adjustments, sTxns);
    const profit  = (Number(t.Unit_Price) - avgCost) * Number(t.Quantity);
    return sum + profit;
  }, 0);

  // ── Low stock count (≤5) ─────────────────────────────────────────────────
  const lowStockCount = products.filter(p => {
    const pid = String(p.ID);
    return calcProductStock(pid, products, adjustments, sTxns, cTxns) <= 5;
  }).length;

  const recentPurchases = [...sTxns].reverse().slice(0, 5);
  const recentSales     = [...cTxns].reverse().slice(0, 5);

  return (
    <div className="space-y-6 animate-fade-in">
      {/* Row 1: entity counts */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 stagger-children">
        <StatCard label="الموردون"  value={suppliers.length}  icon={<Truck   size={20}/>} color="blue"   sub="مورد مسجّل" />
        <StatCard label="العملاء"   value={customers.length}  icon={<Users   size={20}/>} color="purple" sub="عميل مسجّل" />
        <StatCard label="المنتجات"  value={products.length}   icon={<Package size={20}/>} color="yellow"
          sub={lowStockCount > 0 ? `⚠️ ${lowStockCount} صنف أقل من 5 وحدات` : "كل المنتجات متوفرة"} />
      </div>

      {/* Row 2: financials */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-4 stagger-children">
        <StatCard
          label="إجمالي تكلفة المشتريات"
          value={`${fmtCurrency(totalPurchases)} ج`}
          icon={<ShoppingCart size={20}/>}
          color="red"
          sub={`${sTxns.length} فاتورة شراء`}
        />
        <StatCard
          label="إجمالي إيرادات المبيعات"
          value={`${fmtCurrency(totalSales)} ج`}
          icon={<TrendingUp size={20}/>}
          color="green"
          sub={`${cTxns.length} فاتورة بيع`}
        />
        <StatCard
          label="صافي الأرباح الفعلية"
          value={`${fmtCurrency(actualProfit)} ج`}
          icon={actualProfit >= 0 ? <TrendingUp size={20}/> : <TrendingDown size={20}/>}
          color={actualProfit >= 0 ? "green" : "red"}
          sub="ربح البيع الفعلي − متوسط التكلفة"
        />
      </div>

      {/* Row 3: recent activity */}
      {(stL || ctL) ? (
        <LoadingState label="جاري تحميل المعاملات..." />
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-5">
          <div className="erp-card p-5">
            <h3 className="text-sm font-700 text-slate-600 dark:text-slate-300 mb-4 flex items-center gap-2">
              <ShoppingCart size={16} className="text-red-400" />آخر المشتريات
            </h3>
            {recentPurchases.length === 0
              ? <p className="text-sm text-slate-400 text-center py-6">لا توجد مشتريات بعد</p>
              : <div className="space-y-2.5">
                  {recentPurchases.map((t, i) => (
                    <div key={i} className="flex items-center justify-between py-2 border-b border-slate-100 dark:border-slate-700 last:border-0 animate-slide-up" style={{ animationDelay: `${i * 0.05}s` }}>
                      <div>
                        <p className="text-sm font-600 text-slate-700 dark:text-slate-200">{t.Product_Name}</p>
                        <p className="text-xs text-slate-400">{t.Supplier_Name} · {fmtDate(t.Date)}</p>
                      </div>
                      <span className="text-sm font-700 text-red-500 num">{fmtCurrency(t.Total_Price)} ج</span>
                    </div>
                  ))}
                </div>
            }
          </div>
          <div className="erp-card p-5">
            <h3 className="text-sm font-700 text-slate-600 dark:text-slate-300 mb-4 flex items-center gap-2">
              <TrendingUp size={16} className="text-emerald-400" />آخر المبيعات
            </h3>
            {recentSales.length === 0
              ? <p className="text-sm text-slate-400 text-center py-6">لا توجد مبيعات بعد</p>
              : <div className="space-y-2.5">
                  {recentSales.map((t, i) => {
                    const avgCost = calcAvgCost(t.Product_ID, products, adjustments, sTxns);
                    const lineProfit = (Number(t.Unit_Price) - avgCost) * Number(t.Quantity);
                    return (
                      <div key={i} className="flex items-center justify-between py-2 border-b border-slate-100 dark:border-slate-700 last:border-0 animate-slide-up" style={{ animationDelay: `${i * 0.05}s` }}>
                        <div>
                          <p className="text-sm font-600 text-slate-700 dark:text-slate-200">{t.Product_Name}</p>
                          <p className="text-xs text-slate-400">{t.Customer_Name} · {fmtDate(t.Date)}</p>
                        </div>
                        <div className="text-right">
                          <span className="text-sm font-700 text-emerald-500 num block">{fmtCurrency(t.Total_Price)} ج</span>
                          {avgCost > 0 && (
                            <span className={`text-xs num ${lineProfit >= 0 ? "text-emerald-400" : "text-red-400"}`}>
                              ربح: {fmtCurrency(lineProfit)} ج
                            </span>
                          )}
                        </div>
                      </div>
                    );
                  })}
                </div>
            }
          </div>
        </div>
      )}
    </div>
  );
}
