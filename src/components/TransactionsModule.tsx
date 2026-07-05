"use client";
import React, { useMemo, useState } from "react";
import { AlertTriangle, Calendar, Package, Plus, ShoppingCart, Trash2, TrendingUp, Warehouse, X } from "lucide-react";
import { useSheet } from "@/hooks/useSheet";
import {
  Customer, InventoryAdjustment, Product, Supplier,
  SupplierTransaction, CustomerTransaction,
  postMultiSheet, postSheet, updateSheet,
  calcProductStock, calcAvgCost, maxId, todayStr, fmtCurrency, fmtDate,
} from "@/lib/api";
import { Badge, EmptyState, ErrorState, FormGroup, LoadingState, Modal, SectionHeader, TabBar } from "@/components/ui";

// ─── Shared stock badge ────────────────────────────────────────────────────────
function StockBadge({ stock }: { stock: number }) {
  const cls =
    stock <= 0  ? "bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400" :
    stock <= 5  ? "bg-amber-100 text-amber-700 dark:bg-amber-900/30 dark:text-amber-400" :
                  "bg-emerald-100 text-emerald-700 dark:bg-emerald-900/30 dark:text-emerald-400";
  return <span className={`inline-flex items-center rounded-lg px-2 py-0.5 text-xs font-700 num ${cls}`}>متوفر: {stock}</span>;
}

// ─── Invoice line row (shared for purchase and sale forms) ────────────────────
interface LineRow { productId: string; qty: string; unitPrice: string; expiryNote: string; }
const emptyLine = (): LineRow => ({ productId: "", qty: "1", unitPrice: "", expiryNote: "" });

// ─── Purchase Invoice Modal ────────────────────────────────────────────────────
function PurchaseModal({
  suppliers, products, existingTxns, onSave, onClose,
}: {
  suppliers: Supplier[];
  products: Product[];
  existingTxns: SupplierTransaction[];
  onSave: () => void;
  onClose: () => void;
}) {
  const [supplierId, setSupplierId] = useState("");
  const [date,       setDate]       = useState(todayStr());
  const [lines,      setLines]      = useState<LineRow[]>([emptyLine()]);
  const [saving,     setSaving]     = useState(false);

  const addLine  = () => setLines(l => [...l, emptyLine()]);
  const delLine  = (i: number) => setLines(l => l.filter((_, x) => x !== i));

  const setLine = (i: number, patch: Partial<LineRow>) =>
    setLines(l => l.map((r, x) => x === i ? { ...r, ...patch } : r));

  const onProductChange = (i: number, pid: string) => {
    const p = products.find(x => String(x.ID) === pid);
    setLine(i, { productId: pid, unitPrice: p ? String(p.Purchase_Price ?? "") : "" });
  };

  const invoiceTotal = lines.reduce((s, r) => s + (Number(r.qty) || 0) * (Number(r.unitPrice) || 0), 0);
  const canSave = supplierId && lines.every(r => r.productId && r.qty && r.unitPrice);

  const handleSave = async () => {
    if (!canSave) return;
    setSaving(true);
    try {
      const supplier = suppliers.find(s => String(s.ID) === supplierId);
      const invoiceId = `PI-${Date.now()}`;
      let nextId = maxId(existingTxns);
      const rows = lines.map(r => {
        const product = products.find(p => String(p.ID) === r.productId);
        const row = {
          ID: nextId++,
          Invoice_ID: invoiceId,
          Supplier_ID: supplierId,
          Supplier_Name: supplier?.Name ?? "",
          Product_ID: r.productId,
          Product_Name: product?.Name ?? "",
          Quantity: Number(r.qty),
          Unit_Price: Number(r.unitPrice),
          Total_Price: (Number(r.qty) || 0) * (Number(r.unitPrice) || 0),
          Date: date,
          Note: r.expiryNote.trim(),
        };
        return row as Record<string, unknown>;
      });
      await postMultiSheet("Supplier_Transactions", rows);
      onSave();
      onClose();
    } catch {
      // ignore — caller shows toast
    } finally {
      setSaving(false);
    }
  };

  return (
    <Modal
      title="🛒 فاتورة شراء جديدة"
      onClose={onClose}
      maxWidth="max-w-[680px]"
      footer={
        <>
          <button onClick={onClose} className="erp-btn border border-slate-200 dark:border-slate-600 text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">
            إلغاء
          </button>
          <button onClick={handleSave} disabled={saving || !canSave}
            className="erp-btn bg-red-500 hover:bg-red-600 text-white disabled:opacity-50">
            {saving ? "جاري الحفظ..." : `💾 حفظ الفاتورة (${fmtCurrency(invoiceTotal)} ج)`}
          </button>
        </>
      }
    >
      <div className="space-y-4">
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
          <FormGroup label="المورد" required>
            <select className="erp-input" value={supplierId} onChange={e => setSupplierId(e.target.value)}>
              <option value="">— اختر المورد —</option>
              {suppliers.map(s => <option key={s.ID} value={String(s.ID)}>{s.Name}</option>)}
            </select>
          </FormGroup>
          <FormGroup label="تاريخ الفاتورة">
            <input className="erp-input" type="date" value={date} onChange={e => setDate(e.target.value)} />
          </FormGroup>
        </div>

        {/* Product lines */}
        <div className="space-y-2">
          <p className="text-xs font-700 text-slate-500 dark:text-slate-400 uppercase tracking-wide">الأصناف</p>
          {lines.map((row, i) => (
            <div key={i} className="rounded-xl border border-slate-200 dark:border-slate-700 p-3 space-y-2 bg-slate-50 dark:bg-slate-800/50">
              <div className="grid grid-cols-3 gap-2">
                {/* Product */}
                <div className="col-span-3 sm:col-span-1">
                  <select className="erp-input text-sm" value={row.productId} onChange={e => onProductChange(i, e.target.value)}>
                    <option value="">— اختر المنتج —</option>
                    {products.map(p => <option key={p.ID} value={String(p.ID)}>{p.Name}</option>)}
                  </select>
                </div>
                {/* Qty */}
                <div>
                  <input className="erp-input text-sm text-center" type="number" min="1" step="1"
                    value={row.qty} onChange={e => setLine(i, { qty: e.target.value })} placeholder="الكمية" />
                </div>
                {/* Unit cost */}
                <div className="flex gap-1">
                  <input className="erp-input text-sm flex-1" type="number" min="0" step="0.01"
                    value={row.unitPrice} onChange={e => setLine(i, { unitPrice: e.target.value })} placeholder="سعر الشراء (ج)" />
                  {lines.length > 1 && (
                    <button onClick={() => delLine(i)} className="p-2 text-slate-400 hover:text-red-500 transition-colors">
                      <Trash2 size={14} />
                    </button>
                  )}
                </div>
              </div>
              {/* Subtotal + expiry note */}
              <div className="flex items-center justify-between gap-2 flex-wrap">
                {row.qty && row.unitPrice && (
                  <span className="text-xs text-slate-500 num">
                    الإجمالي: <strong className="text-red-600 dark:text-red-400">{fmtCurrency((Number(row.qty)||0)*(Number(row.unitPrice)||0))} ج</strong>
                  </span>
                )}
                <input className="erp-input text-xs flex-1 min-w-[160px]" value={row.expiryNote}
                  onChange={e => setLine(i, { expiryNote: e.target.value })}
                  placeholder="ملاحظة صلاحية (اختياري) مثال: 3 قطع تنتهي مارس 2026" />
              </div>
            </div>
          ))}
          <button onClick={addLine}
            className="erp-btn w-full justify-center border border-dashed border-slate-300 dark:border-slate-600 text-slate-500 hover:border-red-400 hover:text-red-500 dark:hover:border-red-500">
            <Plus size={14} /> إضافة صنف آخر
          </button>
        </div>
      </div>
    </Modal>
  );
}

// ─── Opening Stock Modal ───────────────────────────────────────────────────────
function OpeningStockModal({
  products, existingAdj, onSave, onClose,
}: {
  products: Product[];
  existingAdj: InventoryAdjustment[];
  onSave: () => void;
  onClose: () => void;
}) {
  const [lines,  setLines]  = useState<LineRow[]>([emptyLine()]);
  const [date,   setDate]   = useState(todayStr());
  const [saving, setSaving] = useState(false);

  const addLine = () => setLines(l => [...l, emptyLine()]);
  const delLine = (i: number) => setLines(l => l.filter((_, x) => x !== i));
  const setLine = (i: number, patch: Partial<LineRow>) =>
    setLines(l => l.map((r, x) => x === i ? { ...r, ...patch } : r));

  const canSave = lines.every(r => r.productId && r.qty);

  const handleSave = async () => {
    if (!canSave) return;
    setSaving(true);
    try {
      let nextId = maxId(existingAdj);
      const rows = lines.map(r => {
        const product = products.find(p => String(p.ID) === r.productId);
        return {
          ID: nextId++,
          Product_ID: r.productId,
          Product_Name: product?.Name ?? "",
          Quantity: Number(r.qty),
          Unit_Cost: Number(r.unitPrice) || 0,
          Date: date,
          Note: r.expiryNote.trim(),
        } as Record<string, unknown>;
      });
      await postMultiSheet("Inventory_Adjustments", rows);
      onSave();
      onClose();
    } catch {
      // ignore
    } finally {
      setSaving(false);
    }
  };

  return (
    <Modal
      title="📦 إضافة مخزون قديم"
      onClose={onClose}
      maxWidth="max-w-[600px]"
      footer={
        <>
          <button onClick={onClose} className="erp-btn border border-slate-200 dark:border-slate-600 text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">إلغاء</button>
          <button onClick={handleSave} disabled={saving || !canSave}
            className="erp-btn bg-blue-500 hover:bg-blue-600 text-white disabled:opacity-50">
            {saving ? "جاري الحفظ..." : "💾 إضافة للمخزن"}
          </button>
        </>
      }
    >
      <div className="mb-3 rounded-xl bg-blue-50 dark:bg-blue-900/20 border border-blue-200 dark:border-blue-800 p-3 text-xs text-blue-700 dark:text-blue-300">
        هذه الأداة لإضافة بضاعة قديمة في المخزون بدون تسجيل مورد. سعر التكلفة اختياري.
      </div>
      <div className="space-y-3">
        <FormGroup label="تاريخ الإضافة">
          <input className="erp-input" type="date" value={date} onChange={e => setDate(e.target.value)} />
        </FormGroup>
        {lines.map((row, i) => (
          <div key={i} className="rounded-xl border border-slate-200 dark:border-slate-700 p-3 space-y-2 bg-slate-50 dark:bg-slate-800/50">
            <div className="grid grid-cols-3 gap-2">
              <div className="col-span-3 sm:col-span-1">
                <select className="erp-input text-sm" value={row.productId} onChange={e => setLine(i, { productId: e.target.value })}>
                  <option value="">— اختر المنتج —</option>
                  {products.map(p => <option key={p.ID} value={String(p.ID)}>{p.Name}</option>)}
                </select>
              </div>
              <div>
                <input className="erp-input text-sm text-center" type="number" min="1" step="1"
                  value={row.qty} onChange={e => setLine(i, { qty: e.target.value })} placeholder="الكمية" />
              </div>
              <div className="flex gap-1">
                <input className="erp-input text-sm flex-1" type="number" min="0" step="0.01"
                  value={row.unitPrice} onChange={e => setLine(i, { unitPrice: e.target.value })} placeholder="التكلفة (اختياري)" />
                {lines.length > 1 && (
                  <button onClick={() => delLine(i)} className="p-2 text-slate-400 hover:text-red-500">
                    <Trash2 size={14} />
                  </button>
                )}
              </div>
            </div>
            <input className="erp-input text-xs w-full" value={row.expiryNote}
              onChange={e => setLine(i, { expiryNote: e.target.value })}
              placeholder="ملاحظة صلاحية (اختياري)" />
          </div>
        ))}
        <button onClick={addLine}
          className="erp-btn w-full justify-center border border-dashed border-slate-300 dark:border-slate-600 text-slate-500 hover:border-blue-400 hover:text-blue-500">
          <Plus size={14} /> إضافة صنف آخر
        </button>
      </div>
    </Modal>
  );
}

// ─── Sales Invoice Modal ───────────────────────────────────────────────────────
function SalesModal({
  customers, products, existingTxns,
  adjustments, supTxns,
  onSave, onClose,
}: {
  customers: Customer[];
  products: Product[];
  existingTxns: CustomerTransaction[];
  adjustments: InventoryAdjustment[];
  supTxns: SupplierTransaction[];
  onSave: () => void;
  onClose: () => void;
}) {
  const [customerId, setCustomerId] = useState("");
  const [date,       setDate]       = useState(todayStr());
  const [lines,      setLines]      = useState<LineRow[]>([emptyLine()]);
  const [saving,     setSaving]     = useState(false);

  const addLine = () => setLines(l => [...l, emptyLine()]);
  const delLine = (i: number) => setLines(l => l.filter((_, x) => x !== i));
  const setLine = (i: number, patch: Partial<LineRow>) =>
    setLines(l => l.map((r, x) => x === i ? { ...r, ...patch } : r));

  const onProductChange = (i: number, pid: string) => {
    const p = products.find(x => String(x.ID) === pid);
    setLine(i, { productId: pid, unitPrice: "" }); // selling price entered manually per invoice
  };

  const invoiceTotal = lines.reduce((s, r) => s + (Number(r.qty)||0)*(Number(r.unitPrice)||0), 0);

  // Check stock for each selected product (accounting for other lines in the same invoice)
  const stockByProduct = useMemo(() => {
    const map: Record<string, number> = {};
    products.forEach(p => {
      map[String(p.ID)] = calcProductStock(p.ID, products, adjustments, supTxns, existingTxns);
    });
    return map;
  }, [products, adjustments, supTxns, existingTxns]);

  const lineErrors = lines.map(r => {
    if (!r.productId) return null;
    const baseStock = stockByProduct[r.productId] ?? 0;
    // Subtract qty already in other lines for same product
    const otherLinesQty = lines
      .filter(l => l !== r && l.productId === r.productId)
      .reduce((s, l) => s + (Number(l.qty) || 0), 0);
    const avail = baseStock - otherLinesQty;
    return (Number(r.qty) || 0) > avail ? `المتوفر فقط: ${avail}` : null;
  });

  const hasErrors = lineErrors.some(Boolean);
  const canSave = customerId && lines.every(r => r.productId && r.qty && r.unitPrice) && !hasErrors;

  const handleSave = async () => {
    if (!canSave) return;
    setSaving(true);
    try {
      const customer = customers.find(c => String(c.ID) === customerId);
      const invoiceId = `SI-${Date.now()}`;
      let nextId = maxId(existingTxns);
      const rows = lines.map(r => {
        const product = products.find(p => String(p.ID) === r.productId);
        return {
          ID: nextId++,
          Invoice_ID: invoiceId,
          Customer_ID: customerId,
          Customer_Name: customer?.Name ?? "",
          Product_ID: r.productId,
          Product_Name: product?.Name ?? "",
          Quantity: Number(r.qty),
          Unit_Price: Number(r.unitPrice),
          Total_Price: (Number(r.qty)||0)*(Number(r.unitPrice)||0),
          Date: date,
        } as Record<string, unknown>;
      });
      await postMultiSheet("Customer_Transactions", rows);
      onSave();
      onClose();
    } catch {
      // ignore
    } finally {
      setSaving(false);
    }
  };

  return (
    <Modal
      title="🧾 فاتورة بيع جديدة"
      onClose={onClose}
      maxWidth="max-w-[680px]"
      footer={
        <>
          <button onClick={onClose} className="erp-btn border border-slate-200 dark:border-slate-600 text-slate-600 dark:text-slate-300 hover:bg-slate-50 dark:hover:bg-slate-700">إلغاء</button>
          <button onClick={handleSave} disabled={saving || !canSave}
            className="erp-btn bg-emerald-500 hover:bg-emerald-600 text-white disabled:opacity-50">
            {saving ? "جاري الحفظ..." : `💾 حفظ الفاتورة (${fmtCurrency(invoiceTotal)} ج)`}
          </button>
        </>
      }
    >
      <div className="space-y-4">
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
          <FormGroup label="العميل" required>
            <select className="erp-input" value={customerId} onChange={e => setCustomerId(e.target.value)}>
              <option value="">— اختر العميل —</option>
              {customers.map(c => <option key={c.ID} value={String(c.ID)}>{c.Name}</option>)}
            </select>
          </FormGroup>
          <FormGroup label="تاريخ الفاتورة">
            <input className="erp-input" type="date" value={date} onChange={e => setDate(e.target.value)} />
          </FormGroup>
        </div>

        <div className="space-y-2">
          <p className="text-xs font-700 text-slate-500 dark:text-slate-400 uppercase tracking-wide">الأصناف</p>
          {lines.map((row, i) => {
            const stock = row.productId ? (stockByProduct[row.productId] ?? 0) : null;
            const err = lineErrors[i];
            return (
              <div key={i} className={`rounded-xl border p-3 space-y-2 ${err ? "border-red-300 dark:border-red-700 bg-red-50 dark:bg-red-900/10" : "border-slate-200 dark:border-slate-700 bg-slate-50 dark:bg-slate-800/50"}`}>
                <div className="grid grid-cols-3 gap-2">
                  {/* Product */}
                  <div className="col-span-3 sm:col-span-1">
                    <select className="erp-input text-sm" value={row.productId} onChange={e => onProductChange(i, e.target.value)}>
                      <option value="">— اختر المنتج —</option>
                      {products.map(p => {
                        const s = stockByProduct[String(p.ID)] ?? 0;
                        return <option key={p.ID} value={String(p.ID)} disabled={s <= 0}>{p.Name} {s <= 0 ? "(نفد)" : `(متوفر: ${s})`}</option>;
                      })}
                    </select>
                  </div>
                  {/* Qty */}
                  <div>
                    <input className={`erp-input text-sm text-center ${err ? "border-red-400" : ""}`}
                      type="number" min="1" step="1" value={row.qty}
                      onChange={e => setLine(i, { qty: e.target.value })} placeholder="الكمية" />
                  </div>
                  {/* Selling price */}
                  <div className="flex gap-1">
                    <input className="erp-input text-sm flex-1" type="number" min="0" step="0.01"
                      value={row.unitPrice} onChange={e => setLine(i, { unitPrice: e.target.value })} placeholder="سعر البيع (ج)" />
                    {lines.length > 1 && (
                      <button onClick={() => delLine(i)} className="p-2 text-slate-400 hover:text-red-500"><Trash2 size={14} /></button>
                    )}
                  </div>
                </div>
                {/* Stock info + error */}
                <div className="flex items-center justify-between flex-wrap gap-2">
                  {stock !== null && (
                    <div className="flex items-center gap-2">
                      <StockBadge stock={stock} />
                      {row.qty && row.unitPrice && (
                        <span className="text-xs text-slate-500 num">
                          إجمالي: <strong className="text-emerald-600 dark:text-emerald-400">{fmtCurrency((Number(row.qty)||0)*(Number(row.unitPrice)||0))} ج</strong>
                        </span>
                      )}
                    </div>
                  )}
                  {err && <span className="flex items-center gap-1 text-xs text-red-600 font-600"><AlertTriangle size={12} />{err}</span>}
                </div>
              </div>
            );
          })}
          <button onClick={addLine}
            className="erp-btn w-full justify-center border border-dashed border-slate-300 dark:border-slate-600 text-slate-500 hover:border-emerald-400 hover:text-emerald-500">
            <Plus size={14} /> إضافة صنف آخر
          </button>
        </div>
      </div>
    </Modal>
  );
}

// ─── Grouped invoice list ──────────────────────────────────────────────────────
function InvoiceList({
  txns, isSupplier,
}: {
  txns: (SupplierTransaction | CustomerTransaction)[];
  isSupplier: boolean;
}) {
  const grouped = useMemo(() => {
    const map = new Map<string, (SupplierTransaction | CustomerTransaction)[]>();
    [...txns].reverse().forEach(t => {
      const iid = (t as SupplierTransaction).Invoice_ID || String(t.ID);
      if (!map.has(iid)) map.set(iid, []);
      map.get(iid)!.push(t);
    });
    return Array.from(map.entries());
  }, [txns]);

  if (!grouped.length) return <EmptyState label="لا توجد فواتير مسجّلة بعد" />;

  return (
    <div className="space-y-3">
      {grouped.map(([invoiceId, rows], idx) => {
        const first = rows[0] as SupplierTransaction & CustomerTransaction;
        const total = rows.reduce((s, r) => s + Number(r.Total_Price || 0), 0);
        const partyName = isSupplier ? first.Supplier_Name : first.Customer_Name;
        return (
          <div key={invoiceId} className="erp-card overflow-hidden animate-slide-up" style={{ animationDelay: `${idx * 0.03}s` }}>
            {/* Invoice header */}
            <div className={`px-4 py-2.5 flex items-center justify-between gap-2 flex-wrap ${isSupplier ? "bg-red-50 dark:bg-red-900/10" : "bg-emerald-50 dark:bg-emerald-900/10"}`}>
              <div className="flex items-center gap-2 flex-wrap">
                <Badge variant={isSupplier ? "red" : "green"}>{isSupplier ? "شراء" : "بيع"}</Badge>
                <span className="font-700 text-slate-700 dark:text-slate-200 text-sm">{partyName}</span>
                <span className="text-xs text-slate-400">{fmtDate(first.Date)}</span>
              </div>
              <span className={`font-800 num text-sm ${isSupplier ? "text-red-600 dark:text-red-400" : "text-emerald-600 dark:text-emerald-400"}`}>
                {fmtCurrency(total)} ج
              </span>
            </div>
            {/* Invoice lines */}
            <div className="divide-y divide-slate-100 dark:divide-slate-700">
              {rows.map((r, ri) => {
                const expiry = (r as SupplierTransaction).Note;
                return (
                  <div key={ri} className="px-4 py-2 flex items-center justify-between gap-2 flex-wrap text-sm">
                    <div className="flex items-center gap-3">
                      <Package size={13} className="text-slate-400 shrink-0" />
                      <span className="text-slate-700 dark:text-slate-200">{r.Product_Name}</span>
                      <span className="text-slate-400 num">×{r.Quantity}</span>
                      <span className="text-slate-400 num text-xs">@ {fmtCurrency(r.Unit_Price)} ج</span>
                    </div>
                    <div className="flex items-center gap-2">
                      {expiry && (
                        <span className="flex items-center gap-1 text-xs text-amber-600 dark:text-amber-400 bg-amber-50 dark:bg-amber-900/20 rounded-lg px-2 py-0.5">
                          <Calendar size={11} />{expiry}
                        </span>
                      )}
                      <span className={`num font-600 text-xs ${isSupplier ? "text-red-600 dark:text-red-400" : "text-emerald-600 dark:text-emerald-400"}`}>
                        {fmtCurrency(r.Total_Price)} ج
                      </span>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        );
      })}
    </div>
  );
}

// ─── Inventory Tab ─────────────────────────────────────────────────────────────
function InventoryTab({
  products, adjustments, supTxns, cusTxns, showToast, refetchAdj, refetchSup,
}: {
  products: Product[];
  adjustments: InventoryAdjustment[];
  supTxns: SupplierTransaction[];
  cusTxns: CustomerTransaction[];
  showToast: (m: string, t: "success" | "error") => void;
  refetchAdj: () => void;
  refetchSup: () => void;
}) {
  const [clearingId, setClearingId] = useState<number | null>(null);

  const inventory = useMemo(() => products.map(p => {
    const pid = String(p.ID);
    const stock = calcProductStock(pid, products, adjustments, supTxns, cusTxns);
    // Avg cost from purchases + adjustments with cost
    const avgCost   = calcAvgCost(pid, products, adjustments, supTxns);
    return { product: p, stock, avgCost, stockValue: stock * avgCost };
  }), [products, adjustments, supTxns, cusTxns]);

  // Collect all expiry notes (from purchase txns + adjustments)
  const expiryNotes = useMemo(() => {
    const notes: { id: number; sheet: string; productName: string; note: string }[] = [];
    supTxns.forEach(t => {
      if (t.Note?.trim()) notes.push({ id: t.ID, sheet: "Supplier_Transactions", productName: t.Product_Name, note: t.Note });
    });
    adjustments.forEach(a => {
      if (a.Note?.trim()) notes.push({ id: a.ID, sheet: "Inventory_Adjustments", productName: a.Product_Name, note: a.Note });
    });
    return notes;
  }, [supTxns, adjustments]);

  const clearNote = async (id: number, sheet: string) => {
    setClearingId(id);
    try {
      await updateSheet(sheet, { ID: id, Note: "" });
      showToast("تم مسح الملاحظة ✓", "success");
      sheet === "Inventory_Adjustments" ? refetchAdj() : refetchSup();
    } catch {
      showToast("فشل في المسح", "error");
    } finally {
      setClearingId(null);
    }
  };

  const totalValue = inventory.reduce((s, r) => s + r.stockValue, 0);

  return (
    <div className="animate-fade-in space-y-5">
      {/* Stock summary */}
      <div className="erp-card overflow-hidden">
        <div className="px-5 py-3 border-b border-slate-100 dark:border-slate-700 flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Warehouse size={16} className="text-blue-500" />
            <span className="font-700 text-sm text-slate-700 dark:text-slate-200">المخزون الحالي</span>
          </div>
          <span className="text-xs text-slate-400">قيمة إجمالية: <strong className="num text-blue-600 dark:text-blue-400">{fmtCurrency(totalValue)} ج</strong></span>
        </div>
        <div className="overflow-x-auto">
          <table className="erp-table">
            <thead>
              <tr>
                <th>المنتج</th>
                <th className="text-center">المتوفر</th>
                <th className="text-center">متوسط التكلفة</th>
                <th className="text-center">قيمة المخزون</th>
              </tr>
            </thead>
            <tbody>
              {inventory.length === 0 ? (
                <tr><td colSpan={4}><EmptyState label="لا توجد منتجات" /></td></tr>
              ) : inventory.map((r, i) => (
                <tr key={r.product.ID} className="animate-slide-up" style={{ animationDelay: `${i * 0.03}s` }}>
                  <td className="font-600 text-slate-800 dark:text-slate-100">{r.product.Name}</td>
                  <td className="text-center"><StockBadge stock={r.stock} /></td>
                  <td className="text-center num text-slate-500 text-sm">
                    {r.avgCost > 0 ? `${fmtCurrency(r.avgCost)} ج` : "—"}
                  </td>
                  <td className="text-center num text-blue-600 dark:text-blue-400 font-600 text-sm">
                    {r.stockValue > 0 ? `${fmtCurrency(r.stockValue)} ج` : "—"}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* Expiry notes — always visible */}
      <div className="erp-card overflow-hidden">
        <div className="px-5 py-3 border-b border-slate-100 dark:border-slate-700 flex items-center gap-2">
          <Calendar size={16} className="text-amber-500" />
          <span className="font-700 text-sm text-slate-700 dark:text-slate-200">تنبيهات الصلاحية</span>
          {expiryNotes.length > 0 && <Badge variant="yellow">{expiryNotes.length}</Badge>}
        </div>
        <div className="divide-y divide-slate-100 dark:divide-slate-700">
          {expiryNotes.length === 0 ? (
            <div className="px-5 py-5 text-center text-sm text-slate-400 dark:text-slate-500">
              لا توجد ملاحظات صلاحية حالياً
            </div>
          ) : expiryNotes.map(n => (
            <div key={`${n.sheet}-${n.id}`} className="px-4 py-3 flex items-center justify-between gap-3 flex-wrap">
              <div>
                <p className="text-sm font-600 text-slate-700 dark:text-slate-200">{n.productName}</p>
                <p className="text-xs text-amber-600 dark:text-amber-400 mt-0.5">{n.note}</p>
              </div>
              <button
                onClick={() => clearNote(n.id, n.sheet)}
                disabled={clearingId === n.id}
                className="erp-btn px-2.5 py-1.5 text-xs border border-slate-200 dark:border-slate-600 text-slate-500 hover:text-red-500 hover:border-red-300 disabled:opacity-40"
              >
                <X size={12} /> {clearingId === n.id ? "..." : "مسح الملاحظة"}
              </button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

// ─── Main module ───────────────────────────────────────────────────────────────
export default function TransactionsModule({ showToast }: { showToast: (m: string, t: "success" | "error") => void }) {
  const [tab, setTab] = useState("purchases");

  const { data: suppliers,  isLoading: lSup }                    = useSheet<Supplier>("Suppliers");
  const { data: customers,  isLoading: lCus }                    = useSheet<Customer>("Customers");
  const { data: products,   isLoading: lPro }                    = useSheet<Product>("Products");
  const { data: supTxns,    isLoading: lST,  refetch: rST }      = useSheet<SupplierTransaction>("Supplier_Transactions");
  const { data: cusTxns,    isLoading: lCT,  refetch: rCT }      = useSheet<CustomerTransaction>("Customer_Transactions");
  const { data: adjustments, isLoading: lAdj, refetch: rAdj }    = useSheet<InventoryAdjustment>("Inventory_Adjustments");

  const [showPurchaseModal, setShowPurchaseModal] = useState(false);
  const [showOpeningModal,  setShowOpeningModal]  = useState(false);
  const [showSalesModal,    setShowSalesModal]    = useState(false);

  const isLoading = lSup || lCus || lPro || lST || lCT || lAdj;

  const tabs = [
    { id: "purchases", label: "المشتريات",   icon: <ShoppingCart size={15} /> },
    { id: "sales",     label: "المبيعات",     icon: <TrendingUp   size={15} /> },
    { id: "inventory", label: "المخزن",       icon: <Warehouse    size={15} /> },
  ];

  const handleSavePurchase = () => {
    showToast("تمت إضافة فاتورة الشراء ✓", "success");
    setTimeout(() => { rST(); }, 1500);
  };
  const handleSaveOpening = () => {
    showToast("تمت إضافة المخزون القديم ✓", "success");
    setTimeout(() => { rAdj(); }, 1500);
  };
  const handleSaveSale = () => {
    showToast("تمت إضافة فاتورة البيع ✓", "success");
    setTimeout(() => { rCT(); }, 1500);
  };

  if (isLoading) return <LoadingState />;

  return (
    <div>
      <TabBar tabs={tabs} active={tab} onChange={setTab} />

      {/* Purchases */}
      {tab === "purchases" && (
        <div className="animate-fade-in space-y-4">
          <SectionHeader
            title="فواتير الشراء"
            count={supTxns.length}
            action={
              <div className="flex gap-2">
                <button onClick={() => setShowOpeningModal(true)}
                  className="erp-btn border border-blue-300 dark:border-blue-700 text-blue-600 dark:text-blue-400 hover:bg-blue-50 dark:hover:bg-blue-900/20">
                  <Package size={15} /> مخزون قديم
                </button>
                <button onClick={() => setShowPurchaseModal(true)}
                  className="erp-btn bg-red-500 hover:bg-red-600 text-white">
                  <Plus size={15} /> فاتورة شراء
                </button>
              </div>
            }
          />
          <InvoiceList txns={supTxns} isSupplier={true} />
        </div>
      )}

      {/* Sales */}
      {tab === "sales" && (
        <div className="animate-fade-in space-y-4">
          <SectionHeader
            title="فواتير البيع"
            count={cusTxns.length}
            action={
              <button onClick={() => setShowSalesModal(true)}
                className="erp-btn bg-emerald-500 hover:bg-emerald-600 text-white">
                <Plus size={15} /> فاتورة بيع
              </button>
            }
          />
          <InvoiceList txns={cusTxns} isSupplier={false} />
        </div>
      )}

      {/* Inventory */}
      {tab === "inventory" && (
        <InventoryTab
          products={products}
          adjustments={adjustments}
          supTxns={supTxns}
          cusTxns={cusTxns}
          showToast={showToast}
          refetchAdj={rAdj}
          refetchSup={rST}
        />
      )}

      {/* Modals */}
      {showPurchaseModal && (
        <PurchaseModal
          suppliers={suppliers} products={products} existingTxns={supTxns}
          onSave={handleSavePurchase} onClose={() => setShowPurchaseModal(false)}
        />
      )}
      {showOpeningModal && (
        <OpeningStockModal
          products={products} existingAdj={adjustments}
          onSave={handleSaveOpening} onClose={() => setShowOpeningModal(false)}
        />
      )}
      {showSalesModal && (
        <SalesModal
          customers={customers} products={products} existingTxns={cusTxns}
          adjustments={adjustments} supTxns={supTxns}
          onSave={handleSaveSale} onClose={() => setShowSalesModal(false)}
        />
      )}
    </div>
  );
}
