"use client";
import React, { useMemo, useState } from "react";
import { FileText, Package, TrendingDown, TrendingUp } from "lucide-react";
import { useSheet } from "@/hooks/useSheet";
import {
  Customer, InventoryAdjustment, Product, Supplier,
  SupplierTransaction, CustomerTransaction,
  SupplierPayment, CustomerPayment,
  calcProductStock, calcAvgCost,
  fmtCurrency, fmtDate,
} from "@/lib/api";
import { Badge, EmptyState, ErrorState, LoadingState, TabBar } from "@/components/ui";

// ─── Stock badge ───────────────────────────────────────────────────────────────
function StockBadge({ stock }: { stock: number }) {
  const cls =
    stock <= 0 ? "text-red-600 bg-red-50 dark:bg-red-900/20" :
    stock <= 5 ? "text-amber-600 bg-amber-50 dark:bg-amber-900/20" :
                 "text-emerald-700 bg-emerald-50 dark:bg-emerald-900/20";
  return <span className={`inline-block rounded-lg px-3 py-1 text-sm font-700 num ${cls}`}>{stock}</span>;
}

// ─── Supplier Statements ───────────────────────────────────────────────────────
function SupplierStatements() {
  const { data: suppliers,    isLoading: lS,  isError: eS,  refetch: rS  } = useSheet<Supplier>("Suppliers");
  const { data: products,     isLoading: lP,  isError: eP,  refetch: rP  } = useSheet<Product>("Products");
  const { data: adjustments,  isLoading: lA,  isError: eA               } = useSheet<InventoryAdjustment>("Inventory_Adjustments");
  const { data: supTxns,      isLoading: lT,  isError: eT,  refetch: rT  } = useSheet<SupplierTransaction>("Supplier_Transactions");
  const { data: cusTxns,      isLoading: lC,  isError: eC               } = useSheet<CustomerTransaction>("Customer_Transactions");
  const { data: payments,     isLoading: lPy                             } = useSheet<SupplierPayment>("Supplier_Payments");

  const [selectedId, setSelectedId] = useState("all");

  const isLoading = lS || lP || lA || lT || lC || lPy;
  const isError   = eS || eP || eA || eT || eC;

  const statement = useMemo(() => {
    const rows = selectedId === "all"
      ? supTxns
      : supTxns.filter(t => String(t.Supplier_ID) === selectedId);

    // Group by product
    const byProduct: Record<string, { name: string; qty: number; total: number }> = {};
    rows.forEach(t => {
      const pid = String(t.Product_ID);
      if (!byProduct[pid]) byProduct[pid] = { name: t.Product_Name, qty: 0, total: 0 };
      byProduct[pid].qty   += Number(t.Quantity  || 0);
      byProduct[pid].total += Number(t.Total_Price || 0);
    });

    const totalPurchases = rows.reduce((s, t) => s + Number(t.Total_Price || 0), 0);
    const totalPaid      = payments
      .filter(p => selectedId === "all" || String(p.Supplier_ID) === selectedId)
      .reduce((s, p) => s + Number(p.Amount || 0), 0);

    return { byProduct, totalPurchases, totalPaid, balance: totalPurchases - totalPaid };
  }, [supTxns, payments, selectedId]);

  if (isLoading) return <LoadingState />;
  if (isError)   return <ErrorState onRetry={() => { rS(); rP(); rT(); }} />;

  const supplierName = selectedId === "all"
    ? "جميع الموردين"
    : (suppliers.find(s => String(s.ID) === selectedId)?.Name ?? "");

  return (
    <div className="animate-fade-in space-y-4">
      {/* Selector */}
      <div className="erp-card p-4">
        <div className="flex flex-col sm:flex-row gap-3 items-start sm:items-center">
          <label className="text-sm font-600 text-slate-600 dark:text-slate-300 shrink-0">اختر المورد:</label>
          <select className="erp-input flex-1" value={selectedId} onChange={e => setSelectedId(e.target.value)}>
            <option value="all">📋 إجمالي جميع الموردين</option>
            {suppliers.map(s => <option key={s.ID} value={String(s.ID)}>{s.Name}</option>)}
          </select>
        </div>
      </div>

      {/* Summary cards */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
        {[
          { label: "إجمالي المشتريات",   val: statement.totalPurchases, cls: "text-red-600 dark:text-red-400" },
          { label: "إجمالي المدفوع",      val: statement.totalPaid,      cls: "text-emerald-600 dark:text-emerald-400" },
          { label: "الرصيد المتبقي",      val: statement.balance, cls: statement.balance > 0 ? "text-red-600 dark:text-red-400" : "text-emerald-600 dark:text-emerald-400" },
        ].map(c => (
          <div key={c.label} className="erp-card p-4">
            <p className="text-xs text-slate-500 dark:text-slate-400 mb-1">{c.label}</p>
            <p className={`text-xl font-800 num ${c.cls}`}>{fmtCurrency(c.val)} ج</p>
          </div>
        ))}
      </div>

      {/* Products breakdown */}
      <div className="erp-card overflow-hidden">
        <div className="px-5 py-3 border-b border-slate-100 dark:border-slate-700 flex items-center gap-2">
          <Package size={16} className="text-red-500" />
          <span className="font-700 text-sm text-slate-700 dark:text-slate-200">
            المنتجات المورّدة — {supplierName}
          </span>
        </div>
        <div className="overflow-x-auto">
          <table className="erp-table">
            <thead>
              <tr>
                <th>المنتج</th>
                <th className="text-center">الكمية الواردة</th>
                <th className="text-center">الكمية المباعة</th>
                <th className="text-center text-emerald-600 dark:text-emerald-400">المتوفر</th>
                <th className="text-center">متوسط التكلفة</th>
                <th className="text-left">إجمالي الشراء</th>
              </tr>
            </thead>
            <tbody>
              {Object.keys(statement.byProduct).length === 0
                ? <tr><td colSpan={6} className="text-center py-8 text-slate-400">لا توجد فواتير لهذا المورد</td></tr>
                : Object.entries(statement.byProduct).map(([pid, info]) => {
                  const stock   = calcProductStock(pid, products, adjustments, supTxns, cusTxns);
                  const avgCost = calcAvgCost(pid, products, adjustments, supTxns);
                  const sold    = cusTxns.filter(t => String(t.Product_ID) === pid).reduce((s, t) => s + Number(t.Quantity || 0), 0);
                  return (
                    <tr key={pid} className="animate-slide-up">
                      <td>
                        <div className="flex items-center gap-2">
                          <div className="w-7 h-7 rounded-lg bg-red-100 dark:bg-red-900/30 flex items-center justify-center">
                            <TrendingDown size={13} className="text-red-600 dark:text-red-400" />
                          </div>
                          <span className="font-600 text-slate-800 dark:text-slate-100">{info.name}</span>
                        </div>
                      </td>
                      <td className="text-center num text-slate-600 dark:text-slate-300">{info.qty}</td>
                      <td className="text-center num text-slate-500">{sold}</td>
                      <td className="text-center"><StockBadge stock={stock} /></td>
                      <td className="text-center num text-slate-500 text-sm">
                        {avgCost > 0 ? `${fmtCurrency(avgCost)} ج` : "—"}
                      </td>
                      <td className="text-left num text-red-600 dark:text-red-400 font-600">{fmtCurrency(info.total)} ج</td>
                    </tr>
                  );
                })
              }
            </tbody>
          </table>
        </div>
      </div>

      {/* Detailed transactions */}
      <div className="erp-card overflow-hidden">
        <div className="px-5 py-3 border-b border-slate-100 dark:border-slate-700 flex items-center gap-2">
          <FileText size={16} className="text-slate-500" />
          <span className="font-700 text-sm text-slate-700 dark:text-slate-200">سجل الفواتير التفصيلي</span>
        </div>
        <div className="overflow-x-auto">
          <table className="erp-table">
            <thead>
              <tr><th>رقم</th><th>المورد</th><th>المنتج</th><th>الكمية</th><th>سعر الشراء</th><th>الإجمالي</th><th>التاريخ</th></tr>
            </thead>
            <tbody>
              {(selectedId === "all" ? supTxns : supTxns.filter(t => String(t.Supplier_ID) === selectedId))
                .slice().reverse().map((t, i) => (
                  <tr key={t.ID} className="animate-slide-up" style={{ animationDelay: `${i * 0.02}s` }}>
                    <td><Badge variant="red">#{t.ID}</Badge></td>
                    <td className="font-600 text-slate-700 dark:text-slate-200">{t.Supplier_Name}</td>
                    <td className="text-slate-600 dark:text-slate-300">{t.Product_Name}</td>
                    <td className="num text-slate-500">{t.Quantity}</td>
                    <td className="num text-slate-500">{fmtCurrency(t.Unit_Price)} ج</td>
                    <td className="num text-red-600 dark:text-red-400 font-600">{fmtCurrency(t.Total_Price)} ج</td>
                    <td className="text-slate-400 text-sm">{fmtDate(t.Date)}</td>
                  </tr>
                ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

// ─── Customer Statements ───────────────────────────────────────────────────────
function CustomerStatements() {
  const { data: customers,   isLoading: lC,  isError: eC,  refetch: rC  } = useSheet<Customer>("Customers");
  const { data: products,    isLoading: lP,  isError: eP,  refetch: rP  } = useSheet<Product>("Products");
  const { data: adjustments, isLoading: lA,  isError: eA               } = useSheet<InventoryAdjustment>("Inventory_Adjustments");
  const { data: supTxns,     isLoading: lS,  isError: eS               } = useSheet<SupplierTransaction>("Supplier_Transactions");
  const { data: cusTxns,     isLoading: lT,  isError: eT,  refetch: rT  } = useSheet<CustomerTransaction>("Customer_Transactions");
  const { data: payments,    isLoading: lPy                             } = useSheet<CustomerPayment>("Customer_Payments");

  const [selectedId, setSelectedId] = useState("all");

  const isLoading = lC || lP || lA || lS || lT || lPy;
  const isError   = eC || eP || eA || eS || eT;

  const statement = useMemo(() => {
    const rows = selectedId === "all"
      ? cusTxns
      : cusTxns.filter(t => String(t.Customer_ID) === selectedId);

    const byProduct: Record<string, { name: string; qty: number; revenue: number; profit: number }> = {};
    rows.forEach(t => {
      const pid     = String(t.Product_ID);
      const avgCost = calcAvgCost(pid, products, adjustments, supTxns);
      const profit  = (Number(t.Unit_Price) - avgCost) * Number(t.Quantity);
      if (!byProduct[pid]) byProduct[pid] = { name: t.Product_Name, qty: 0, revenue: 0, profit: 0 };
      byProduct[pid].qty     += Number(t.Quantity || 0);
      byProduct[pid].revenue += Number(t.Total_Price || 0);
      byProduct[pid].profit  += profit;
    });

    const totalSales  = rows.reduce((s, t) => s + Number(t.Total_Price || 0), 0);
    const totalProfit = Object.values(byProduct).reduce((s, p) => s + p.profit, 0);
    const totalPaid   = payments
      .filter(p => selectedId === "all" || String(p.Customer_ID) === selectedId)
      .reduce((s, p) => s + Number(p.Amount || 0), 0);

    return { byProduct, totalSales, totalProfit, totalPaid, balance: totalSales - totalPaid };
  }, [cusTxns, payments, adjustments, supTxns, selectedId]);

  if (isLoading) return <LoadingState />;
  if (isError)   return <ErrorState onRetry={() => { rC(); rP(); rT(); }} />;

  const customerName = selectedId === "all"
    ? "جميع العملاء"
    : (customers.find(c => String(c.ID) === selectedId)?.Name ?? "");

  return (
    <div className="animate-fade-in space-y-4">
      {/* Selector */}
      <div className="erp-card p-4">
        <div className="flex flex-col sm:flex-row gap-3 items-start sm:items-center">
          <label className="text-sm font-600 text-slate-600 dark:text-slate-300 shrink-0">اختر العميل:</label>
          <select className="erp-input flex-1" value={selectedId} onChange={e => setSelectedId(e.target.value)}>
            <option value="all">📋 إجمالي جميع العملاء</option>
            {customers.map(c => <option key={c.ID} value={String(c.ID)}>{c.Name}</option>)}
          </select>
        </div>
      </div>

      {/* Summary */}
      <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
        {[
          { label: "إجمالي المبيعات",  val: statement.totalSales,   cls: "text-emerald-600 dark:text-emerald-400" },
          { label: "الربح الفعلي",     val: statement.totalProfit,  cls: statement.totalProfit >= 0 ? "text-emerald-600 dark:text-emerald-400" : "text-red-600 dark:text-red-400" },
          { label: "المُحصَّل",        val: statement.totalPaid,    cls: "text-blue-600 dark:text-blue-400" },
          { label: "الرصيد المتبقي",   val: statement.balance,      cls: statement.balance > 0 ? "text-red-600 dark:text-red-400" : "text-emerald-600 dark:text-emerald-400" },
        ].map(c => (
          <div key={c.label} className="erp-card p-4">
            <p className="text-xs text-slate-500 dark:text-slate-400 mb-1">{c.label}</p>
            <p className={`text-lg font-800 num ${c.cls}`}>{fmtCurrency(c.val)} ج</p>
          </div>
        ))}
      </div>

      {/* Products breakdown with profit */}
      <div className="erp-card overflow-hidden">
        <div className="px-5 py-3 border-b border-slate-100 dark:border-slate-700 flex items-center gap-2">
          <Package size={16} className="text-emerald-500" />
          <span className="font-700 text-sm text-slate-700 dark:text-slate-200">
            المنتجات المباعة — {customerName}
          </span>
        </div>
        <div className="overflow-x-auto">
          <table className="erp-table">
            <thead>
              <tr>
                <th>المنتج</th>
                <th className="text-center">الكمية المباعة</th>
                <th className="text-center text-emerald-600 dark:text-emerald-400">المتوفر حاليًا</th>
                <th className="text-center">إجمالي البيع</th>
                <th className="text-center">الربح الفعلي</th>
              </tr>
            </thead>
            <tbody>
              {Object.keys(statement.byProduct).length === 0
                ? <tr><td colSpan={5} className="text-center py-8 text-slate-400">لا توجد فواتير لهذا العميل</td></tr>
                : Object.entries(statement.byProduct).map(([pid, info]) => {
                  const stock = calcProductStock(pid, products, adjustments, supTxns, cusTxns);
                  const product = products.find(p => String(p.ID) === pid);
                  return (
                    <tr key={pid} className="animate-slide-up">
                      <td>
                        <div className="flex items-center gap-2">
                          <div className="w-7 h-7 rounded-lg bg-emerald-100 dark:bg-emerald-900/30 flex items-center justify-center">
                            <TrendingUp size={13} className="text-emerald-600 dark:text-emerald-400" />
                          </div>
                          <span className="font-600 text-slate-800 dark:text-slate-100">{info.name}</span>
                        </div>
                      </td>
                      <td className="text-center num text-slate-600 dark:text-slate-300">{info.qty}</td>
                      <td className="text-center"><StockBadge stock={stock} /></td>
                      <td className="text-center num text-emerald-600 dark:text-emerald-400 font-600">{fmtCurrency(info.revenue)} ج</td>
                      <td className="text-center">
                        <span className={`num font-700 text-sm ${info.profit >= 0 ? "text-emerald-600 dark:text-emerald-400" : "text-red-600 dark:text-red-400"}`}>
                          {info.profit >= 0 ? "+" : ""}{fmtCurrency(info.profit)} ج
                        </span>
                      </td>
                    </tr>
                  );
                })
              }
            </tbody>
          </table>
        </div>
      </div>

      {/* Detailed transactions */}
      <div className="erp-card overflow-hidden">
        <div className="px-5 py-3 border-b border-slate-100 dark:border-slate-700 flex items-center gap-2">
          <FileText size={16} className="text-slate-500" />
          <span className="font-700 text-sm text-slate-700 dark:text-slate-200">سجل الفواتير التفصيلي</span>
        </div>
        <div className="overflow-x-auto">
          <table className="erp-table">
            <thead>
              <tr><th>رقم</th><th>العميل</th><th>المنتج</th><th>الكمية</th><th>سعر البيع</th><th>الإجمالي</th><th>الربح</th><th>التاريخ</th></tr>
            </thead>
            <tbody>
              {(selectedId === "all" ? cusTxns : cusTxns.filter(t => String(t.Customer_ID) === selectedId))
                .slice().reverse().map((t, i) => {
                  const avgCost   = calcAvgCost(t.Product_ID, products, adjustments, supTxns);
                  const lineProfit = (Number(t.Unit_Price) - avgCost) * Number(t.Quantity);
                  return (
                    <tr key={t.ID} className="animate-slide-up" style={{ animationDelay: `${i * 0.02}s` }}>
                      <td><Badge variant="green">#{t.ID}</Badge></td>
                      <td className="font-600 text-slate-700 dark:text-slate-200">{t.Customer_Name}</td>
                      <td className="text-slate-600 dark:text-slate-300">{t.Product_Name}</td>
                      <td className="num text-slate-500">{t.Quantity}</td>
                      <td className="num text-slate-500">{fmtCurrency(t.Unit_Price)} ج</td>
                      <td className="num text-emerald-600 dark:text-emerald-400 font-600">{fmtCurrency(t.Total_Price)} ج</td>
                      <td>
                        {avgCost > 0
                          ? <span className={`num text-xs font-600 ${lineProfit >= 0 ? "text-emerald-500" : "text-red-500"}`}>
                              {lineProfit >= 0 ? "+" : ""}{fmtCurrency(lineProfit)} ج
                            </span>
                          : <span className="text-xs text-slate-400">—</span>
                        }
                      </td>
                      <td className="text-slate-400 text-sm">{fmtDate(t.Date)}</td>
                    </tr>
                  );
                })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

// ─── Module ────────────────────────────────────────────────────────────────────
export default function StatementsModule() {
  const [tab, setTab] = useState("suppliers");
  const tabs = [
    { id: "suppliers", label: "كشف الموردين", icon: <TrendingDown size={15} /> },
    { id: "customers", label: "كشف العملاء",  icon: <TrendingUp   size={15} /> },
  ];
  return (
    <div>
      <TabBar tabs={tabs} active={tab} onChange={setTab} />
      {tab === "suppliers" && <SupplierStatements />}
      {tab === "customers" && <CustomerStatements />}
    </div>
  );
}
